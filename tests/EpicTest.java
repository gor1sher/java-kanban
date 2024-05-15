import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void addSubtask_shouldNotAddYourself_Epic() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        inMemoryTaskManager.createEpic(epic);
        epic.addSubtack(epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }

    @Test
    void addSubtask_shouldNotAddYourself_Subtask(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);
        subtask.setEpic(subtask.getId());
        assertNotEquals(subtask.getId(), subtask.getEpic());
    }

    @Test
    void setId_eachOtherIfTheirIdIsEqual_Task(){
        Task task = new Task("аза", "куув", Status.NEW);
        task.setId(15);
        Task task1 = new Task("аза", "куув", Status.NEW);
        task1.setId(15);
        assertTrue(task.equals(task1));
    }

    @Test
    void setId_eachOtherIfTheirIdIsEqual_Epic(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(20);
        Epic epic1 = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic1.setId(20);;
        assertEquals(epic, epic1);
    }

    @Test
    void getTaskById_mustBeEqualId(){
        Task task = new Task("аза", "куув", Status.NEW);
        task = inMemoryTaskManager.createTask(task);
        assertEquals(task, inMemoryTaskManager.getTaskById(task.getId()));
    }

    @Test
    void getEpicById_mustBeEqualId(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic = inMemoryTaskManager.createEpic(epic);
        assertEquals(epic, inMemoryTaskManager.getEpicById(epic.getId()));
    }

    @Test
    void testInMemoryTaskManager() {
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
    void testInMemoryTaskManagerDontChange() {
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
    void testMangers_getDefault() {
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
    void getHistoryList_inMemoryHistoryManager(){
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
} 