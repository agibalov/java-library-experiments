package me.loki2302.uidom;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "label")
public class LabelUiElement extends UiElement {
	@Attribute(name = "text", required = false)
	public String text;

	@Override
	public <TResult> TResult accept(UiElementVisitor<TResult> visitor) {
		return visitor.visit(this);
	}
}