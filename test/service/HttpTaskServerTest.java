package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.historyManagers.InMemoryHistoryManager;
import service.httpTaskManagers.HttpTaskServer;
import service.taskManagers.InMemoryTaskManager;
import service.taskManagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private static HttpServer httpServer;
    private static TaskManager taskManager;
    private static Gson gson;
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + PORT;

    @BeforeAll
    public static void startServer() throws IOException {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServer(taskManager));
        httpServer.start();
        System.out.println("Server started on port " + PORT);

        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    @AfterAll
    public static void stopServer() {
        httpServer.stop(0);
    }

    @Test
    public void testCreateTask() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/tasks");

        Task task = new Task("travel", "buy ticket", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2020, 12, 12, 15, 15, 0));

        taskManager.createTask(task);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .setPrettyPrinting()
                .create();

        String jsonTask = gson1.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String body = gson1.fromJson(response.body(), String.class);

        assertEquals("Задача успешно создана", body);
    }

    @Test
    public void testGetTask() throws Exception {
        Task task = new Task("travel", "buy ticket", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2020, 12, 13, 15, 15, 0));

        taskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/tasks/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getId(), receivedTask.getId());
        assertEquals(task.getName(), receivedTask.getName());
        assertEquals(task.getDescription(), receivedTask.getDescription());
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task = new Task("travel", "buy ticket", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2020, 12, 15, 15, 15, 0));
        taskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/tasks/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        assertEquals(null, taskManager.getTaskById(task.getId()));
    }



    static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            jsonWriter.value(localDate.format(dtf));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    static class DurationTypeAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            return Duration.parse(in.nextString());
        }
    }
}

