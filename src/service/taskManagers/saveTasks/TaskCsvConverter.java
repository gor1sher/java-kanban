package service.taskManagers.saveTasks;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskCsvConverter {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static String taskToCsv(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s\n",
                task.getId(), TaskType.TASK, task.getName(),
                task.getStatus().toString(), task.getDescription(),
                task.getDuration().toString(), task.getStartTime().format(formatter));
    }

    public static String epicToCsv(Epic task) {
        return String.format("%d,%s,%s,%s,%s\n",
                task.getId(), TaskType.EPIC, task.getName(),
                task.getStatus().toString(), task.getDescription());
    }

    public static String subtaskToCsv(Subtask task) {
        return String.format("%d,%s,%s,%s,%d,%s,%s,%s\n",
                task.getId(), TaskType.SUBTASK, task.getName(),
                task.getStatus().toString(), task.getEpic(),
                task.getDescription(), task.getDuration().toString(), task.getStartTime().format(formatter));
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
        Duration duration = Duration.parse(columns[5].trim());
        LocalDateTime localDateTime = LocalDateTime.parse(columns[6].trim(), formatter);

        Task task = new Task(columns[2], columns[4], status, duration, localDateTime);
        task.setId(id);

        return task;
    }

    public static Subtask csvLineToSubtask(String line) {
        String[] columns = csvLine(line);
        String description = columns[5].trim();
        Integer epic = Integer.parseInt(columns[4].trim());
        Status status = Status.valueOf(columns[3].trim());
        int id = Integer.parseInt(columns[0].trim());
        Duration duration = Duration.parse(columns[6].trim());
        LocalDateTime localDateTime = LocalDateTime.parse(columns[7].trim(), formatter);

        Subtask subtask = new Subtask(columns[2], description, status, epic, duration, localDateTime);
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
