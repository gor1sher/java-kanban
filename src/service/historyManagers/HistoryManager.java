package service.historyManagers;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Integer id, Task task);

    void remove(int id);

    List<Task> getHistory();
}
