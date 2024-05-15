package service;

public class Managers {

    static InMemoryTaskManager inMemoryTaskManager;

    public static TaskManager getDefault() {
        inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        return inMemoryTaskManager;
    }
}
