import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createTask(new Task("asd", "asd", Status.NEW));
        manager.getListAllTask();


        Epic epic = new Epic("аза", "гусейнов", Status.IN_PROGRESS);
        manager.createEpic(epic);
        manager.getListAllEpic();

        manager.createSubtask(new Subtask("gor", "gors", Status.IN_PROGRESS, epic.getId()));
        manager.createSubtask(new Subtask("gor1", "gors1", Status.IN_PROGRESS, epic.getId()));


        Epic epic1 = new Epic("аза1", "гусейнов", Status.IN_PROGRESS);
        manager.createEpic(epic1);
        manager.getListAllEpic();

        manager.createSubtask(new Subtask("gor", "gors", Status.IN_PROGRESS, epic1.getId()));
        manager.createSubtask(new Subtask("gor1", "gors1", Status.IN_PROGRESS, epic1.getId()));
        manager.getSubtasksByEpic(epic1);
        manager.getListAllSubtask();
        manager.getHistory();

    }
}
