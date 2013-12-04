package me.loki2302.uidom;


import org.simpleframework.xml.Root;

@Root(name = "input")
public class InputUiElement extends UiElement {
	@Override
	public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}
}