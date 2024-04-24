package tests;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    @Test
    void addSubtask_shouldNotAddYourself_Epic() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.addSubtack(epic.getId(), epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }

    @Test
    void addSubtask_shouldNotAddYourself_Subtask(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        int subtaskCount = epic.getSubtasks().size();
        epic.addSubtack(subtask.getId(), subtask.getId());
        assertEquals(subtaskCount, epic.getSubtasks().size());
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
        epic1.setId(20);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask.setId(40);
        Subtask subtask1 = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask1.setId(40);
        assertTrue(epic.equals(epic1));
        assertTrue(subtask.equals(subtask1));
    }

    @Test
    void setId_eachOtherIfTheirIdIsEqual_Subtask(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask.setId(40);
        Subtask subtask1 = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask1.setId(40);
        assertTrue(subtask.equals(subtask1));
    }

    @Test
    void getTaskById_mustBeEqualId(){
        Task task = new Task("аза", "куув", Status.NEW);
        task.setId(15);
        assertTrue(task.equals(inMemoryTaskManager.getTaskById(15)));
    }

    @Test
    void getEpicById_mustBeEqualId(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(20);
        assertTrue(epic.equals(inMemoryTaskManager.getEpicById(20)));
    }

    @Test
    void getSubtaskById_getEpicById_mustBeEqualId(){
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        Subtask subtask = new Subtask("gor", "qwert", Status.IN_PROGRESS, epic.getId());
        subtask.setId(40);
        assertTrue(subtask.equals(inMemoryTaskManager.getSubtaskById(40)));
    }
}