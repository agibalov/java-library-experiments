package me.loki2302;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DesignRoot
public class Calculator extends VerticalLayout {
    private final static Logger LOGGER = LoggerFactory.getLogger(Calculator.class);

    TextField aTextField;
    TextField bTextField;
    Button addNumbersButton;

    public Calculator() {
        Design.read(this);

        addNumbersButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                int a = Integer.valueOf(aTextField.getValue());
                int b = Integer.valueOf(bTextField.getValue());
                int result = a + b;

                LOGGER.info("{} and {} is {}", a, b, result);

                Notification.show(String.format("%d + %d = %d", a, b, result));

                aTextField.setValue("");
                bTextField.setValue("");
            }
        });
    }
}
