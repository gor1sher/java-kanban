package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.saveTasks.FileBackedTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest {

    @Test
    void readFail_haveToGetEverythingOut() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        fileBackedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 16, 12, 30, 0));

        fileBackedTaskManager.createSubtask(subtask);
        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        fileBackedTaskManager.createTask(task);
    }
}