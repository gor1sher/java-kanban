package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManagers.InMemoryHistoryManager;
import service.TaskManagers.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {

    @Test
    void readFail_haveToGetEverythingOut() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        fileBackedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.DONE, epic.getId());
        fileBackedTaskManager.createSubtask(subtask);
        Task task = new Task("аза2", "куув", Status.NEW);
        fileBackedTaskManager.createTask(task);
    }
}