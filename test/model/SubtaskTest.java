package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubtaskTest {

    @Test
    void setId_eachOtherIfTheirIdIsEqual() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(12);
        Subtask subtask = new Subtask("аза", "куув", Status.NEW, epic.getId());
        subtask.setId(15);
        Subtask subtask1 = new Subtask("аза", "куув", Status.NEW, epic.getId());
        subtask1.setId(15);
        assertTrue(subtask.equals(subtask1));
    }
}
