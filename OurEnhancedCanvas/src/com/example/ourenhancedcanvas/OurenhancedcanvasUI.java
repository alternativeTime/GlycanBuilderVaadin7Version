package com.example.ourenhancedcanvas;

import javax.servlet.annotation.WebServlet;

import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas;

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
//import java.util.logging.Logger;
//import org.vaadin.hezamu.canvas.Canvas.DimensionEventListener;


@Theme("ourenhancedcanvas")
public class OurenhancedcanvasUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5933174761250362892L;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = OurenhancedcanvasUI.class, widgetset="com.example.ourenhancedcanvas.widgetset.OurenhancedcanvasWidgetset")
	public static class Servlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -586638892370754436L;
	}
	
	//Logger logger=Logger.getLogger("test");
	
	int yPos=0;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 515279044386449481L;

			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		layout.addComponent(button);
		

		
		Label label = new Label("EnhancedCanvas Test Application");
		layout.addComponent(label);
		
//		Panel panel=new Panel();
//		VerticalLayout layout=(VerticalLayout) panel.getContent();
		
		//panel.setSizeFull();
		layout.setSizeFull();
		
		final Canvas theCanvas=new Canvas();
		layout.addComponent(theCanvas);
		layout.setExpandRatio(theCanvas, 1f);
		
		//theCanvas.setName("CanvasTest");
		theCanvas.enableMouseSelectionRectangle(true);
		theCanvas.stroke();
		
		theCanvas.setSizeFull();
		theCanvas.setMinimumSize(300, 400);
		
		theCanvas.setFont("12pt Calibri");
		theCanvas.textAlign("left");
		theCanvas.setFillStyle("black");
		theCanvas.fillText("-.0123456789:;", 50,50,100);
		
		NativeButton buttonRec=new NativeButton("Add rectangle");
		buttonRec.addClickListener(new ClickListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 8867521933360799919L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.setFillStyle("blue");
				theCanvas.fillRect(0, yPos, 20, 20);
				yPos+=20;
				
				
				theCanvas.setMinimumSize(300, yPos);
				theCanvas.setScroll(yPos, 0);
			}
		});
		
		layout.addComponent(buttonRec);
		
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