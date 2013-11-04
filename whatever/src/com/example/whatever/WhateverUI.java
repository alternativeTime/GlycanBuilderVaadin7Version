package com.example.whatever;

import javax.servlet.annotation.WebServlet;

import com.easy.canvas.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("whatever")
public class WhateverUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = WhateverUI.class, widgetset = "com.example.whatever.widgetset.WhateverWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	private static final int height = 400;
	private static final int width = 500;
	private Canvas canvas;
	
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		createCanvas();
		layout.addComponent(canvas);		
	}
	
	private void createCanvas() {
		  canvas = new Canvas();
	      canvas.setWidth(width + "px");
	      canvas.setHeight(height + "px");
	      canvas.setMinimumSize(800, 800);
	      //canvas.setCoordinateSpaceWidth(width));
	      //canvas.setCoordinateSpaceHeight(height));
	      //canvas.beginPath();
	      canvas.setFillStyle("#F00");
	      canvas.fillRect(100, 50, 100, 100);
	      canvas.setFillStyle("#0F0");
	      canvas.fillRect(200, 150, 100, 100);
	      canvas.setFillStyle("#00F");
	      canvas.fillRect(300, 250, 100, 100);
	      //canvas.closePath();
	      
			//canvas.beginPath();
			canvas.moveTo(75,40);
			canvas.setFillStyle("#FFF");
			canvas.cubicCurveTo(75,37,70,25,50,25);
			canvas.cubicCurveTo(20,25,20,62.5,20,62.5);
			canvas.cubicCurveTo(20,80,40,102,75,120);
			canvas.cubicCurveTo(110,102,130,80,130,62.5);
			canvas.cubicCurveTo(130,62.5,130,25,100,25);
			canvas.cubicCurveTo(85,25,75,37,75,40);
			//canvas.closePath();
			canvas.fill();
	      canvas.enableMouseSelectionRectangle(true);
	      canvas.setBackgroundColor("#EF6");
	   }

}