package me.loki2302;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

public class SimpleApp {	
	public static void main(String[] args) {        
    	Frame frame = new Frame("Hello World");
    	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
    	
    	Panel numberAPanel = new Panel();
    	numberAPanel.setLayout(new BoxLayout(numberAPanel, BoxLayout.X_AXIS));    	
    	numberAPanel.add(new Label("Number A:"));    	
    	final TextField numberATextField = new TextField(20);
    	numberAPanel.add(numberATextField);
    	frame.add(numberAPanel);
    	
    	Panel numberBPanel = new Panel();
    	numberBPanel.setLayout(new BoxLayout(numberBPanel, BoxLayout.X_AXIS));    	
    	numberBPanel.add(new Label("Number B:"));    	
    	final TextField numberBTextField = new TextField(20);
    	numberBPanel.add(numberBTextField);
    	frame.add(numberBPanel);
    	
    	Button addNumbersButton = new Button("Add numbers");
    	frame.add(addNumbersButton);
    	
    	Panel resultPanel = new Panel();
    	resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.X_AXIS));
    	resultPanel.add(new Label("Result:"));
    	final Label resultLabel = new Label();
    	resultPanel.add(resultLabel);
    	frame.add(resultPanel);
    	
    	frame.pack();
    	
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		addNumbersButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String numberAString = numberATextField.getText();				
				int numberA;
				try {
					numberA = Integer.parseInt(numberAString);
				} catch(NumberFormatException ex) {
					//
					throw new RuntimeException(ex);
				}
				
				String numberBString = numberBTextField.getText();
				int numberB;
				try {
					numberB = Integer.parseInt(numberBString);
				} catch(NumberFormatException ex) {
					//					
					throw new RuntimeException(ex);
				}
				
				int result = numberA + numberB;
				resultLabel.setText(Integer.toString(result));
			}			
		});
    }
}
