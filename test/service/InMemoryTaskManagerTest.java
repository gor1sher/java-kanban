package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void create_mustCreateTasksCorrectly() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        Epic saveEpic = inMemoryTaskManager.createEpic(epic);

        assertEquals(epic.getDescription(), saveEpic.getDescription());
        assertEquals(epic.getName(), saveEpic.getName());
        assertEquals(epic.getStatus(), saveEpic.getStatus());

        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        Subtask saveSubtask = inMemoryTaskManager.createSubtask(subtask);

        assertEquals(subtask.getDescription(), saveSubtask.getDescription());
        assertEquals(subtask.getName(), saveSubtask.getName());
        assertEquals(subtask.getStatus(), saveSubtask.getStatus());
        assertEquals(subtask.getEpic(), saveSubtask.getEpic());

        Task task = new Task("аза", "куув", Status.NEW);
        Task saveTask = inMemoryTaskManager.createTask(task);

        assertEquals(task.getDescription(), saveTask.getDescription());
        assertEquals(task.getName(), saveTask.getName());
        assertEquals(task.getStatus(), saveTask.getStatus());
    }

    @Test
    void getById_shouldHaveTheSameId() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        Task task = new Task("аза", "куув", Status.NEW);
        subtask = inMemoryTaskManager.createSubtask(subtask);
        task = inMemoryTaskManager.createTask(task);

        assertEquals(epic, inMemoryTaskManager.getEpicById(epic.getId()));
        assertEquals(subtask, inMemoryTaskManager.getSubtaskById(subtask.getId()));
        assertEquals(task, inMemoryTaskManager.getTaskById(task.getId()));
    }

    @Test
    void getHistoryList_returnUpdateHistoryList() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("gor1", "qwert", Status.IN_PROGRESS, epic.getId());
        Task task = new Task("аза2", "куув", Status.NEW);
        subtask = inMemoryTaskManager.createSubtask(subtask);
        task = inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getEpicById(epic.getId());
        inMemoryTaskManager.getSubtaskById(subtask.getId());
        inMemoryTaskManager.getTaskById(task.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();
        assertTrue(list.contains(subtask));
        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));
    }

    @Test
    void updateStatusForEpic_statusInProgress() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void updateStatusForEpic_statusDone() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.DONE, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void updateStatusForEpic_statusNew() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.NEW, epic.getStatus());
    }
}
