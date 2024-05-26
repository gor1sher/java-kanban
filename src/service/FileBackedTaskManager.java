package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final HashMap<Integer, Task> savedTasks;
    private final HashMap<Integer, Task> readTasks;
    private int id = 0;
    String file = "tasks.txt";

    private int generatedId() {
        return id++;
    }

    public FileBackedTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        super(inMemoryHistoryManager);
        savedTasks = new HashMap<>();
        readTasks = new HashMap<>();
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save(epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save(subtask);
        return subtask;
    }

    public HashMap<Integer, Task> getReadTasks() {
        return readTasks;
    }

    public HashMap<Integer, Task> getSavedTasks() {
        return savedTasks;
    }

    private void save(Task taskSave) {
        savedTasks.put(generatedId(), taskSave);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            for (Map.Entry<Integer, Task> entry : savedTasks.entrySet()) {
                if (entry.getValue() instanceof Subtask) {
                    String line = String.format("%d,%s,%s,%s,%d,%s\n",
                            entry.getKey(), "Subtask", entry.getValue().getName(),
                            entry.getValue().getStatus().toString(), ((Subtask) entry.getValue()).getEpic(),
                            entry.getValue().getDescription());

                    fileWriter.write(line);
                } else if (entry.getValue() instanceof Epic) {
                    String line = String.format("%d,%s,%s,%s,%s\n",
                            entry.getKey(), "Epic", entry.getValue().getName(),
                            entry.getValue().getStatus().toString(), entry.getValue().getDescription());

                    fileWriter.write(line);
                } else {
                    String line = String.format("%d,%s,%s,%s,%s\n",
                            entry.getKey(), "Task", entry.getValue().getName(),
                            entry.getValue().getStatus().toString(), entry.getValue().getDescription());

                    fileWriter.write(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    public void readFail() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] columns = line.split(",");

                int id = Integer.parseInt(columns[0].trim());
                String type = columns[1].trim();
                String name = columns[2].trim();
                Status status = Status.valueOf(columns[3].trim());

                if (type.trim().equals("Task")) {
                    String description = columns[4].trim();
                    Task task = new Task(name, description, status);
                    readTasks.put(id, task);
                } else if (type.trim().equals("Epic")) {
                    String description = columns[4].trim();
                    Epic epic = new Epic(name, description, status);
                    readTasks.put(id, epic);
                } else {
                    String description = columns[5].trim();
                    Integer epic = Integer.parseInt(columns[4].trim());
                    Subtask subtask = new Subtask(name, description, status, epic);
                    readTasks.put(id, subtask);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата числа в строке: " + e.getMessage());
        }
    }
}
