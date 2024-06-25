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
    Task task;
    Task task1;
    Task task3;
    Task task4;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

        task3 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 30, 12, 30, 0));

        task4 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 30, 12, 30, 0));

        task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        task1 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 18, 12, 30, 0));

        epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic);

        subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 16, 12, 30, 0));
        inMemoryTaskManager.createSubtask(subtask);

        inMemoryTaskManager.createTask(task);
    }

    @Test
    public void create_mustCreateTasksCorrectly() {
        Epic saveEpic = epic;

        assertEquals(epic.getDescription(), saveEpic.getDescription());
        assertEquals(epic.getName(), saveEpic.getName());
        assertEquals(epic.getStatus(), saveEpic.getStatus());

        Subtask saveSubtask = subtask;

        assertEquals(subtask.getDescription(), saveSubtask.getDescription());
        assertEquals(subtask.getName(), saveSubtask.getName());
        assertEquals(subtask.getStatus(), saveSubtask.getStatus());
        assertEquals(subtask.getEpic(), saveSubtask.getEpic());

        Task saveTask = task;

        assertEquals(task.getDescription(), saveTask.getDescription());
        assertEquals(task.getName(), saveTask.getName());
        assertEquals(task.getStatus(), saveTask.getStatus());
    }

    @Test
    public void getById_shouldHaveTheSameId() {
        assertEquals(epic, inMemoryTaskManager.getEpicById(epic.getId()));
        assertEquals(subtask, inMemoryTaskManager.getSubtaskById(subtask.getId()));
        assertEquals(task, inMemoryTaskManager.getTaskById(task.getId()));
    }

    @Test
    public void getHistoryList_returnUpdateHistoryList() {
        inMemoryTaskManager.getEpicById(epic.getId());
        inMemoryTaskManager.getSubtaskById(subtask.getId());
        inMemoryTaskManager.getTaskById(task.getId());

        ArrayList<Task> list = inMemoryTaskManager.getHistoryList();
        assertTrue(list.contains(subtask));
        assertTrue(list.contains(task));
        assertTrue(list.contains(epic));
    }

    @Test
    public void updateStatusForEpic_statusInProgress() {
        Epic epic10 = epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic10);

        Subtask subtask10 = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2021, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(subtask10);

        subtask10.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask10);
        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicById(epic10.getId()).getStatus());
    }

    @Test
    public void updateStatusForEpic_statusDone() {
        Epic epic10 = epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2022, 3, 15, 12, 30, 0));

        inMemoryTaskManager.createEpic(epic10);

        Subtask subtask10 = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId(),
                Duration.ofMinutes(22),
                LocalDateTime.of(2022, 3, 16, 12, 30, 0));

        inMemoryTaskManager.createSubtask(subtask10);

        subtask10.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask10);
        assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(epic10.getId()).getStatus());
    }

    @Test
    public void updateStatusForEpic_statusNew() {
        inMemoryTaskManager.createEpic(epic);
        subtask.setStatus(Status.NEW);
        inMemoryTaskManager.updateSubtask(subtask);
        assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void updateTimeEpic_testException() {
        assertThrows(ValidationException.class, () -> {
            inMemoryTaskManager.createTask(task3);
            inMemoryTaskManager.createTask(task4);
        }, "Пересечение по времени задач должно приводить к исключению");
    }
}
