import com.google.gson.Gson;
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
                LocalDateTime.of(2023, 3, 28, 11, 30, 0));
        epic.setId(100);

        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 23, 12, 30, 0));

        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                        LocalDateTime.of(2023, 3, 29, 13, 30, 0));

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createTask(epic);
        fileBackedTaskManager.createTask(subtask);

        System.out.println(epic.getDuration());

        Gson gson = new Gson();

    }
}
