package me.loki2302;

import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.swing.BoxLayout;

import me.loki2302.uidom.BoxContainerUiElement;
import me.loki2302.uidom.ButtonUiElement;
import me.loki2302.uidom.InputUiElement;
import me.loki2302.uidom.LabelUiElement;
import me.loki2302.uidom.UiDefinition;
import me.loki2302.uidom.UiElement;
import me.loki2302.uidom.UiElementVisitor;
import me.loki2302.uidom.WindowContainerUiElement;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class LayoutFromXmlApp {	
	public static void main(String[] args) {
		/*

		TODO: load everything from resources/calculator_view.xml
		
		<window title="Hello World" orientation="vertical">
			<box orientation="horizontal">
				<label text="Number A:" />
				<input id="numberAInput" />
			</box>
			<box orientation="horizontal">
				<label text="Number B:" />
				<input id="numberBInput" />
			</box>
			<button id="addNumbersButton" text="Add numbers" />
			<box orientation="horizontal">
				<label text="Result:" />
				<label id="resultLabel" />
			</box>				
		</window>
		
		*/
		
		InputStream inputStream = LayoutFromXmlApp.class.getResourceAsStream("/calculator_view.xml");
		try {
			UiDefinition uiDefinition = serializer.read(UiDefinition.class, inputStream);
			System.out.println(uiDefinition);			
			System.out.println(uiDefinition.rootElement);
			
			Component rootComponent = uiDefinition.rootElement.accept(new UiElementVisitor<Component>() {
				@Override
				public Component visit(WindowContainerUiElement e) {
					Frame frame = new Frame(e.title);
					frame.setLayout(e.orientation.equals("horizontal") ? 
							new BoxLayout(frame, BoxLayout.X_AXIS) : 
							new BoxLayout(frame, BoxLayout.Y_AXIS));
					
					for(UiElement element : e.children) {
						Component childComponent = element.accept(this);
						frame.add(childComponent);
					}
					return frame;
				}

				@Override
				public Component visit(BoxContainerUiElement e) {					
					Panel panel = new Panel();
					panel.setLayout(e.orientation.equals("horizontal") ? 
							new BoxLayout(panel, BoxLayout.X_AXIS) : 
							new BoxLayout(panel, BoxLayout.Y_AXIS));
					
					for(UiElement element : e.children) {
						Component childComponent = element.accept(this);
						panel.add(childComponent);
					}
					return panel;
				}

				@Override
				public Component visit(LabelUiElement e) {
					Label label = new Label(e.text);
					return label;
				}

				@Override
				public Component visit(InputUiElement e) {
					TextField textField = new TextField(20);
					return textField;
				}

				@Override
				public Component visit(ButtonUiElement e) {
					Button button = new Button(e.text);
					return button;
				}				
			});
			
			System.out.println(rootComponent);
			
			Frame frame = (Frame)rootComponent;
			frame.pack();
	    	
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent event) {
					System.exit(0);
				}
			});			
			
		} catch (Exception e) {			
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {}
		}
	}
	
	private static Serializer serializer = new Persister();
	
	public static String toXml(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer.write(o, baos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        byte[] bytes = baos.toByteArray();
        String xml = new String(bytes, Charset.forName("UTF-8"));
        
        return xml;
    }
    
    public static <T> T fromXml(String xml, Class<T> clazz) {
        try {
            return serializer.read(clazz, xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}