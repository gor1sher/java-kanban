package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayList<Task> historyList;

    public InMemoryHistoryManager(){
        historyList = new ArrayList<>();
    }
    @Override
    public void add(Task task) {
        historyList.add(task);
    }

    public void remove(int zero){
        historyList.remove(zero);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
