package org.vaadin.hezamu.canvas;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class CanvasApp extends Application {
	private static final long serialVersionUID = -8463345951901567159L;

	@Override
	public void init() {
		setMainWindow(new Window(getClass().getSimpleName()));

		Canvas canvas = new Canvas();
		canvas.setWidth("400px");
		canvas.setHeight("400px");

		// Draw some shapes to the canvas
		canvas.saveContext();
		canvas.clear();
		canvas.translate(175, 175);
		canvas.scale(1.6f, 1.6f);
		for (int i = 1; i < 6; ++i) {
			canvas.saveContext();
			canvas.setFillStyle("rgb(" + (51 * i) + "," + (255 - 51 * i)
					+ ",255)");

			for (int j = 0; j < i * 6; ++j) {
				canvas.rotate((float) (Math.PI * 2 / (i * 6)));
				canvas.beginPath();
				canvas.arc(0, i * 12.5f, 5, 0, (float) (Math.PI * 2), true);
				canvas.fill();
			}

			canvas.restoreContext();
		}

		canvas.restoreContext();
		
		canvas.drawImage(
				"http://www.google.ru/intl/en_com/images/srpr/logo1w.png", 50,
				50);

		getMainWindow().addComponent(canvas);
	}
}
