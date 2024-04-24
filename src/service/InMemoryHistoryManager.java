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

    @Override
    public ArrayList<Task> getHistory() {
        if (historyList.size() <= 10) {
            return historyList;
        } else {
            while (historyList.size() > 10){
                historyList.remove(0);
            }
            return historyList;
        }
    }
}
