import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Epic epic = new Epic("дом", "3 шага для постройки дома", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("посадить дерево", "березка", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("выростить сына", "иван", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("построить дом", "двухэтажный", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask3);

        Epic epic1 = new Epic("машина", "купить машину", Status.NEW);
        inMemoryTaskManager.createEpic(epic1);

        inMemoryTaskManager.getSubtaskById(subtask1.getId());
        System.out.println(inMemoryTaskManager.getHistoryList());

        inMemoryTaskManager.getEpicById(epic.getId());
        System.out.println(inMemoryTaskManager.getHistoryList());

        inMemoryTaskManager.getSubtaskById(subtask3.getId());
        System.out.println(inMemoryTaskManager.getHistoryList());

        inMemoryTaskManager.getEpicById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistoryList());

        inMemoryTaskManager.getSubtaskById(subtask2.getId());
        System.out.println(inMemoryTaskManager.getHistoryList());

    }
}
