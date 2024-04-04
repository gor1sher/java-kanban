package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ArrayList<Subtask> getSubtasks() {
        return (ArrayList<Subtask>) subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtack(Subtask subtask){
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask){
        subtasks.remove(subtask);
    }

    public void updateSubTask(Subtask newSubTask) {
        Subtask subtask = subtasks.get(newSubTask.getId());
        subtasks.set(subtask.getId(), newSubTask);
    }

}
