import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void updateStatusForEpic_inProgress_InMemoryTaskMeneger() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void updateStatusForEpic_Done_InMemoryTaskMeneger() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.DONE, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void updateStatusForEpic_New_InMemoryTaskMeneger() {
        Epic epic = new Epic("дом", "купить дом", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("посадить дерево", "березка", Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        assertEquals(Status.NEW, epic.getStatus());
    }
}
