package me.loki2302;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

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
	}
	
	public static abstract class UiElement {
		@Attribute(name = "id")
		public String id;
	}
	
	public static abstract class ContainerUiElement extends LayoutFromXmlApp.UiElement {
		@Attribute(name = "orientation")
		public String orientation; // TODO: how do i use enum here?
	}
	
	@Root(name = "window")
	public static class WindowUiElement extends LayoutFromXmlApp.ContainerUiElement {
		@Attribute(name = "title")
		public String title;
	}
	
	@Root(name = "label")
	public static class LabelUiElement extends LayoutFromXmlApp.UiElement {
		@Attribute(name = "text")
		public String text;
	}
	
	@Root(name = "input")
	public static class InputUiElement extends LayoutFromXmlApp.UiElement {			
	}
	
	@Root(name = "button")
	public static class ButtonUiElement extends LayoutFromXmlApp.UiElement {
		@Attribute(name = "text")
		public String text;
	}
	
	@Root(name = "box")
	public static class BoxContainerUiElement extends LayoutFromXmlApp.ContainerUiElement {
		// TODO: how do i add children here?
	}
}