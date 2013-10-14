package org.vaadin.hezamu.canvas.widgetset.client.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.vaadin.hezamu.canvas.widgetset.client.UUID;

import ac.uk.icl.dell.gwt.canvasfont.client.Simplex;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.ContainerResizedListener;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;


/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 * 
 * Mouse move|Rectangle drawing|Quick redraw|Export|Custom JavaScript calling support| added by David R. Damerell
 */
public class VCanvas extends Composite implements Paintable{
	
	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-canvas";

	public static final String CLICK_EVENT_IDENTIFIER = "click";

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	protected ApplicationConnection client;

	private MouseHandlingCanvas canvas;

	private int widthCache;

	private int heightCache;

	private final Map<String, CanvasGradient> gradients = new HashMap<String, CanvasGradient>();

	//TODO
	private boolean enableMouseSelectionMode=false;
	//TODO
	private UIDL lastUIDL;
	//TODO
	private StringBuffer lastDrawCommandSet=new StringBuffer();
	//TODO
	private boolean regenerateFastDraw=false;
	//TODO
	private String uuid=UUID.uuid();
	//TODO
	/** 
	 * Used to allow for communication with this component from an outer web page 
	 * (when the Vaadin application is embedded within another web page) 
	 * */
	private String name=UUID.uuid();
	//TODO
	private String SELF_PLACE_HOLDER="<SELF>";
	//TODO
	boolean mouseDown=false;
	//TODO
	boolean fireMouseMoveEvents=false;
	//TODO
	private int mouseDownPoint_x,mouseDownPoint_y;
	//TODO
	private boolean mouseMoved;
	//TODO
	private int lastXPosMove,lastYPosMove;
	//TODO	
	private int lastWidth,lastHeight;
	//TODO
	private int startX,startY;
	//TODO
	private boolean firstResize=true;
	//TODO
	private boolean resize=false;
	//TODO
	private int minimumCanvasWidth=1;
	//TODO
	private int minimumCanvasHeight=1;
	//TODO
	private int cachedScrollTop;
	//TODO
	private int cachedScrollLeft;
	//TODO
	private int scrollTopFinal;
	//TODO
	private int scrollLeftFinal;
	
	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VCanvas() {
		super();

		canvas = new MouseHandlingCanvas();
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (client == null) {
					return;
				}

				int x = event.getClientX() - getAbsoluteLeft();
				int y = event.getClientY() - getAbsoluteTop();
				//TODO
				if(enableMouseSelectionMode){
					mouseDown(x, y);
					//TODO
				}else{
					client.updateVariable(paintableId, "mx", x, false);
					client.updateVariable(paintableId, "my", y, false);
					client.updateVariable(paintableId, "event", "mousedown", true);
				}
			}
		});
		
		canvas.addMouseUpHandler(new MouseUpHandler() {
			

			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (client == null) {
					return;
				}

				int x = event.getClientX() - getAbsoluteLeft();
				int y = event.getClientY() - getAbsoluteTop();
				//TODO
				if(enableMouseSelectionMode){
					mouseUp(x, y);
					
					cachedScrollTop=getElement().getParentElement().getScrollTop();
					cachedScrollLeft=getElement().getParentElement().getScrollLeft();
				}else{
					client.updateVariable(paintableId, "mx", x, false);
					client.updateVariable(paintableId, "my", y, false);
					client.updateVariable(paintableId, "event", "mouseup", true);
				}
			}
		});
		//TODO
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (client == null) {
					return;
				}

				int x = event.getClientX() - getAbsoluteLeft();
				int y = event.getClientY() - getAbsoluteTop();
				
				if(enableMouseSelectionMode){
					mouseMove(x, y);
				}else{
					//client.updateVariable(paintableId, "mx", x, false);
					//client.updateVariable(paintableId, "my", y, false);
					//client.updateVariable(paintableId, "event", "mousemove", true);
				}
			}
		});

		initWidget(canvas.canvas); // All Composites need to call initWidget()

		setStyleName(CLASSNAME);
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		// Save reference to server connection object to be able to send
		// user interaction later
		
		this.client = client;

		// Save the UIDL identifier for the component
		paintableId = uidl.getId();
		
		//TODO
		client.updateVariable(paintableId, "event","awaiting_instructions",true);
		
		// This call should be made first. Ensure correct implementation,
		// and let the containing layout manage caption, etc.
		if (client.updateComponent(this, uidl, true)) {
			return;
		}
		//TODO
		lastUIDL=uidl;
		//TODO
		lastDrawCommandSet=new StringBuffer();
		//TODO
		regenerateFastDraw=true;
		//TODO 
		updateFromUIDL(uidl);
	}
	//TODO
	public void clear(){
		canvas.context2d.setTransform(1,0,0,1,0,0);
		canvas.context2d.clearRect(0, 0, canvas.canvas.getCanvasElement().getWidth(), canvas.canvas.getCanvasElement().getHeight());
	}
	//TODO
	public void updateFromUIDL(UIDL uidl) {
		//TODO: Only do this once
		getElement().getParentElement().getStyle().setOverflow(Overflow.AUTO);

		//TODO: Only do these once
		Element parentElement=canvas.getElement().getParentElement();
		DOM.setStyleAttribute((com.google.gwt.user.client.Element) parentElement, "width", "100%");
		DOM.setStyleAttribute((com.google.gwt.user.client.Element) parentElement, "height", "100%");
		
		clear(); // Always redraw fully
		
		//Run sizing commands first
		
		//sometimes the server sends multiple setminimumsize commands, only listen to the last one
		UIDL childMinSize=null;
		for (Iterator<Object> iter = uidl.getChildIterator(); iter.hasNext();) {
			UIDL childUIDL = (UIDL) iter.next();
			String command = childUIDL.getTag().toLowerCase();
			if(command.equals("setminimumsize")){
				childMinSize=childUIDL;
			}
		}
		
		if(childMinSize!=null){
			setMinimumSize(childMinSize);
		}
		
		UIDL child=uidl.getChildByTagName("setsizefull");
		if(child!=null){
			handleSetSizeFull();
		}
		
		child=uidl.getChildByTagName("setscroll");
		if(child!=null){
			handleSetScroll(child);
		}
		
		for (Iterator<Object> iter = uidl.getChildIterator(); iter.hasNext();) {
			UIDL childUIDL = (UIDL) iter.next();
			String command = childUIDL.getTag().toLowerCase();
			
			if (command.equals("stroke")) {
				canvas.context2d.stroke();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".stroke();");
			} else if (command.equals("restorecontext")) {
				canvas.restoreContext();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".restore();");
			} else if (command.equals("savecontext")) {
				canvas.saveContext();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".save();");
			} else if (command.equals("beginpath")) {
				canvas.context2d.beginPath();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".beginPath();");
			} else if (command.equals("closepath")) {
				canvas.context2d.closePath();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".closePath();");
			} else if (command.equals("clear")) {
				clear();
			} else if (command.equals("setglobalalpha")) {
				handleSetGlobalAlpha(childUIDL);
			} else if (command.equals("addcolorstop")) {
				handleAddColorStop(childUIDL);
			} else if (command.equals("setglobalcompositeoperation")) {
				handleSetGlobalCompositeOperation(childUIDL);
			} else if (command.equals("setstrokecolor")) {
				handleStrokeColorCommand(childUIDL);
			} else if (command.equals("setbackgroundcolor")) {
				handleBackgroundColorCommand(childUIDL);
			} else if (command.equals("translate")) {
				handleTranslateCommand(childUIDL);
			} else if (command.equals("scale")) {
				handleScaleCommand(childUIDL);
			} else if (command.equals("arc")) {
				handleArcCommand(childUIDL);
			} else if (command.equals("createlineargradient")) {
				handleCreateLinearGradient(childUIDL);
			} else if (command.equals("createradialgradient")) {
				handleCreateRadialGradient(childUIDL);
			} else if (command.equals("cubiccurveto")) {
				handleCubicCurveTo(childUIDL);
			} else if (command.equals("drawimage1")) {
				handleDrawImage1(childUIDL);
			} else if (command.equals("drawimage2")) {
				handleDrawImage2(childUIDL);
			} else if (command.equals("drawimage3")) {
				handleDrawImage3(childUIDL);
			} else if (command.equals("fill")) {
				canvas.context2d.fill();
				lastDrawCommandSet.append(SELF_PLACE_HOLDER+".fill();");
			} else if (command.equals("fillrect")) {
				handleFillRect(childUIDL);
			} else if (command.equals("lineto")) {
				handleLineTo(childUIDL);
			} else if (command.equals("moveto")) {
				handleMoveTo(childUIDL);
			} else if (command.equals("quadraticcurveto")) {
				handleQuadraticCurveTo(childUIDL);
			} else if (command.equals("rect")) {
				handleRect(childUIDL);
			} else if (command.equals("rotate")) {
				handleRotate(childUIDL);
			} else if (command.equals("setgradientfillstyle")) {
				handleSetGradientFillStyle(childUIDL);
			} else if (command.equals("setfillstyle")) {
				handleSetFillStyle(childUIDL);
			} else if (command.equals("setlinecap")) {
				handleSetLineCap(childUIDL);
			} else if (command.equals("setlinekoin")) {
				handleSetLineJoin(childUIDL);
			} else if (command.equals("setlinewidth")) {
				handleSetLineWidth(childUIDL);
			} else if (command.equals("setmiterlimit")) {
				handleSetMiterLimit(childUIDL);
			} else if (command.equals("setcolorstrokestyle")) {
				handleSetColorStrokeStyle(childUIDL);
			} else if (command.equals("setgradientstrokestyle")) {
				handleSetGradientStrokeStyle(childUIDL);
			} else if (command.equals("strokerect")) {
				handleStrokeRect(childUIDL);
			} else if (command.equals("transform")) {
				handleTransform(childUIDL);
			} else if (command.equals("textto")) {
				handleTextTo(childUIDL);
			}else if (command.equals("texttoangle")) {
				handleTextToAngle(childUIDL);
			}else if (command.equals("mouseselectionmode")){
				handleMouseSelectionMode(childUIDL);
			}else if(command.equals("getcurrentheight")){
				//getCurrentHeight();
			}else if(command.equals("exportresponse")){
				handleExportResponse(childUIDL);
			}else if(command.equals("changename")){
				handleNameChange(childUIDL);
			}else if(command.equals("nativesetfont")){
				handleSetFont(childUIDL);
			}else if(command.equals("nativetextalign")){
				handleSetTextAlign(childUIDL);
			}else if(command.equals("nativefilltext")){
				handleFillText(childUIDL);
			}else if(command.equals("nativefilltextmaxwidth")){
				handleFillTextMaxWidth(childUIDL);
			}else if(command.equals("setfiremousemoveevents")){
				fireMouseMoveEvents=childUIDL.getBooleanAttribute("fire");
			}else if(command.equals("getsize")){
				handleGetSize();
			}else {
				System.err.println("Invalid command: " + command);
			}
		}
		
