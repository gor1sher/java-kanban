package model;

public class Subtask extends Task{
    private Integer epic;

    public Subtask(String name, String description, Status status, Integer epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Integer getEpic() {
        return epic;
    }

    public void setEpic(Integer epic) {
        this.epic = epic;
    }

}
