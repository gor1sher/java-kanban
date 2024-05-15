import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void addSubtask_shouldNotAddYourself_Epic() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        inMemoryTaskManager.createEpic(epic);
        epic.addSubtack(epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }

    @Test
    void setId_eachOtherIfTheirIdIsEqual_Epic() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.setId(20);
        Epic epic1 = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic1.setId(20);
        assertEquals(epic, epic1);
    }
}