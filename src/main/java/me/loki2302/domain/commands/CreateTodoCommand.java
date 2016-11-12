package me.loki2302.domain.commands;

public class CreateTodoCommand {
    public final String text;

    public CreateTodoCommand(String text) {
        this.text = text;
    }
}
