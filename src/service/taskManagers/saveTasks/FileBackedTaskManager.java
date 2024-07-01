package service.taskManagers.saveTasks;

import model.Epic;
import model.Subtask;
import model.Task;
import service.historyManagers.InMemoryHistoryManager;
import service.taskManagers.InMemoryTaskManager;
import service.taskManagers.exception.ManagerSaveException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String file = "tasks.txt";
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    public FileBackedTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        super(inMemoryHistoryManager);
        readFile();
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                fileWriter.write(TaskCsvConverter.subtaskToCsv(entry.getValue()));
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                fileWriter.write(TaskCsvConverter.epicToCsv(entry.getValue()));
            }

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                fileWriter.write(TaskCsvConverter.taskToCsv(entry.getValue()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    private void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] columns = line.split(",");

                int id = Integer.parseInt(columns[0].trim());
                String type = columns[1].trim();

                if (type.equals("TASK")) {
                    Task task = TaskCsvConverter.csvLineToTask(line);
                    tasks.put(id, task);
                } else if (type.equals("EPIC")) {
                    Epic epic = TaskCsvConverter.csvLineToEpic(line);
                    epics.put(id, epic);
                } else {
                    Subtask subtask = TaskCsvConverter.csvLineToSubtask(line);
                    subtasks.put(id, subtask);
                }

                if (id > super.id) super.id = id;
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                Epic epic = epics.get(entry.getValue().getEpic());

                updateStartTimeEpic(epic);
                updateEndTimeEpic(epic);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Ошибка формата числа в строке: " + e.getMessage());
        }
    }
}
