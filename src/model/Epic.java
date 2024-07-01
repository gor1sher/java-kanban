package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private List<Integer> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, Duration duration,
                LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status) {
        super.setName(name);
        super.setStatus(status);
        this.setDescription(description);
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

    public void addSubtack(Integer subtask) {
        if (subtask != null) {
            if (!subtask.equals(super.getId())) {
                subtasks.add(subtask);
            }
        }
    }

    public void setDuration(Optional<Duration> duration) {
        if (duration.isPresent()) {
            super.duration = duration.get();
        }
    }

    public void setStartTime(Optional<LocalDateTime> startTime) {
        if (startTime.isPresent()) {
            super.startTime = startTime.get();
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void removeSubtask(Integer subtask) {
        subtasks.remove(subtask);
    }
}
