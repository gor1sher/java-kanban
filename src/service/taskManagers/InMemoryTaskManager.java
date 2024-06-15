package service.taskManagers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.exception.NotFoundException;
import service.taskManagers.exception.ValidationException;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 1;
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final InMemoryHistoryManager historyList;
    private TreeSet<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    public ArrayList<Task> getHistoryList() {
        return historyList.getHistory();
    }

    public void removeHistory(int id) {
        historyList.remove(id);
    }

    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyList = inMemoryHistoryManager;
    }

    private int generateId() {
        return id++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addPriorityTask(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic());
        epic.addSubtack(subtask.getId());
        updateStatusForEpic(epic);

        updateLocalDateTimeForEpic(epic);
        addPriorityTask(subtask);

        return subtask;
    }

    @Override
    public void removeTask(int id) {
        priorityTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtasks()) {
            epic.removeSubtask(subtask);
            subtasks.remove(subtask);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Integer epicId = subtask.getEpic();
        Epic epic = epics.get(epicId);
        epic.removeSubtask(subtask.getId());
        updateStatusForEpic(epic);

        priorityTasks.remove(subtask);
        subtasks.remove(id);
        updateLocalDateTimeForEpic(epic);
    }

    @Override
    public Task getTaskById(int id) {
        historyList.add(id, tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyList.add(id, epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyList.add(id, subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateTask(Task newTask) {
        Task task = tasks.get(newTask.getId());
        if (task == null)
            throw new NotFoundException("Epic id = " + task.getId());

        priorityTasks.remove(task.getId());
        addPriorityTask(task);
        tasks.put(task.getId(), newTask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        if (epic == null)
            throw new NotFoundException("Epic id = " + epic.getId());

        priorityTasks.remove(epic);
        addPriorityTask(epic);
        epics.put(epic.getId(), newEpic);
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getEpic());
        Subtask subtask = subtasks.get(newSubtask.getId());
        if (subtask == null)
            throw new NotFoundException("Epic id = " + subtask.getId());

        priorityTasks.remove(subtask);
        addPriorityTask(subtask);
        subtasks.put(subtask.getId(), newSubtask);
        updateStatusForEpic(epic);
    }

    @Override
    public List<Task> getListAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getListAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Integer> getSubtasksByEpic(Epic specificEpic) {
        Epic epic = epics.get(specificEpic.getId());
        return epic.getSubtasks();
    }

    public boolean checkPriorityTask(Task task){
        return priorityTasks.stream()
                .anyMatch(task1 -> task1.getEndTime().isBefore(task.getStartTime()) ||
                        task1.getStartTime().isAfter(task.getEndTime()));
    }

    public void addPriorityTask(Task task){
        if(checkPriorityTask(task))
            priorityTasks.add(task);
        else
            throw new ValidationException("Пересечение с задачей");
    }

    public TreeSet<Task> getPrioritizedTasks(){
        return priorityTasks;
    }

    private void updateLocalDateTimeForEpic(Epic epic){
        ArrayList<Integer> subtasksList = epic.getSubtasks();

        epic.setDuration(subtasksList.stream()
                .map(a -> subtasks.get(a).getDuration())
                .reduce((a, b) -> a.plus(b)));
    }

    private void updateStatusForEpic(Epic epic) {
        ArrayList<Integer> subtasksList = epic.getSubtasks();
        int countDone = 0;
        int countNew = 0;
        if (epic.getSubtasks() != null) {
            for (Integer subtaskId : subtasksList) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                } else if (subtask.getStatus() == Status.DONE) {
                    countDone++;
                } else {
                    countNew++;
                }
            }

            if (countNew == subtasksList.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == subtasksList.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}

