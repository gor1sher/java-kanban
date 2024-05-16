package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyMap;
    private CustomLinkedList historyList;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
        historyList = new CustomLinkedList();
    }

    @Override
    public void add(Integer id, Task task) {
        historyMap.remove(id);
        historyList.add(task);
        historyMap.put(id, historyList.getNodeTail());
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.get(id);
        historyList.remove(node);
        historyMap.remove(node);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node next = historyList.head;
        while (next != null) {
            tasks.add(next.getTask());
            next = next.next;
        }
        return tasks;
    }

    private class Node {

        Task task;
        Node next;
        Node prev;

        public Task getTask() {
            return task;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public Node(Task task) {
            this.task = task;
        }
    }

    private class CustomLinkedList {

        Node head;
        Node tail;

        CustomLinkedList() {
            head = null;
            tail = null;
        }

        void add(Task task) {
            Node newNode = new Node(task);
            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;
        }

        Node getNodeTail() {
            return tail;
        }

        Node getNodeHead() {
            return head;
        }

        void remove(Node node) {
            if (node == null) {
                return;
            }

            if (node == head && node == tail) {
                head = null;
                tail = null;
                return;
            }

            if (node == head) {
                head = node.next;
                head.prev = null;
                return;
            }

            if (node == tail) {
                tail = node.prev;
                tail.next = null;
                return;
            }

            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}
