import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.taskManagers.saveTasks.FileBackedTaskManager;
import service.historyManagers.InMemoryHistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());

        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, 100,
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 16, 12, 30, 0));

        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createTask(epic);
        fileBackedTaskManager.createTask(subtask);
    }
}
