package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.InMemoryTaskManager;
import service.taskManagers.TaskManager;
import service.taskManagers.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    TaskManager inMemoryTaskManager;


    @BeforeEach
    public void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    public void createEpic_mustCreateEpicCorrectly() {
        Epic newEpic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(newEpic);

        Epic saveEpic = inMemoryTaskManager.getEpicById(newEpic.getId());

        assertEquals(newEpic.getDescription(), saveEpic.getDescription());
        assertEquals(newEpic.getName(), saveEpic.getName());
        assertEquals(newEpic.getStatus(), saveEpic.getStatus());
    }

    @Test
    public void createSubtask_mustCreateSubtaskCorrectly() {
        Epic newEpic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(newEpic);

        Subtask newSubtask = new Subtask("посадить дерево", "березка", Status.NEW, newEpic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(newSubtask);

        Subtask saveSubtask = inMemoryTaskManager.getSubtaskById(newSubtask.getId());

        assertEquals(newSubtask.getDescription(), saveSubtask.getDescription());
        assertEquals(newSubtask.getName(), saveSubtask.getName());
        assertEquals(newSubtask.getStatus(), saveSubtask.getStatus());
        assertEquals(newSubtask.getEpic(), saveSubtask.getEpic());
    }

    @Test
    public void createTask_mustCreateTaskCorrectly() {
        Task newTask = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 17, 12, 30, 0));

        inMemoryTaskManager.createTask(newTask);

        Task saveTask = inMemoryTaskManager.getTaskById(newTask.getId());

        assertEquals(newTask.getDescription(), saveTask.getDescription());
        assertEquals(newTask.getName(), saveTask.getName());
        assertEquals(newTask.getStatus(), saveTask.getStatus());
    }

    @Test
    public void getById_shouldHaveTheSameId() {
        Task task0 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2016, 3, 17, 12, 30, 0));

        Epic epic0 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2016, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic0);

        Subtask subtask0 = new Subtask("посадить дерево", "березка", Status.NEW, epic0.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2016, 3, 16, 12, 30, 0));
        inMemoryTaskManager.createSubtask(subtask0);

        inMemoryTaskManager.createTask(task0);

        assertEquals(epic0, inMemoryTaskManager.getEpicById(epic0.getId()));
        assertEquals(subtask0, inMemoryTaskManager.getSubtaskById(subtask0.getId()));
        assertEquals(task0, inMemoryTaskManager.getTaskById(task0.getId()));
    }

    @Test
    public void getHistoryList_returnUpdateHistoryList() {
        Task task01 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2015, 3, 17, 12, 30, 0));

        Epic epic01 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2015, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic01);

        Subtask subtask01 = new Subtask("посадить дерево", "березка", Status.NEW, epic01.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2015, 3, 16, 12, 30, 0));
        inMemoryTaskManager.createSubtask(subtask01);

        inMemoryTaskManager.createTask(task01);

        inMemoryTaskManager.getEpicById(epic01.getId());
        inMemoryTaskManager.getSubtaskById(subtask01.getId());
        inMemoryTaskManager.getTaskById(task01.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();
        assertTrue(list.contains(subtask01));
        assertTrue(list.contains(task01));
        assertTrue(list.contains(epic01));
    }

    @Test
    public void updateStatusForEpic_statusInProgress() {
        Epic epic10 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic10);

        Subtask subtask10 = new Subtask("посадить дерево", "березка", Status.NEW, epic10.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(subtask10);

        subtask10.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask10);
        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicById(epic10.getId()).getStatus());
    }

    @Test
    public void updateStatusForEpic_statusDone() {
        Epic epic10 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2022, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic10);

        Subtask subtask10 = new Subtask("посадить дерево", "березка", Status.NEW, epic10.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2022, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(subtask10);

        subtask10.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask10);
        assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(epic10.getId()).getStatus());
    }

    @Test
    public void updateStatusForEpic_statusNew() {
        Epic epic12 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic12);

        Subtask subtask12 = new Subtask("посадить дерево", "березка", Status.NEW, epic12.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2019, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(subtask12);

        subtask12.setStatus(Status.NEW);
        inMemoryTaskManager.updateSubtask(subtask12);
        assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(epic12.getId()).getStatus());
    }

    @Test
    public void updateTimeEpic_testException() {
        assertThrows(ValidationException.class, () -> {
            Task task3 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                    LocalDateTime.of(2023, 3, 30, 12, 30, 0));

            Task task4 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                    LocalDateTime.of(2023, 3, 30, 12, 30, 0));

            inMemoryTaskManager.createTask(task3);
            inMemoryTaskManager.createTask(task4);
        }, "Пересечение по времени задач должно приводить к исключению");
    }
}
