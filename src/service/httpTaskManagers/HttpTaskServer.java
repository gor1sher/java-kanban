package service.httpTaskManagers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import model.Task;
import service.taskManagers.TaskManager;
import service.taskManagers.exception.ValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer implements HttpHandler {

    TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String requestPath = exchange.getRequestURI().getPath();
            String[] pathParts = requestPath.split("/");
            switch (requestMethod) {
                case "GET":
                    get(pathParts, exchange);
                    break;
                case "POST":
                    post(pathParts, exchange);
                    break;
                case "DELETE":
                    delete(pathParts, exchange);
                    break;
                default:
                    writeResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (IOException e) {
            writeResponse(exchange, "Внутренняя ошибка сервера", 500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            writeResponse(exchange, "Неизвестная ошибка", 500);
        }
    }

    public void delete(String[] pathParts, HttpExchange exchange) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2].trim());
            switch (pathParts[1]) {
                case "tasks":
                    Task task = taskManager.getTaskById(id);
                    if (task == null) {
                        writeResponse(exchange, "Задача не найдена", 404);
                        return;
                    }
                    taskManager.removeTask(task.getId());
                    String removeTask = new Gson().toJson("Задача успешно удалена");
                    writeResponse(exchange, removeTask, 204);
                    break;
                case "subtasks":
                    Subtask subtask = taskManager.getSubtaskById(id);
                    if (subtask == null) {
                        writeResponse(exchange, "Подзадача не найдена", 404);
                        return;
                    }
                    taskManager.removeSubtask(subtask.getId());
                    String removeSubtask = new Gson().toJson("Подзадача успешно удалена");
                    writeResponse(exchange, removeSubtask, 204);
                    break;
                case "epics":
                    Epic epic = taskManager.getEpicById(id);
                    if (epic == null) {
                        writeResponse(exchange, "Эпик не найден", 404);
                        return;
                    }
                    taskManager.removeEpic(epic.getId());
                    String removeEpic = new Gson().toJson("Эпик успешно удален");
                    writeResponse(exchange, removeEpic, 204);
                    break;
                default:
                    writeResponse(exchange, "Неверный тип задачи", 400);
                    break;
            }
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Неверный формат идентификатора", 400);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            writeResponse(exchange, "Ошибка ввода-вывода", 500);
        }
    }

    private void post(String[] pathParts, HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();

        try {
            switch (pathParts[1]) {
                case "tasks":
                    Task task = taskFromJson(inputStream);
                    if (task.getId() >= 0) {
                        taskManager.updateTask(task);
                    } else {
                        taskManager.createTask(task);
                    }

                    String taskJson = new Gson().toJson("Задача успешно создана");
                    writeResponse(exchange, taskJson, 201);
                    break;
                case "subtasks":
                    Subtask subtask = subtaskFromJson(inputStream);
                    if (subtask.getId() >= 0) {
                        taskManager.updateSubtask(subtask);
                    } else {
                        taskManager.createSubtask(subtask);
                    }
                    String subtaskJson = new Gson().toJson("Подзадача успешно создана");
                    writeResponse(exchange, subtaskJson, 201);
                    break;
                case "epics":
                    Epic epic = epicFromJson(inputStream);
                    taskManager.createEpic(epic);
                    String epicJson = new Gson().toJson("Эпик успешно создан");
                    writeResponse(exchange, epicJson, 201);
                    break;
                default:
                    writeResponse(exchange, "Неверный тип задачи", 400);
                    break;
            }
        } catch (ValidationException e) {
            writeResponse(exchange, "Ошибка валидации: " + e.getMessage(), 409);
        }
    }

    private String getString(InputStream inputStream) throws IOException {
        byte[] requestBody = inputStream.readAllBytes();
        return new String(requestBody, StandardCharsets.UTF_8);
    }

    private Task taskFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .setPrettyPrinting()
                .create();
        return gson.fromJson(requestBody, Task.class);
    }

    private Subtask subtaskFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .setPrettyPrinting()
                .create();
        return gson.fromJson(requestBody, Subtask.class);
    }

    private Epic epicFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        return new Gson().fromJson(requestBody, Epic.class);
    }

    public void get(String[] pathParts, HttpExchange exchange) throws IOException {
        switch (pathParts[1]) {
            case "tasks":
                if (pathParts.length == 2) {
                    String jsonList = new Gson().toJson(taskManager.getListAllTask());
                    writeResponse(exchange, jsonList, 200);
                } else {
                    int id = Integer.parseInt(pathParts[2]);
                    Task task = taskManager.getTaskById(id);
                    check(task, exchange);
                }
                break;
            case "subtasks":
                if (pathParts.length == 2) {
                    String jsonList = new Gson().toJson(taskManager.getListAllSubtask());
                    writeResponse(exchange, jsonList, 200);
                } else {
                    int id = Integer.parseInt(pathParts[2]);
                    Subtask task = taskManager.getSubtaskById(id);
                    check(task, exchange);
                }
                break;
            case "epics":
                if (pathParts.length == 2) {
                    String jsonList = new Gson().toJson(taskManager.getListAllEpic());
                    writeResponse(exchange, jsonList, 200);
                } else {
                    int id = Integer.parseInt(pathParts[2]);
                    Epic task = taskManager.getEpicById(id);
                    check(task, exchange);
                }
                break;
            case "history":
                String jsonList = new Gson().toJson(taskManager.getHistoryList());
                writeResponse(exchange, jsonList, 200);
                break;
            case "prioritized":
                String json = new Gson().toJson(taskManager.getPrioritizedTasks());
                writeResponse(exchange, json, 200);
                break;
            default:
                writeResponse(exchange, "Неверный тип задачи", 400);
                break;
        }
    }

    private void check(Task task, HttpExchange exchange) throws IOException {
        if (task != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            String taskJson = gson.toJson(task);
            writeResponse(exchange, taskJson, 200);
        } else {
            writeResponse(exchange, "Такой задачи нет", 404);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, responseString.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseString.getBytes());
        }
        exchange.close();
    }

    class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
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

    class DurationTypeAdapter extends TypeAdapter<Duration> {

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
