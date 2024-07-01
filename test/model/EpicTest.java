package model;

import org.junit.jupiter.api.Test;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void addSubtask_shouldNotAddYourself() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));
        inMemoryTaskManager.createEpic(epic);
        epic.addSubtack(epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }

    @Test
    void setId_eachOtherIfTheirIdIsEqual() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        epic.setId(20);
        Epic epic1 = new Epic("дом", "купить дом", Status.NEW, Duration.ofMinutes(22),
                LocalDateTime.of(2023, 3, 15, 12, 30, 0));

        epic1.setId(20);
        assertEquals(epic, epic1);
    }
}