package me.loki2302.uidom;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "window")
public class WindowContainerUiElement extends ContainerUiElement {
	@Attribute(name = "title")
	public String title;

	@Override
	public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}
}