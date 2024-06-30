package service.httpTaskManagers;

import com.google.gson.Gson;
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
            writeResponse(exchange, "Неизвестная ошибка", 500);
        }
    }


    public void delete(String[] pathParts, HttpExchange exchange) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[3].trim());
            switch (pathParts[2]) {
                case "tasks":
                    Task task = taskManager.getTaskById(id);
                    if (task == null) {
                        writeResponse(exchange, "Задача не найдена", 404);
                        return;
                    }
                    taskManager.removeTask(task.getId());
                    writeResponse(exchange, "Задача успешно удалена", 200);
                    break;
                case "subtasks":
                    Subtask subtask = taskManager.getSubtaskById(id);
                    if (subtask == null) {
                        writeResponse(exchange, "Подзадача не найдена", 404);
                        return;
                    }
                    taskManager.removeSubtask(subtask.getId());
                    writeResponse(exchange, "Подзадача успешно удалена", 200);
                    break;
                case "epics":
                    Epic epic = taskManager.getEpicById(id);
                    if (epic == null) {
                        writeResponse(exchange, "Эпик не найден", 404);
                        return;
                    }
                    taskManager.removeEpic(epic.getId());
                    writeResponse(exchange, "Эпик успешно удален", 200);
                    break;
                default:
                    writeResponse(exchange, "Неверный тип задачи", 400);
                    break;
            }
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Неверный формат идентификатора", 400);
        } catch (IOException e) {
            writeResponse(exchange, "Ошибка ввода-вывода", 500);
        }
    }


    public void post(String[] pathParts, HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();

        switch (pathParts[2]) {
            case "tasks":
                Task task = taskFromJson(inputStream);

                try {
                    if (task.getId() >= 0) {
                        taskManager.updateTask(task);
                    } else {
                        taskManager.createTask(task);
                    }
                    writeResponse(exchange, "Задача успешно создана", 201);
                } catch (ValidationException e) {
                    writeResponse(exchange, "Задача пересекается с существующими", 406);
                }
                break;
            case "subtasks":
                Subtask subtask = subtaskFromJson(inputStream);

                try {
                    if (subtask.getId() >= 0) {
                        taskManager.updateSubtask(subtask);
                    } else {
                        taskManager.createSubtask(subtask);
                    }
                    writeResponse(exchange, "Задача успешно создана", 201);
                } catch (ValidationException e) {
                    writeResponse(exchange, "Задача пересекается с существующими", 406);
                }
                break;
            case "epics":
                Epic epic = epicFromJson(inputStream);

                taskManager.createEpic(epic);
                writeResponse(exchange, "Задача успешно создана", 201);
                break;
        }
    }

    private String getString(InputStream inputStream) throws IOException {
        byte[] requestBody = inputStream.readAllBytes();
        return new String(requestBody, StandardCharsets.UTF_8);
    }

    private Task taskFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        return new Gson().fromJson(requestBody, Task.class);
    }

    private Subtask subtaskFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        return new Gson().fromJson(requestBody, Subtask.class);
    }

    private Epic epicFromJson(InputStream inputStream) throws IOException {
        String requestBody = getString(inputStream);
        return new Gson().fromJson(requestBody, Epic.class);
    }

    public void get(String[] pathParts, HttpExchange exchange) throws IOException {
        switch (pathParts[2]) {
            case "tasks":
                if (pathParts.length == 3) {
                    int id = Integer.parseInt(pathParts[3]);
                    Task task = taskManager.getTaskById(id);
                    check(task, exchange);
                } else {
                    String jsonList = new Gson().toJson(taskManager.getListAllTask());
                    writeResponse(exchange, jsonList, 200);
                }
            case "subtasks":
                if (pathParts.length == 3) {
                    int id = Integer.parseInt(pathParts[3]);
                    Subtask task = taskManager.getSubtaskById(id);
                    check(task, exchange);
                } else {
                    String jsonList = new Gson().toJson(taskManager.getListAllSubtask());
                    writeResponse(exchange, jsonList, 200);
                }
            case "epics":
                if (pathParts.length == 3) {
                    int id = Integer.parseInt(pathParts[3]);
                    Epic task = taskManager.getEpicById(id);
                    check(task, exchange);
                } else {
                    String jsonList = new Gson().toJson(taskManager.getListAllEpic());
                    writeResponse(exchange, jsonList, 200);
                }
            case "history":
                String jsonList = new Gson().toJson(taskManager.getHistoryList());
                writeResponse(exchange, jsonList, 200);
            case "prioritized":
                String json = new Gson().toJson(taskManager.getPrioritizedTasks());
                writeResponse(exchange, json, 200);
            default:
                writeResponse(exchange, "Неверный тип задачи", 400);
                break;
        }
    }

    private void check(Task task, HttpExchange exchange) throws IOException {
        if (task != null) {
            String taskJson = new Gson().toJson(task);
            writeResponse(exchange, taskJson, 200);
        } else {
            writeResponse(exchange, "Такой задачи нет", 404);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes());
        }
        exchange.close();
    }
}
