package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskManagers.InMemoryTaskManager;
import service.taskManagers.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void getDefault_сreationTwoObjects() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(inMemoryTaskManager);

        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        epic = inMemoryTaskManager.createEpic(epic);

        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 16, 12, 30, 0));

        subtask = inMemoryTaskManager.createSubtask(subtask);
        task = inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getSubtaskById(subtask.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();

        assertTrue(list.contains(subtask));
        assertEquals(epic, inMemoryTaskManager.getEpicById(epic.getId()));
        assertEquals(subtask, inMemoryTaskManager.getSubtaskById(subtask.getId()));
        assertEquals(task, inMemoryTaskManager.getTaskById(task.getId()));
    }
}
