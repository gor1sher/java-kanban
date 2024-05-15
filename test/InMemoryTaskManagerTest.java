import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void getDefault_сreationTwoObjects_InMemoryTaskManager() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(inMemoryTaskManager);

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
    void create_dontChange_InMemoryTaskManager() {
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
    void getById_shouldHaveTheSameId_InMemoryTaskManager() {
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
}
