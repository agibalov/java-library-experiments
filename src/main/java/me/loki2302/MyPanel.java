package me.loki2302;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Panel;
import com.vaadin.ui.declarative.Design;

@DesignRoot
public class MyPanel extends Panel {
    // How do I connect this to UI component?
    // What's the view and what's the UI?
    public MyPanel() {
        Design.read("MyPanel.html", this);
    }
}
