package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubtaskTest {

    @Test
    void setId_changesId() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, 15,
                Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 16, 12, 30, 0));

        int newId = 15;
        subtask.setId(newId);
        assertEquals(newId, subtask.getId());
    }
}
