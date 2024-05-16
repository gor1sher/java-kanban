import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void getHistoryList_InMemoryHistoryManager() {
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
        System.out.println(list);
    }

    @Test
    void remove_removeView_InMemoryHistoryManager() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        Task task = new Task("аза2", "куув", Status.NEW);
        task = inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getEpicById(epic.getId());
        inMemoryTaskManager.getTaskById(task.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();

        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));

        inMemoryTaskManager.removeHistory(epic.getId());

        ArrayList<Task> list1 = inMemoryTaskManager.getHistoryList();

        assertFalse(list1.contains(epic));

    }

    @Test
    void add_addingElements_InMemoryHistoryManager() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        Task task = new Task("аза2", "куув", Status.NEW);
        task = inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getEpicById(epic.getId());
        inMemoryTaskManager.getTaskById(task.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();

        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));

        Subtask subtask = new Subtask("gor1", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask = inMemoryTaskManager.createSubtask(subtask);

        inMemoryTaskManager.getSubtaskById(subtask.getId());

        ArrayList<Task> list1 = inMemoryTaskManager.getHistoryList();

        assertTrue(list1.contains(subtask));
    }
}
