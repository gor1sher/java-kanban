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
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    Subtask subTasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
    }
    public int generateId(){
        return id++;
    }

    public void createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask){
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtack(subtask);
    }

    public  void removeTask(int id){
        tasks.remove(id);
    }

    public void removeEpic(int id){
        epics.remove(id);
    }

    public void removeSubtask(int id){
        for (Epic epic : epics.values()){
            for (Subtask subtask : epic.getSubtasks()) {
                if(subtask.getId() == id){
                    epic.removeSubtask(subtask);
                    subtasks.remove(subtask);
                    return;
                }
            }
        }
    }



    public Task getTaskForId(int id){
        return tasks.get(id);
    }

    public Epic getEpicForId(int id){
        return epics.get(id);
    }

    public Subtask getSubtaskForId(int id){
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
    }

    public void getListAllTask() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    public void getListAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        for (Epic epic : epicList) {
            System.out.println(epic);
        }
    }
    public void getListAllSubtask(){
        for (Epic epic : epics.values()) {
            System.out.println("Epic id:" + epic.getId() + " SubTasks:" + epic.getSubtasks());
        }
    }
    public void getListAllSubtaskForEpic(Epic specificEpic){
        Epic epic = epics.get(specificEpic.getId());
        System.out.println(epic.getSubtasks());
    }

    private void updateStatusEpic(Epic epic){
        List<Subtask> subtasks = epic.getSubtasks();
        Status firstStatus = subtasks.get(0).getStatus();
        if (firstStatus == Status.IN_PROGRESS){
            epic.setStatus(Status.IN_PROGRESS);
        } else if (firstStatus == Status.NEW) {
            extracted(epic, subtasks, Status.NEW);
        } else {
            extracted(epic, subtasks, Status.DONE);
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
