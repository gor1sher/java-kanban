package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epic;

    public Subtask(String name, String description, Status status, Integer epic, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epic = epic;
    }

    public Integer getEpic() {
        return epic;
    }

    public void setEpic(Integer epic) {
        if (epic != null) {
            if (!epic.equals(super.getId())) {
                this.epic = epic;
            }
        }
    }

}
