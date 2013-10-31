package com.example.whatever;

import javax.servlet.annotation.WebServlet;

import com.easy.canvas.Canvas;
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

	int yPos=0;
	
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		button.setSizeUndefined();
		layout.addComponent(button);
		Label label = new Label("EnhancedCanvas Test Application");
		layout.addComponent(label);
		
		final Canvas theCanvas = new Canvas();
		layout.addComponent(theCanvas);
		
		theCanvas.setCaption("hello world");
		
		layout.setSizeFull();	
		//layout.setWidth("600px");
		//layout.setHeight("600px");
		//Panel panel=new Panel();
		//panel.setContent(theCanvas);
		layout.setExpandRatio(theCanvas, 1f);	
		
//		VerticalLayout layout=(VerticalLayout) panel.getContent();
		
		//panel.setSizeFull();

//		
		theCanvas.setName("CanvasTest");
		theCanvas.enableMouseSelectionRectangle(true);
		theCanvas.stroke();
		theCanvas.setBackgroundColor("#F11");
		
		theCanvas.setSizeFull();
		theCanvas.setMinimumSize(300, 300);
		
		theCanvas.setFont("12pt Calibri");
		theCanvas.textAlign("right");
		theCanvas.setFillStyle("blue");
		theCanvas.fillText("-.0123456789:;", 800,100);
		theCanvas.setFont("Bold 30px Sans-Serif");

		// TODO: not working with max width theCanvas.fillText("HELLO", 400, 50, 500);
		theCanvas.fillText("HELLO", 400, 50);
		theCanvas.beginPath();
		theCanvas.moveTo(75,40);
		theCanvas.cubicCurveTo(75,37,70,25,50,25);
		theCanvas.cubicCurveTo(20,25,20,62.5,20,62.5);
		theCanvas.cubicCurveTo(20,80,40,102,75,120);
		theCanvas.cubicCurveTo(110,102,130,80,130,62.5);
		theCanvas.cubicCurveTo(130,62.5,130,25,100,25);
		theCanvas.cubicCurveTo(85,25,75,37,75,40);
		theCanvas.closePath();
		theCanvas.fill();
		//theCanvas.setScroll(2000, 2000);
		theCanvas.respondToExportRequest("Export command without type");
		
		NativeButton buttonRec=new NativeButton("Add rectangle");
		buttonRec.addClickListener(new ClickListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 8867521933360799919L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.setFillStyle("blue");
				theCanvas.fillRect(0, yPos, 100, 100);
				yPos+=110;
				
				//TODO: does not resize
				theCanvas.setMinimumSize(300, yPos);
				theCanvas.setScroll(yPos, 300);
			}
		});
		layout.addComponent(buttonRec);
		
		Button linesDemo = new Button("Lines Demo", new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.saveContext();
				theCanvas.clear();
				theCanvas.scale(0.9f, 0.9f);
				theCanvas.translate(30, 40);
				theCanvas.saveContext();
				for (int i = 0; i < 10; ++i) {
					theCanvas.setLineWidth(1 + i);
					theCanvas.beginPath();
					theCanvas.moveTo(5 + i * 14, 5);
					theCanvas.lineTo(5 + i * 14, 140);
					theCanvas.stroke();
				}
				theCanvas.restoreContext();
				theCanvas.saveContext();
				theCanvas.translate(0, 170);
				theCanvas.setLineWidth(2);
				theCanvas.beginPath();
				theCanvas.moveTo(10, 10);
				theCanvas.lineTo(140, 10);
				theCanvas.moveTo(10, 140);
				theCanvas.lineTo(140, 140);
				theCanvas.stroke();
				theCanvas.setLineWidth(15);
				theCanvas.beginPath();
				theCanvas.moveTo(25, 10);
				theCanvas.lineTo(25, 140);
				theCanvas.stroke();
				theCanvas.beginPath();
				theCanvas.moveTo(75, 10);
				theCanvas.lineTo(75, 140);
				theCanvas.stroke();
				theCanvas.beginPath();
				theCanvas.moveTo(125, 10);
				theCanvas.lineTo(125, 140);
				theCanvas.stroke();
				theCanvas.restoreContext();
				theCanvas.saveContext();
				theCanvas.translate(170, 0);
				theCanvas.setLineWidth(10);
				theCanvas.beginPath();
				theCanvas.moveTo(-5, 5);
				theCanvas.lineTo(35, 45);
				theCanvas.lineTo(75, 5);
				theCanvas.lineTo(115, 45);
				theCanvas.lineTo(155, 5);
				theCanvas.stroke();
				theCanvas.beginPath();
				theCanvas.moveTo(-5, 5 + 40);
				theCanvas.lineTo(35, 45 + 40);
				theCanvas.lineTo(75, 5 + 40);
				theCanvas.lineTo(115, 45 + 40);
				theCanvas.lineTo(155, 5 + 40);
				theCanvas.stroke();
				theCanvas.beginPath();
				theCanvas.moveTo(-5, 5 + 80);
				theCanvas.lineTo(35, 45 + 80);
				theCanvas.lineTo(75, 5 + 80);
				theCanvas.lineTo(115, 45 + 80);
				theCanvas.lineTo(155, 5 + 80);
				theCanvas.stroke();
				theCanvas.restoreContext();
				theCanvas.saveContext();
				theCanvas.translate(170, 170);
				theCanvas.setLineWidth(2);
				theCanvas.strokeRect(-5, 50, 160, 50);
				theCanvas.setLineWidth(10);
				theCanvas.setMiterLimit(10);
				theCanvas.beginPath();
				theCanvas.moveTo(0, 100);
				float dy;
				for (int i = 0; i < 19; ++i) {
					if (i % 2 == 0) {
						dy = 25.0f;
					} else {
						dy = -25.0f;
					}
					theCanvas.lineTo((float) (Math.pow(i, 1.5) * 2.0f), 75 + dy);
				}
				theCanvas.stroke();
				theCanvas.restoreContext();
				theCanvas.restoreContext();
				
			}
		});
		
		layout.addComponent(linesDemo);
		
		//mainWindow.addComponent(panel);
		//((VerticalLayout)mainWindow.getContent()).setExpandRatio(panel, 1f);
		//setMainWindow(mainWindow);
		
//		theCanvas.addSelectionListener(new SelectionListener(){
//			@Override
//			public void recieveSelectionUpdate(double x, double y, double width, double height, boolean mouseMoved) {
//				logger.info(x+"|"+y+"|"+width+"|"+height+"|"+mouseMoved);
//			}
//		});
		
		//theCanvas.enableMouseMoveEventFiring();
		
//		theCanvas.addListener(new DimensionEventListener() {
//			@Override
//			public void dimensionUpdate(float width, float height, Component component, float parentWidth, float parentHeight, int scrollTop) {
//				logger.info("Canvas size: "+width+"|"+height+"|"+parentWidth+"|"+parentHeight+"|"+scrollTop);
//			}
//		});
		
	}

}