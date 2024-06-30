package service.taskManagers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    public Task createTask(Task task);

    public Epic createEpic(Epic epic);

    public Subtask createSubtask(Subtask subtask);

    public void removeTask(int id);

    public void removeEpic(int id);

    public void removeSubtask(int id);

    public Task getTaskById(int id);

    public Epic getEpicById(int id);

    public Subtask getSubtaskById(int id);

    public void updateTask(Task newTask);

    public void updateEpic(Epic newEpic);

    public void updateSubtask(Subtask newSubtask);

    public List<Task> getListAllTask();

    public List<Epic> getListAllEpic();

    public List<Subtask> getListAllSubtask();

    public ArrayList<Integer> getSubtasksByEpic(Epic specificEpic);

    public ArrayList<Task> getHistoryList();

    public TreeSet<Task> getPrioritizedTasks();
}
