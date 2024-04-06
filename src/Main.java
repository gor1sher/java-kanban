import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createTask(new Task("asd", "asd", Status.NEW));
        manager.getListAllTask();
        Epic epic2 = new Epic("qwe", "qwe");
        manager.updateStatusEpic(epic2);

        Epic epic = new Epic("аза", "гусейнов", Status.IN_PROGRESS);
        manager.createEpic(epic);
        manager.getListAllEpic();

        manager.createSubtask(new Subtask("gor", "gors", Status.IN_PROGRESS, epic));
        manager.createSubtask(new Subtask("gor1", "gors1", Status.IN_PROGRESS, epic));


        Epic epic1 = new Epic("аза1", "гусейнов", Status.IN_PROGRESS);
        manager.createEpic(epic1);
        manager.getListAllEpic();

        manager.createSubtask(new Subtask("gor", "gors", Status.IN_PROGRESS, epic1));
        manager.createSubtask(new Subtask("gor1", "gors1", Status.IN_PROGRESS, epic1));
        manager.getSubtasksByEpic(epic1);
        manager.getListAllSubtask();

    }
}