//		else if(command.equals("setminimumsize")){
//			setMinimumSize(childUIDL);
//		}else if(command.equals("setscroll")){
//			handleSetScroll(childUIDL);
//		}else if(command.equals("setsizefull")){
//			handleSetSizeFull();
//		}
		
		if(lastDrawCommandSet.toString().length()>0){
			regenerateFastDraw=true;
			
			redraw();
		}
	}
	
	private void handleSetSizeFull() {
		getElement().getParentElement().getStyle().setWidth(100, Unit.PCT);
		getElement().getParentElement().getStyle().setHeight(100, Unit.PCT);
	}

	public void handleSetScroll(final UIDL uidl){
		int scrollTop=uidl.getIntAttribute("scrollTop");
		if(scrollTop==-1){
			scrollTop=cachedScrollTop;
		}
		
		scrollTopFinal=scrollTop;
		
		int scrollLeft=uidl.getIntAttribute("scrollLeft");
		if(scrollLeft==-1){
			scrollLeft=cachedScrollLeft;
		}
		
		scrollLeftFinal=scrollLeft;
		
		int scrollTopNow=getElement().getParentElement().getScrollTop();

		if(scrollTopNow!=scrollTopFinal){
			getElement().getParentElement().setScrollTop(scrollTopFinal);
			
		}
		
		int scrollLeftNow=getElement().getParentElement().getScrollLeft();

		if(scrollLeftNow!=scrollLeftFinal){
			getElement().getParentElement().setScrollLeft(scrollLeftFinal);
		}
		
		//this is a bit crude and might suffer from a race condition (although I haven't seen any direct evidence for one)
//		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
//			@Override
//			public boolean execute() {
//				int scrollTopNow=getElement().getParentElement().getScrollTop();
//
//				if(scrollTopNow!=scrollTopFinal){
//					getElement().getParentElement().setScrollTop(scrollTopFinal);
//					
//					return false;
//				}else{
//					return true;
//				}
//			}
//		}, 1);
//		
//		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
//			@Override
//			public boolean execute() {
//				int scrollLeftNow=getElement().getParentElement().getScrollLeft();
//
//				if(scrollLeftNow!=scrollLeftFinal){
//					getElement().getParentElement().setScrollLeft(scrollLeftFinal);
//					return false;
//					
//					
//				}else{
//					return true;
//				}
//			}
//		}, 1);		
	}
	
	private void setMinimumSize(UIDL childUIDL){
		minimumCanvasWidth=childUIDL.getIntAttribute("width");
		minimumCanvasHeight=childUIDL.getIntAttribute("height");
		
		updateDimensions();
	}
	
	Simplex simplex = new Simplex();

	private ImageData imageData;

	private int lastCanvasWidth;

	private int lastCanvasHeight;

	private Canvas bufferCanvas=Canvas.createIfSupported();

	private void handleTextToAngle(UIDL childUIDL) {
		String text=childUIDL.getStringAttribute("text");
		double x=childUIDL.getDoubleAttribute("x");
		double y=childUIDL.getDoubleAttribute("y");
		double angle=childUIDL.getDoubleAttribute("angle");
		double scale=childUIDL.getDoubleAttribute("scale");
		
		canvas.setFont(simplex);
        canvas.textTo(text, x, y, angle, scale);
	}
	
	private void handleMouseSelectionMode(UIDL childUIDL){
		enableMouseSelectionMode=Boolean.parseBoolean(childUIDL.getStringAttribute("enable"));
	}

	private void handleTextTo(UIDL childUIDL) {
		String text=childUIDL.getStringAttribute("text");
		double x=childUIDL.getDoubleAttribute("x");
		double y=childUIDL.getDoubleAttribute("y");

		canvas.setFont(simplex);
        canvas.textTo(text, x, y,0,1);
	}
	
