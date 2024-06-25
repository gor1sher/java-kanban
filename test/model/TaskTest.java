package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    void setId_eachOtherIfTheirIdIsEqual() {
        Task task = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        task.setId(15);
        Task task1 = new Task("аза2", "куув", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 17, 12, 30, 0));

        task1.setId(15);
        assertEquals(task, task1);
    }
}
