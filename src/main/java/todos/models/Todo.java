package todos.models;

public class Todo {

    private String name;
    private boolean isDone;

    public Todo(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String toJson() {
        return "{ \"name\": \"" + this.getName() + "\"," +
                "\"isDone\": " + this.isDone() + "\"}";
    }
}
