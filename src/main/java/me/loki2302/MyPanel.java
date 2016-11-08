package me.loki2302;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.declarative.Design;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DesignRoot
public class MyPanel extends HorizontalLayout {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyPanel.class);

    Button theButton;

    public MyPanel() {
        Design.read(this);

        theButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                LOGGER.info("Omg!");
                Notification.show("MyPanel says hi!");
            }
        });
    }
}
