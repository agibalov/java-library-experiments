package me.loki2302.uidom;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "button")
public class ButtonUiElement extends UiElement {
	@Attribute(name = "text")
	public String text;

	@Override
	public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}
}