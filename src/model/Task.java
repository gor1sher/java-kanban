package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class Task {
    private String name;
    private String description;
    private Status status;
    private Integer id;

    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task(String name, String description, Status status, Duration duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public Duration getDuration(){
        return duration;
    }

    public void setDuration(Optional<Duration> duration){
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", description=" + description +
                ", status=" + status +
                ", id=" + id + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }
}
