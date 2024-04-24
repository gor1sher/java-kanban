package tests;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    void testAddSubtask() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.addSubtack(epic.getId(), epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }

    @Test
    void testEqualForIdTask(){
        Task task = new Task("аза", "куув", Status.NEW);
        task.setId(15);
        Task task1 = new Task("аза", "куув", Status.NEW);
        task1.setId(15);
        assertTrue(task.equals(task1));
    }

    @Test
    void testEqualForIdEpicSubTask(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(20);
        Epic epic1 = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic1.setId(20);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask.setId(40);
        Subtask subtask1 = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask1.setId(40);
        assertTrue(epic.equals(epic1));
        assertTrue(subtask.equals(subtask1));
    }

    @Test
    void testInMemoryTaskManagerAddDifferentTypesOfTasks(){
        Task task = new Task("аза", "куув", Status.NEW);
        task.setId(15);
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(20);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask.setId(40);
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        assertTrue(task.equals(inMemoryTaskManager.getTaskById(15)));
        assertTrue(epic.equals(inMemoryTaskManager.getEpicById(20)));
        assertTrue(subtask.equals(inMemoryTaskManager.getSubtaskById(40)));
    }

}