package me.loki2302;

public class TodoItemUpdatedEvent {
    public final String todoId;
    public final String text;

    public TodoItemUpdatedEvent(String todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }
}
