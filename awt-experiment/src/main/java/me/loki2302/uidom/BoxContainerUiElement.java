package me.loki2302.uidom;


import org.simpleframework.xml.Root;

@Root(name = "box")
public class BoxContainerUiElement extends ContainerUiElement {
	@Override
	public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}		
}