//	  context.font = "30pt Calibri";
//	    context.textAlign = "center";
//	    context.fillStyle = "blue";
//	    context.fillText("Hello World!", x, y);
	
	private void handleSetFont(UIDL childUIDL){
		String font=childUIDL.getStringAttribute("font");
		
		//context.font = "30pt Calibri";
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".font=\""+font+"\";");
		
//		canvas.context2d.beginPath();
//		canvas.context2d.setStrokeStyle("blue");
//		canvas.context2d.moveTo(0, 0);
//		canvas.context2d.lineTo(10, 0);
//		canvas.context2d.stroke();
//		canvas.context2d.closePath();
		
		canvas.context2d.setFont(font);
	}
	
	private void handleSetTextAlign(UIDL childUIDL){
		String textAlign=childUIDL.getStringAttribute("textalign");
		
//		canvas.context2d.beginPath();
//		canvas.context2d.setStrokeStyle("red");
//		canvas.context2d.moveTo(0, 0);
//		canvas.context2d.lineTo(10, 0);
//		canvas.context2d.stroke();
//		canvas.context2d.closePath();
		
		//context.textAlign = "center";
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".textAlign=\""+textAlign+"\";");
		canvas.context2d.setTextAlign(textAlign);
	}
	
	private void handleFillText(UIDL childUIDL){
		String text=childUIDL.getStringAttribute("text");
		float x=childUIDL.getFloatAttribute("x");
		float y=childUIDL.getFloatAttribute("y");
		
//		canvas.context2d.beginPath();
//		canvas.context2d.setStrokeStyle("green");
//		canvas.context2d.moveTo(0, 0);
//		canvas.context2d.lineTo(10, 0);
//		canvas.context2d.stroke();
//		canvas.context2d.closePath();
		
		//context.fillText("Hello World!", x, y);
		
		if(childUIDL.hasAttribute("center_x")){
			double width=canvas.context2d.measureText(text).getWidth();
			x=(float) (x-(width/2f));
		}else if(childUIDL.hasAttribute("offset_x_by_width")){
			double width=canvas.context2d.measureText(text).getWidth();
			x=(float) (x-width);
		}
		
		int xI=(int)x;
		int yI=(int)y;
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".fillText(\""+text+"\","+xI+","+yI+");");
		
		canvas.context2d.fillText(text, xI, yI);
	}
	
	private void handleFillTextMaxWidth(UIDL childUIDL){
		String text=childUIDL.getStringAttribute("text");
		@SuppressWarnings("unused")
		Set<String> names=childUIDL.getAttributeNames();
		
		@SuppressWarnings("unused")
		int a=names.size();
		
		float x=childUIDL.getFloatAttribute("x");
		float y=childUIDL.getFloatAttribute("y");
		float maxWidth=childUIDL.getFloatAttribute("maxwidth");
		
		//context.fillText("Hello World!", x, y);
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".fillText(\""+text+"\","+x+","+y+","+maxWidth+");");
		
		canvas.context2d.fillText(text, x, y,maxWidth);
	}

	private void handleAddColorStop(UIDL uidl) {
		String gradientName = uidl.getStringAttribute("gradient");
		if (!gradients.containsKey(gradientName)) {
			System.err
					.println("handleAddColorStop: Gradient not foud with name "
							+ gradientName);
			return;
		}

		double offset = uidl.getDoubleAttribute("offset");
		String color = uidl.getStringAttribute("color");
		gradients.get(gradientName).addColorStop(offset, color);
	}

	private void handleSetGlobalCompositeOperation(UIDL uidl) {
		String mode = uidl.getStringAttribute("mode");
		if (mode.equalsIgnoreCase("DESTINATION_OVER")) {
			canvas.context2d.setGlobalCompositeOperation(Context2d.Composite.DESTINATION_OVER);
		} else if (mode.equalsIgnoreCase("SOURCE_OVER")) {
			canvas.context2d.setGlobalCompositeOperation(Context2d.Composite.SOURCE_OVER);
		} else {
			System.err.println("Invalid composition mode: " + mode);
		}
	}

	private void handleSetGlobalAlpha(UIDL uidl) {
		double alpha = uidl.getDoubleAttribute("alpha");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".globalAlpha="+alpha+";");

		canvas.context2d.setGlobalAlpha(alpha);
	}

	private void handleCreateLinearGradient(UIDL uidl) {
		String name = uidl.getStringAttribute("name");
		double x0 = uidl.getDoubleAttribute("x0");
		double y0 = uidl.getDoubleAttribute("y0");
		double x1 = uidl.getDoubleAttribute("x1");
		double y1 = uidl.getDoubleAttribute("y1");

		CanvasGradient newGradient = canvas.context2d
				.createLinearGradient(x0, y0, x1, y1);

		gradients.put(name, newGradient);
	}

	private void handleCreateRadialGradient(UIDL uidl) {
		String name = uidl.getStringAttribute("name");
		double x0 = uidl.getDoubleAttribute("x0");
		double y0 = uidl.getDoubleAttribute("y0");
		double r0 = uidl.getDoubleAttribute("r0");
		double x1 = uidl.getDoubleAttribute("x1");
		double y1 = uidl.getDoubleAttribute("y1");
		double r1 = uidl.getDoubleAttribute("r1");

		CanvasGradient newGradient = canvas.context2d.createRadialGradient(x0, y0, r0,
				x1, y1, r1);

		gradients.put(name, newGradient);
	}

	private void handleCubicCurveTo(UIDL uidl) {
		double cp1x = uidl.getDoubleAttribute("cp1x");
		double cp1y = uidl.getDoubleAttribute("cp1y");
		double cp2x = uidl.getDoubleAttribute("cp2x");
		double cp2y = uidl.getDoubleAttribute("cp2y");
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");

		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".bezierCurveTo("+cp1x+", "+cp1y+", "+cp2x+", "+cp2y+", "+x+","+y+");");
		
		//canvas.context2d.moveTo(cp1x, cp1y);
		canvas.context2d.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
		
		//canvas.context2d.cubicCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
	}

	private void handleDrawImage1(UIDL uidl) {
		String url = uidl.getStringAttribute("url");
		double offsetX = uidl.getDoubleAttribute("offsetX");
		double offsetY = uidl.getDoubleAttribute("offsetY");

		// Canvas.drawImage won't show the image unless it's already loaded
		Image.prefetch(url);

		Image image = new Image(url);

		canvas.context2d.drawImage(ImageElement.as(image.getElement()), offsetX, offsetY);
	}

	private void handleDrawImage2(UIDL uidl) {
		String url = uidl.getStringAttribute("url");
		double offsetX = uidl.getDoubleAttribute("offsetX");
		double offsetY = uidl.getDoubleAttribute("offsetY");
		double height = uidl.getDoubleAttribute("height");
		double width = uidl.getDoubleAttribute("width");

		// Canvas.drawImage won't show the image unless it's already loaded
		Image.prefetch(url);

		Image image = new Image(url);

		canvas.context2d.drawImage(ImageElement.as(image.getElement()), offsetX, offsetY,
				height, width);
	}

	private void handleDrawImage3(UIDL uidl) {
		String url = uidl.getStringAttribute("url");
		double sourceX = uidl.getDoubleAttribute("sourceX");
		double sourceY = uidl.getDoubleAttribute("sourceY");
		double sourceHeight = uidl.getDoubleAttribute("sourceHeight");
		double sourceWidth = uidl.getDoubleAttribute("sourceWidth");
		double destX = uidl.getDoubleAttribute("destX");
		double destY = uidl.getDoubleAttribute("destY");
		double destHeight = uidl.getDoubleAttribute("destHeight");
		double destWidth = uidl.getDoubleAttribute("destWidth");

		// Canvas.drawImage won't show the image unless it's already loaded
		Image.prefetch(url);

		Image image = new Image(url);

		canvas.context2d.drawImage(ImageElement.as(image.getElement()), sourceX, sourceY,
				sourceHeight, sourceWidth, destX, destY, destHeight, destWidth);
	}

	private void handleFillRect(UIDL uidl) {
		double startX = uidl.getDoubleAttribute("startX");
		double startY = uidl.getDoubleAttribute("startY");
		double width = uidl.getDoubleAttribute("width");
		double height = uidl.getDoubleAttribute("height");

		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".fillRect("+startX+","+startY+","+width+","+height+");");
		
		canvas.context2d.fillRect(startX, startY, width, height);
	}

	private void handleLineTo(UIDL uidl) {
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".lineTo("+x+","+y+");");

		canvas.context2d.lineTo(x, y);
	}

	private void handleMoveTo(UIDL uidl) {
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".moveTo("+x+","+y+");");

		canvas.context2d.moveTo(x, y);
	}

	private void handleQuadraticCurveTo(UIDL uidl) {
		double cpx = uidl.getDoubleAttribute("cpx");
		double cpy = uidl.getDoubleAttribute("cpy");
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");

		canvas.context2d.quadraticCurveTo(cpx, cpy, x, y);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".quadraticCurveTo("+cpx+","+cpy+","+x+","+y+");");
	}

	private void handleRect(UIDL uidl) {
		double startX = uidl.getDoubleAttribute("startX");
		double startY = uidl.getDoubleAttribute("startY");
		double width = uidl.getDoubleAttribute("width");
		double height = uidl.getDoubleAttribute("height");

		canvas.context2d.rect(startX, startY, width, height);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".rect("+startX+","+startY+","+width+","+height+");");
	}

	private void handleRotate(UIDL uidl) {
		double angle = uidl.getDoubleAttribute("angle");

		canvas.context2d.rotate(angle);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".rotate("+angle+");");
	}

	private void handleSetGradientFillStyle(UIDL uidl) {
		String gradientName = uidl.getStringAttribute("gradient");

		if (gradients.containsKey(gradientName)) {
			canvas.context2d.setFillStyle(gradients.get(gradientName));
		} else {
			System.out
					.println("handleSetGradientFillStyle: Gradient not foud with name "
							+ gradientName);
		}
	}

	private void handleSetFillStyle(UIDL uidl) {
		String color = uidl.getStringAttribute("color");

		if (color.equalsIgnoreCase("transparent")) {
			//FillStrokeStyle style=FillStrokeStyle.
			canvas.context2d.setFillStyle("rgba(255,255,255,0)");
		} else {
			canvas.context2d.setFillStyle(color);
			lastDrawCommandSet.append(SELF_PLACE_HOLDER+".fillStyle=\""+color+"\";");
		}
	}

	private void handleSetLineCap(UIDL uidl) {
		String lineCap = uidl.getStringAttribute("lineCap");

		canvas.context2d.setLineCap(lineCap);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".lineCap="+lineCap+";");
	}

	private void handleSetLineJoin(UIDL uidl) {
		String lineJoin = uidl.getStringAttribute("lineJoin");

		canvas.context2d.setLineJoin(lineJoin);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".lineJoin="+lineJoin+";");
	}

	private void handleSetLineWidth(UIDL uidl) {
		double width = uidl.getDoubleAttribute("width");

		canvas.context2d.setLineWidth(width);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".lineWidth="+width+";");
	}

	private void handleSetMiterLimit(UIDL uidl) {
		double miterLimit = uidl.getDoubleAttribute("miterLimit");

		canvas.context2d.setMiterLimit(miterLimit);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".miterLimit="+miterLimit+";");
	}

	private void handleSetGradientStrokeStyle(UIDL uidl) {
		String gradientName = uidl.getStringAttribute("gradient");

		if (gradients.containsKey(gradientName)) {
			canvas.context2d.setStrokeStyle(gradients.get(gradientName));
		} else {
			System.out
					.println("handleSetStrokeStyle: Gradient not found with name "
							+ gradientName);
		}
	}

	private void handleSetColorStrokeStyle(UIDL uidl) {
		String color = uidl.getStringAttribute("color");

		canvas.context2d.setStrokeStyle(color);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".strokeStyle=\""+color+"\";");
	}

	private void handleStrokeRect(UIDL uidl) {
		double startX = uidl.getDoubleAttribute("startX");
		double startY = uidl.getDoubleAttribute("startY");
		double width = uidl.getDoubleAttribute("width");
		double height = uidl.getDoubleAttribute("height");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".strokeRect("+startX+","+startY+","+width+","+height+");");

		canvas.context2d.strokeRect(startX, startY, width, height);
	}

	private void handleTransform(UIDL uidl) {
		double m11 = uidl.getDoubleAttribute("m11");
		double m12 = uidl.getDoubleAttribute("m12");
		double m21 = uidl.getDoubleAttribute("m21");
		double m22 = uidl.getDoubleAttribute("m22");
		double dx = uidl.getDoubleAttribute("dx");
		double dy = uidl.getDoubleAttribute("dy");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".transform("+m11+","+m12+","+m21+","+m22+","+dx+","+dy+");");

		canvas.context2d.transform(m11, m12, m21, m22, dx, dy);
	}

	private void handleBackgroundColorCommand(UIDL uidl) {
		String rgb = uidl.getStringAttribute("rgb");

		
		DOM.setStyleAttribute(canvas.canvas.getElement(),"backgroundColor",rgb);
	}

	private void handleStrokeColorCommand(UIDL uidl) {
		String rgb = uidl.getStringAttribute("rgb");

		canvas.context2d.setStrokeStyle(rgb);
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".strokeStyle=\""+rgb+"\";");
	}

	private void handleArcCommand(UIDL uidl) {
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");
		double radius = uidl.getDoubleAttribute("radius");
		double startAngle = uidl.getDoubleAttribute("startAngle");
		double endAngle = uidl.getDoubleAttribute("endAngle");
		boolean antiClockwise = uidl.getBooleanAttribute("antiClockwise");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".arc("+x+","+y+","+radius+","+startAngle+","+endAngle+","+antiClockwise+");");

		canvas.context2d.arc(x, y, radius, startAngle, endAngle, antiClockwise);
	}

	private void handleTranslateCommand(UIDL uidl) {
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");
		
		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".translate("+x+","+y+");");

		canvas.context2d.translate(x, y);
	}

	private void handleScaleCommand(UIDL uidl) {
		double x = uidl.getDoubleAttribute("x");
		double y = uidl.getDoubleAttribute("y");

		lastDrawCommandSet.append(SELF_PLACE_HOLDER+".scale("+x+","+y+");");
		
		canvas.context2d.scale(x, y);
	}

	@Override
	public void setWidth(String width) {		
		super.setWidth(width);
		
		updateDimensions();
	}
	
	protected void updateDimensions(){
		boolean repaint=false;
		
		int parentWidth=getElement().getParentElement().getClientWidth(); //width of holding div
		int width=getElement().getClientWidth(); //should be canvas.style.width (I think)
		
		if(minimumCanvasWidth>parentWidth){ //is minimum canvas width > current canvas width (must rely on parent width)
			canvas.setWidth(minimumCanvasWidth+"px");
			canvas.canvas.setCoordinateSpaceWidth(minimumCanvasWidth);
			
			lastCanvasWidth=minimumCanvasWidth;
			
			repaint=true;
		}else if(width!=parentWidth){ //is the current canvas width equal to the parent width (if not reset to parent)
			canvas.setWidth(parentWidth+"px");
			canvas.canvas.setCoordinateSpaceWidth(parentWidth);
			
			lastCanvasWidth=parentWidth;
			repaint=true;
		}else if(width!=canvas.canvas.getCoordinateSpaceWidth()){ //is the canvas.width property equal to the canvas.style.width
			canvas.canvas.setCoordinateSpaceWidth(width);
			
			lastCanvasWidth=width;
			repaint=true;
		}
		
		//see comments above
		int parentHeight=getElement().getParentElement().getClientHeight();
		int height=getElement().getClientHeight();
		
		if(minimumCanvasHeight>parentHeight){
			canvas.setHeight(minimumCanvasHeight+"px");
			canvas.canvas.setCoordinateSpaceHeight(minimumCanvasHeight);
			repaint=true;
			
			lastCanvasHeight=minimumCanvasHeight;
		}else if(height!=parentHeight){
			canvas.setHeight(parentHeight+"px");
			canvas.canvas.setCoordinateSpaceHeight(parentHeight);
			repaint=true;
			
			lastCanvasHeight=parentHeight;
		}else if(height!=canvas.canvas.getCoordinateSpaceHeight()){
			canvas.canvas.setCoordinateSpaceHeight(height);
			repaint=true;
			
			lastCanvasHeight=height;
		}
		
		if(repaint){
			getElement().getParentElement().setScrollLeft(scrollLeftFinal);
			getElement().getParentElement().setScrollTop(scrollTopFinal);
			
			clear();
			
			if (client != null && lastDrawCommandSet.length()>0) {
				redraw();
			}			
		}
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		
		updateDimensions();
	}
	
	public void handleGetSize(){
		client.updateVariable(paintableId, "clientHeight", String.valueOf(super.getWidget().getElement().getClientHeight()), false);
		client.updateVariable(paintableId, "clientWidth", String.valueOf(super.getWidget().getElement().getClientWidth()), false);
		client.updateVariable(paintableId, "clientParentHeight", String.valueOf(super.getWidget().getElement().getParentElement().getClientHeight()), false);
		client.updateVariable(paintableId, "clientParentWidth", String.valueOf(super.getWidget().getElement().getParentElement().getClientWidth()), false);
		client.updateVariable(paintableId, "clientScrollTop", String.valueOf(getElement().getParentElement().getScrollTop()), false);
			
		client.updateVariable(paintableId, "request", "getsize", true);
	}
	public void mouseDown(int x, int y) {
		mouseDownPoint_x=x;
		mouseDownPoint_y=y;
		lastXPosMove=x;
		lastYPosMove=y;
		mouseDown=true;
		startX=x;
		startY=y;
	}

    /**
     * 
     * @param x
     * @param y
     * @author David R. Damerell
     */
	public void mouseMove(int x, int y) {
		if(mouseDown && ( (x-lastXPosMove >10 || x-lastXPosMove < -10) || ((y-lastYPosMove >10 || y-lastYPosMove < -10)))){
			mouseMoved=true;
			lastXPosMove=x;
			lastYPosMove=y;
			clear();
			
			int x1,y1,width,height;
			
			if(mouseDownPoint_x > x){
				x1=x;
				width=mouseDownPoint_x-x1;
			}else{
				x1=mouseDownPoint_x;
				width=x-x1;
			}
			
			if(mouseDownPoint_y > y){
				y1=y;
				height=mouseDownPoint_y-y1;
			}else{
				y1=mouseDownPoint_y;
				height=y-y1;
			}
			
			lastWidth=width;
			lastHeight=height;
			
			redraw();
			
			canvas.context2d.strokeRect(x1, y1, width, height);
		}else if(fireMouseMoveEvents){
			client.updateVariable(paintableId, "mx", x, false);
			client.updateVariable(paintableId, "my", y, false);
			client.updateVariable(paintableId, "lastwidth", 0, false);
			client.updateVariable(paintableId, "lastheight", 0, false);
			client.updateVariable(paintableId, "mousemoved", true, false);
			
			client.updateVariable(paintableId, "event", "mousemoveselection", true);
		}
	}
	
	/**
	 * @author David R. Damerell
	 */
	private void redraw(){
		if(regenerateFastDraw){
			regenerateFastDraw=false;
			
//			say("redrawing: "+lastCanvasHeight);
			
			imageData=canvas.context2d.getImageData(0, 0, lastCanvasWidth, lastCanvasHeight);
		
			//slow
			bufferCanvas.setCoordinateSpaceHeight(lastCanvasHeight);
			bufferCanvas.setCoordinateSpaceWidth(lastCanvasWidth);
			bufferCanvas.getContext2d().putImageData(imageData, 0, 0);
		}
		
		//fast
		canvas.context2d.drawImage(bufferCanvas.getCanvasElement(), 0, 0);
		
		//canvas.context2d.putImageData(imageData, 0, 0);
	}

	public void mouseUp(int x, int y) {
		if(mouseMoved){
			clear();
			redraw();
		}
		
		System.err.println("Mouse up!");
		
		x=startX > x ? x: startX;
		y=startY > y ? y: startY;
		
		mouseDown=false;
		
		client.updateVariable(paintableId, "mx", x, false);
		client.updateVariable(paintableId, "my", y, false);
		client.updateVariable(paintableId, "lastwidth", lastWidth, false);
		client.updateVariable(paintableId, "lastheight", lastHeight, false);
		client.updateVariable(paintableId, "mousemoved", mouseMoved, false);
		client.updateVariable(paintableId, "event", "mousemoveselection", true);
		
		mouseMoved=false;
	}
	
	/**
	 * var impl=canvas.@com.google.gwt.widgetideas.graphics.client.GWTCanvas::impl;
		var canvasContext=impl.@com.google.gwt.widgetideas.graphics.client.impl.GWTCanvasImplDefault::canvasContext;
		eval(canvasCommands);
		
		var canvasContext=canvas.@com.google.gwt.canvas.client.Canvas::getContext2d();
		eval(canvasCommands);
	 * @param canvasCommands
	 * @param canvas
	 * @author David R. Damerell
	 */
	public native void runCachedCanvasCommands(String canvasCommands,com.google.gwt.canvas.client.Canvas canvas)/*-{
		var canvasContext=canvas.@com.google.gwt.canvas.client.Canvas::getContext2d();
		eval(canvasCommands);
	}-*/;
	
	/**
	 * 
	 * @param uidl
	 * @author David R. Damerell
	 */
	public void handleNameChange(UIDL uidl){
		String name=uidl.getStringAttribute("name");
		
		this.name=name;
		publish(this,this.name);
	}
	
	/**
	 *     $wnd[""+name+""]=instance;
	    
	    $wnd[""+name+""].canvas=instance.@org.vaadin.hezamu.canvas.widgetset.client.ui.VCanvas::canvas;
	    
	    var impl=$wnd[""+name+""].canvas.@com.google.gwt.widgetideas.graphics.client.GWTCanvas::impl;
		var canvasContext=impl.@com.google.gwt.widgetideas.graphics.client.impl.GWTCanvasImplDefault::canvasContext;
	    
	    $wnd[""+name+""].canvasContext=canvasContext;
	    
	
	  	var exportAs=function(type,callBackJS) {
        	return instance.@org.vaadin.hezamu.canvas.widgetset.client.ui.VCanvas::exportCanvas(Ljava/lang/String;Ljava/lang/String;) (type,callBackJS);
    	};
    	
    	$wnd[""+name+""].runCommand=exportAs;
    	
    	////////
    	
    	
	 * 
	 * 
	 * @param instance
	 * @param name
	 * @author David R. Damerell
	 */
	public native void publish(VCanvas instance,String name)/*-{
	    $wnd[""+name+""]=instance;
	    
	    $wnd[""+name+""].canvas=instance.@org.vaadin.hezamu.canvas.widgetset.client.ui.VCanvas::canvas;
	    
		var canvasContext=$wnd[""+name+""].canvas.@ac.uk.icl.dell.gwt.canvas.client.ClientSideFontCanvas::context2d;
	    
	    $wnd[""+name+""].canvasContext=canvasContext;
	    
	
	  	var exportAs=function(type,callBackJS) {
        	return instance.@org.vaadin.hezamu.canvas.widgetset.client.ui.VCanvas::exportCanvas(Ljava/lang/String;Ljava/lang/String;) (type,callBackJS);
    	};
    	
    	$wnd[""+name+""].runCommand=exportAs;
  	}-*/;
	
	public String callBack;
	
	/**
	 * 
	 * @param type
	 * @param callBackJS
	 * @author David R. Damerell
	 */
	public void exportCanvas(String type, String callBackJS){
		callBack=callBackJS;	
		client.updateVariable(paintableId, "type", type, false);
		client.updateVariable(paintableId, "event", "export", true);
    }
	
	/**
	 * 
	 * @param uidl
	 * @author David R. Damerell
	 */
	private void handleExportResponse(UIDL uidl){
		String response=uidl.getStringAttribute("response");
		runUserExportAction(callBack,response);
	}
	
	/**
	 * @param callBack
	 * @param response
	 * @author David R. Damerell
	 */
	public native void runUserExportAction(String callBack,String response)/*-{
		callBack.run(response);
	}-*/;
	
	/**
	 * @param s
	 * @author David R. Damerell
	 */
	public native void say(String s)/*-{
		alert(s);
	}-*/;
}
