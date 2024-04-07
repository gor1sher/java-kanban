package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private static int id = 0;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
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
        Epic epic = epics.get(subtask.getEpic());
        epic.addSubtack(subtask.getId());
        updateStatusForEpic(epic);
        return subtask;
    }

    public void removeTask(int id){
        tasks.remove(id);
    }

    public void removeEpic(int id){
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtasks()){
            epic.removeSubtask(subtask);
            subtasks.remove(subtask);
        }
        epics.remove(id);
    }

    public void removeSubtask(int id){
        Subtask subtask = subtasks.get(id);
        Integer epicId = subtask.getEpic();
        Epic epic = epics.get(epicId);
        epic.removeSubtask(subtask.getId());
        updateStatusForEpic(epic);

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
        Epic epic = epics.get(newSubtask.getEpic());
        Subtask subtask = subtasks.get(newSubtask.getId());
        subtasks.put(subtask.getId(), newSubtask);
        updateStatusForEpic(epic);
    }

    public List<Task> getListAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }
    public List<Subtask> getListAllSubtask(){
        return new ArrayList<>(subtasks.values());
    }
    public ArrayList<Integer> getSubtasksByEpic(Epic specificEpic){
        Epic epic = epics.get(specificEpic.getId());
        return epic.getSubtasks();
    }

    private void updateStatusForEpic(Epic epic){
        ArrayList<Integer> subtasksList = epic.getSubtasks();
        int countDone = 0;
        int countNew = 0;
        if(epic.getSubtasks() != null) {
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
