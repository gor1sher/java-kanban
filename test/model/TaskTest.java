package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    void setId_eachOtherIfTheirIdIsEqual() {
        Task task = new Task("аза", "куув", Status.NEW);
        task.setId(15);
        Task task1 = new Task("аза", "куув", Status.NEW);
        task1.setId(15);
        assertTrue(task.equals(task1));
    }
}
