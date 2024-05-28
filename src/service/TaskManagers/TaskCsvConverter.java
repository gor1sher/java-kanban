package service.TaskManagers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class TaskCsvConverter {

    public static String taskToCsv(Integer id, Task task) {
        return String.format("%d,%s,%s,%s,%s\n",
                id, "Task", task.getName(),
                task.getStatus().toString(), task.getDescription());
    }

    public static String epicToCsv(Integer id, Epic task) {
        return String.format("%d,%s,%s,%s,%s\n",
                id, "Epic", task.getName(),
                task.getStatus().toString(), task.getDescription());
    }

    public static String subtaskToCsv(Integer id, Subtask task) {
        return String.format("%d,%s,%s,%s,%d,%s\n",
                id, "Subtask", task.getName(),
                task.getStatus().toString(), task.getEpic(),
                task.getDescription());
    }

    private static String[] csvLine(String line) {
        String[] columns = line.split(",");
        columns[2] = columns[2].trim();
        columns[4] = columns[4].trim();

        return columns;
    }

    public static Task csvLineToTask(String line) {
        String[] columns = csvLine(line);
        Status status = Status.valueOf(columns[3].trim());
        int id = Integer.parseInt(columns[0].trim());

        Task task = new Epic(columns[2], columns[4], status);
        task.setId(id);

        return task;
    }

    public static Subtask csvLineToSubtask(String line) {
        String[] columns = csvLine(line);
        String description = columns[5].trim();
        Integer epic = Integer.parseInt(columns[4].trim());
        Status status = Status.valueOf(columns[3].trim());
        int id = Integer.parseInt(columns[0].trim());

        Subtask subtask = new Subtask(columns[2], description, status, epic);
        subtask.setId(id);

        return subtask;
    }

    public static Epic csvLineToEpic(String line) {
        String[] columns = csvLine(line);
        Status status = Status.valueOf(columns[3].trim());
        int id = Integer.parseInt(columns[0].trim());

        Epic epic = new Epic(columns[2], columns[4], status);
        epic.setId(id);

        return epic;
    }
}
