package me.loki2302.uidom;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root    
public abstract class UiElement {
	@Attribute(name = "id", required = false)
	public String id;
	
	public abstract <TResult> TResult accept(UiElementVisitor<TResult> visitor);		
}