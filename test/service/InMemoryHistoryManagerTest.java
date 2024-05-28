package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManagers.InMemoryHistoryManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    void getHistory_returnInCorrectOrder() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        Task task = new Task("аза2", "куув", Status.NEW);

        inMemoryHistoryManager.add(1, epic);
        inMemoryHistoryManager.add(2, task);

        ArrayList<Task> list = inMemoryHistoryManager.getHistory();
        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));
    }

    @Test
    void remove_removeFromMapAndlinkedList() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        epic.setId(12);
        Task task = new Task("аза2", "куув", Status.NEW);
        task.setId(15);

        inMemoryHistoryManager.add(epic.getId(), epic);
        inMemoryHistoryManager.add(task.getId(), task);

        ArrayList<Task> list = inMemoryHistoryManager.getHistory();

        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));

        inMemoryHistoryManager.remove(epic.getId());

        ArrayList<Task> list1 = inMemoryHistoryManager.getHistory();

        assertFalse(list1.contains(epic));
    }

    @Test
    void add_addWithoutDoublicatsToEnd() {
        Epic epic = new Epic("аза1", "куув", Status.IN_PROGRESS);
        Task task = new Task("аза2", "куув", Status.NEW);

        inMemoryHistoryManager.add(1, epic);
        inMemoryHistoryManager.add(2, task);

        ArrayList<Task> list = inMemoryHistoryManager.getHistory();

        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));

        inMemoryHistoryManager.add(1, epic);

        ArrayList<Task> list1 = inMemoryHistoryManager.getHistory();

        assertEquals(2, list1.size());
    }
}
