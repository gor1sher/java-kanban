package service.TaskManagers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryManagers.InMemoryHistoryManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String file = "tasks.txt";

    public FileBackedTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        super(inMemoryHistoryManager);
    }

    @Override
    public Task createTask(Task task) {
        readFile();
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        readFile();
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        readFile();
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void removeTask(int id) {
        readFile();
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        readFile();
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        readFile();
        super.removeSubtask(id);
        save();
    }

    void save() {
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                fileWriter.write(TaskCsvConverter.subtaskToCsv(entry.getKey(), entry.getValue()));
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                fileWriter.write(TaskCsvConverter.epicToCsv(entry.getKey(), entry.getValue()));
            }

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                fileWriter.write(TaskCsvConverter.taskToCsv(entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] columns = line.split(",");

                int id = Integer.parseInt(columns[0].trim());
                String type = columns[1].trim();

                if (type.equals("Task")) {
                    Task task = TaskCsvConverter.csvLineToTask(line);
                    tasks.put(id, task);
                } else if (type.equals("Epic")) {
                    Epic epic = TaskCsvConverter.csvLineToEpic(line);
                    epics.put(id, epic);
                } else {
                    Subtask subtask = TaskCsvConverter.csvLineToSubtask(line);
                    subtasks.put(id, subtask);
                }

                if (id > getId()) {
                    setId(id);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата числа в строке: " + e.getMessage());
        }
    }
}
