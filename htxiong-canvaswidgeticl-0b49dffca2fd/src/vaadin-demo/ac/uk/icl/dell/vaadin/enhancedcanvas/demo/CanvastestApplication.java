package ac.uk.icl.dell.vaadin.enhancedcanvas.demo;

import java.util.logging.Logger;

import org.vaadin.damerell.canvas.BasicCanvas;
import org.vaadin.damerell.canvas.BasicCanvas.SelectionListener;
import org.vaadin.hezamu.canvas.Canvas.DimensionEventListener;

import com.vaadin.Application;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CanvastestApplication extends Application {
	private static final long serialVersionUID = 6664261009817712202L;
	
	Logger logger=Logger.getLogger("test");
	
	int yPos=0;
	
	@Override
	public void init() {
		Window mainWindow = new Window("Canvas test");
		mainWindow.setSizeFull();
		mainWindow.getContent().setSizeFull();
		
		Label label = new Label("EnhancedCanvas Test Application");
		mainWindow.addComponent(label);
		
		Panel panel=new Panel();
		VerticalLayout layout=(VerticalLayout) panel.getContent();
		
		panel.setSizeFull();
		layout.setSizeFull();
		
		final BasicCanvas theCanvas=new BasicCanvas();
		layout.addComponent(theCanvas);
		layout.setExpandRatio(theCanvas, 1f);
		
		theCanvas.setName("CanvasTest");
		theCanvas.enableMouseSelectionRectangle(true);
		theCanvas.stroke();
		
		theCanvas.setSizeFull();
		theCanvas.setMinimumSize(300, 400);
		
		theCanvas.font("12pt Calibri");
		theCanvas.textAlign("left");
		theCanvas.setFillStyle("black");
		theCanvas.fillText("-.0123456789:;", 50,50,100);
		
		NativeButton buttonRec=new NativeButton("Add rectangle");
		buttonRec.addListener(new ClickListener(){

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
		
		mainWindow.addComponent(panel);
		((VerticalLayout)mainWindow.getContent()).setExpandRatio(panel, 1f);
		setMainWindow(mainWindow);
		
		theCanvas.addSelectionListener(new SelectionListener(){
			@Override
			public void recieveSelectionUpdate(double x, double y, double width, double height, boolean mouseMoved) {
				logger.info(x+"|"+y+"|"+width+"|"+height+"|"+mouseMoved);
			}
		});
		
		theCanvas.enableMouseMoveEventFiring();
		
		theCanvas.addListener(new DimensionEventListener() {
			@Override
			public void dimensionUpdate(float width, float height, Component component, float parentWidth, float parentHeight, int scrollTop) {
				logger.info("Canvas size: "+width+"|"+height+"|"+parentWidth+"|"+parentHeight+"|"+scrollTop);
			}
		});
	}
}
