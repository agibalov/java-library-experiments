package me.loki2302.uidom;

import java.util.List;



import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

public abstract class ContainerUiElement extends UiElement {
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