package com.example.whatever;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public abstract class WidgetDemo extends VerticalLayout{
	
	private static final long serialVersionUID = 2551230003240474084L;

	boolean isInitialized = false;

	public boolean initIfNeeded() {
		if (isInitialized)
			return true;

		setSizeFull();
		setSpacing(true);
		setMargin(true);

		Button b;
		addComponent(b = new Button("Show source", new ClickListener() {
			private static final long serialVersionUID = -9177551932248006636L;

			@Override
			public void buttonClick(ClickEvent event) {
				showSourceClicked();
			}
		}));

		b.setStyleName(BaseTheme.BUTTON_LINK);

		isInitialized = true;

		return false;
	}

	public void showSourceClicked() {
		Window window = new Window(getClass().getSimpleName() + " source");
		window.setWidth("800px");
		window.setHeight("600px");
		window.center();

		window.addComponent(new Label(getDemoSource(),
				Label.CONTENT_PREFORMATTED));

		getWindow().addWindow(window);
	}

	public abstract String getDemoSource();

}
