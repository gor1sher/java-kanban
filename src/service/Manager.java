package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    static int id = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
    }
    private int generateId(){
        return id++;
    }

    public Task createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;

    }

    public Epic createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask){
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtack(subtask);
        updateStatusEpic(subtask.getEpic());
        return subtask;
    }

    public void removeTask(int id){
        tasks.remove(id);
    }

    public void removeEpic(int id){
        for (Subtask subtask: epics.get(id).getSubtasks()){
            subtask.getEpic().removeSubtask(subtask);
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    public void removeSubtask(int id){
        Subtask subtask = subtasks.get(id);
        Epic epic = subtask.getEpic();

        epic.removeSubtask(subtask);
        updateStatusEpic(epic);

        subtasks.remove(id);
    }

    public Task getTaskById(int id){
        return tasks.get(id);
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }

    public void updateTask(Task newTask){
        Task task = tasks.get(newTask.getId());
        tasks.put(task.getId(), newTask);
    }

    public void updateEpic(Epic newEpic){
        Epic epic = epics.get(newEpic.getId());
        epics.put(epic.getId(), newEpic);
    }

    public void updateSubtask(Subtask newSubtask){
        Epic epic = epics.get(newSubtask.getEpic().getId());
        epic.updateSubTask(newSubtask);
        updateStatusEpic(epic);
    }

    public List<Task> getListAllTask() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());
        return taskList;
    }

    public List<Epic> getListAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }
    public List<Subtask> getListAllSubtask(){
        ArrayList<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        return subtaskList;
    }
    public ArrayList<Subtask> getSubtasksByEpic(Epic specificEpic){
        Epic epic = epics.get(specificEpic.getId());
        return epic.getSubtasks();
    }

    public void updateStatusEpic(Epic epic){
        List<Subtask> subtasks = epic.getSubtasks();
        if (epic.getSubtasks() != null) {
            Status firstStatus = subtasks.get(0).getStatus();
            if (firstStatus == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (firstStatus == Status.NEW) {
                extracted(epic, subtasks, Status.NEW);
            } else {
                extracted(epic, subtasks, Status.DONE);
            }
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    private static void extracted(Epic epic, List<Subtask> subtasks, Status status) {
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != status) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(status);
    }
}
