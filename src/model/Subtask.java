package model;

public class Subtask extends Task{
    public Epic epic;

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {

        return epic;
    }

    public void setEpic(Epic epic) {

        this.epic = epic;
    }

}
