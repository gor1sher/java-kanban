import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        fileBackedTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.DONE, epic.getId());
        fileBackedTaskManager.createSubtask(subtask);
        Task task = new Task("аза2", "куув", Status.NEW);
        fileBackedTaskManager.createTask(task);

        fileBackedTaskManager.readFail();

        HashMap<Integer, Task> mapa = fileBackedTaskManager.getReadTasks();
        for (Map.Entry<Integer, Task> entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getName() + " " + entry.getValue().getDescription() + " " +
                    entry.getValue().getStatus());
        }
    }
}
