package me.loki2302.uidom;



import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

@Root(name = "ui")
public class UiDefinition {
	@ElementUnion({
    	@Element(name = "window", type = WindowContainerUiElement.class),
    	@Element(name = "box", type = BoxContainerUiElement.class),
    	@Element(name = "label", type = LabelUiElement.class),
    	@Element(name = "input", type = InputUiElement.class),
    	@Element(name = "button", type = ButtonUiElement.class),
    })
	public UiElement rootElement;
}