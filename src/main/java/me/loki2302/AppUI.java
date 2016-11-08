package me.loki2302;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
public class AppUI extends UI {
    @Autowired
    private DummyService dummyService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Button button = new Button("Hello");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Hello there");
                dummyService.doSomething();
            }
        });

        setContent(new VerticalLayout(
                new Label("Push this button"),
                button,
                new Calculator()));
    }
}
