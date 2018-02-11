package me.loki2302.uidom;



public interface UiElementVisitor<TResult> {
	TResult visit(WindowContainerUiElement e);
	TResult visit(BoxContainerUiElement e);
	TResult visit(LabelUiElement e);
	TResult visit(InputUiElement e);
	TResult visit(ButtonUiElement e);
}