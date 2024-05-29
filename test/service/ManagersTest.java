package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskManagers.InMemoryTaskManager;
import service.taskManagers.Managers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void getDefault_сreationTwoObjects() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(inMemoryTaskManager);

        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        Task task = new Task("аза", "куув", Status.NEW);
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
