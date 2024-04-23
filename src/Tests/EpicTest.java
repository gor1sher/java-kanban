package Tests;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    void testAddSubtask() {
        Epic epic = new Epic("аза", "куув", Status.IN_PROGRESS);
        epic.addSubtack(epic.getId(), epic.getId());
        assertTrue(epic.getSubtasks().isEmpty(), "Список подзадач должен оставаться пустым");
    }
}