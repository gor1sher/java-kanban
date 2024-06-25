package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.saveTasks.FileBackedTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.inMemoryTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void readFail_haveToGetEverythingOut() throws IOException {
        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 15, 11, 30, 0));
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22), LocalDateTime.of(2021, 3, 15, 12, 23, 0));
        inMemoryTaskManager.createSubtask(subtask);

        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 17, 12, 30, 0));
        inMemoryTaskManager.createTask(task);

        FileBackedTaskManager loadedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());

        assertEquals(epic, loadedTaskManager.getEpicById(epic.getId()));
        assertEquals(subtask, loadedTaskManager.getSubtaskById(subtask.getId()));
        assertEquals(task, loadedTaskManager.getTaskById(task.getId()));
    }
}