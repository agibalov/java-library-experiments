package me.loki2302;

public class TodoItemCreatedEvent {
    public final String todoId;
    public final String text;

    public TodoItemCreatedEvent(String todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }
}
