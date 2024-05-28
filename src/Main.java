import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManagers.FileBackedTaskManager;
import service.HistoryManagers.InMemoryHistoryManager;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager());
        Epic epic4 = new Epic("дом", "купить дом", Status.NEW);
        fileBackedTaskManager.createEpic(epic4);
        Subtask subtask5 = new Subtask("посадить дерево", "березка", Status.DONE, epic4.getId());
        fileBackedTaskManager.createSubtask(subtask5);
        Task tas7k = new Task("аза2", "куув", Status.NEW);
        fileBackedTaskManager.createTask(tas7k);

        fileBackedTaskManager.removeTask(tas7k.getId());

        Epic epic1 = new Epic("дом", "купить дом", Status.NEW);
        fileBackedTaskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("посадить дерево", "березка", Status.DONE, epic1.getId());
        fileBackedTaskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("посадить дерево", "березка", Status.DONE, epic1.getId());
        fileBackedTaskManager.createSubtask(subtask2);
    }
}
