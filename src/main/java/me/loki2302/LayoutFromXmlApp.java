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
import java.util.List;

import javax.swing.BoxLayout;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;
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
	
    @Root(name = "ui")
    public static class UiDefinition {
    	@ElementUnion({
        	@Element(name = "window", type = WindowContainerUiElement.class),
        	@Element(name = "box", type = BoxContainerUiElement.class),
        	@Element(name = "label", type = LabelUiElement.class),
        	@Element(name = "input", type = InputUiElement.class),
        	@Element(name = "button", type = ButtonUiElement.class),
        })
    	public UiElement rootElement;
    }
    
    @Root    
	public static abstract class UiElement {
		@Attribute(name = "id", required = false)
		public String id;
		
		public abstract <TResult> TResult accept(UiElementVisitor<TResult> visitor);		
	}
    
    public static interface UiElementVisitor<TResult> {
		TResult visit(WindowContainerUiElement e);
		TResult visit(BoxContainerUiElement e);
		TResult visit(LabelUiElement e);
		TResult visit(InputUiElement e);
		TResult visit(ButtonUiElement e);
	}
	
	public static abstract class ContainerUiElement extends UiElement {
		@Attribute(name = "orientation")
		public String orientation; // TODO: how do i use enum here?
		
		// TODO: how do i get rid of this duplication?
		@ElementListUnion({
			@ElementList(entry = "box", inline = true, type = BoxContainerUiElement.class),
			@ElementList(entry = "label", inline = true, type = LabelUiElement.class),
			@ElementList(entry = "input", inline = true, type = InputUiElement.class),
			@ElementList(entry = "button", inline = true, type = ButtonUiElement.class),
		})
		public List<UiElement> children;
	}
	
	@Root(name = "window")
	public static class WindowContainerUiElement extends ContainerUiElement {
		@Attribute(name = "title")
		public String title;

		@Override
		public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
			return visitor.visit(this);
		}
	}
	
	@Root(name = "label")
	public static class LabelUiElement extends UiElement {
		@Attribute(name = "text", required = false)
		public String text;

		@Override
		public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
			return visitor.visit(this);
		}
	}
	
	@Root(name = "input")
	public static class InputUiElement extends UiElement {
		@Override
		public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
			return visitor.visit(this);
		}
	}
	
	@Root(name = "button")
	public static class ButtonUiElement extends UiElement {
		@Attribute(name = "text")
		public String text;

		@Override
		public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
			return visitor.visit(this);
		}
	}
	
	@Root(name = "box")
	public static class BoxContainerUiElement extends ContainerUiElement {
		@Override
		public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
			return visitor.visit(this);
		}		
	}
}