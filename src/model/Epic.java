package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super.setName(name);
        this.setDescription(description);
    }

    public ArrayList<Integer> getSubtasks() {
        return (ArrayList<Integer>) subtasks;
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtack(Integer subtask){
        subtasks.add(subtask);
    }

    public void removeSubtask(Integer subtask){
        subtasks.remove(subtask);
    }
}
