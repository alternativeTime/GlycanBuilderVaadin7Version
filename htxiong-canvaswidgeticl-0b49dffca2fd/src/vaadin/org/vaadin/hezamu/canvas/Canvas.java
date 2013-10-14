package org.vaadin.hezamu.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Server side component for the VCanvas widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.hezamu.canvas.widgetset.client.ui.VCanvas.class)
public class Canvas extends AbstractComponent {
	private static final long serialVersionUID = -5388297546218777306L;

	public static final String BEVEL = "BEVEL";
	public static final String BUTT = "BUTT";
	public static final String DESTINATION_OVER = "DESTINATION_OVER";
	public static final String SOURCE_OVER = "SOURCE_OVER";
	public static final String MITER = "MITER";
	public static final String TRANSPARENT = "TRANSPARENT";
	public static final String ROUND = "ROUND";
	public static final String SQUARE = "SQUARE";

	private final List<Map<String, Object>> commands = new ArrayList<Map<String, Object>>();

	private final List<CanvasMouseDownListener> downListeners = new ArrayList<CanvasMouseDownListener>();

	private final List<CanvasMouseUpListener> upListeners = new ArrayList<CanvasMouseUpListener>();
	
	//TODO
	private final List<CanvasMouseMoveListener> moveListeners = new ArrayList<CanvasMouseMoveListener>();
	//TODO
	private HashSet<Object> drawingCommands=new HashSet<Object>();
	//TODO
	private final List<Map<String, Object>> oneRunCommands = new ArrayList<Map<String, Object>>();
	
	public void createLinearGradient(String name, double x0, double y0,
			double x1, double y1) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "createLinearGradient");
		arguments.put("name", name);
		arguments.put("x0", x0);
		arguments.put("y0", y0);
		arguments.put("x1", x1);
		arguments.put("y1", y1);

		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint(); 
	}

	public void createRadialGradient(String name, double x0, double y0,
			double r0, double x1, double y1, double r1) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "createRadialGradient");
		arguments.put("name", name);
		arguments.put("x0", x0);
		arguments.put("y0", y0);
		arguments.put("r0", r0);
		arguments.put("x1", x1);
		arguments.put("y1", y1);
		arguments.put("r1", r1);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);
		
		requestRepaint();
	}

	public void cubicCurveTo(double cp1x, double cp1y, double cp2x,
			double cp2y, double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "cubicCurveTo");
		arguments.put("cp1x", cp1x);
		arguments.put("cp1y", cp1y);
		arguments.put("cp2x", cp2x);
		arguments.put("cp2y", cp2y);
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void drawImage(String url, double offsetX, double offsetY) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "drawImage1");
		arguments.put("url", url);
		arguments.put("offsetX", offsetX);
		arguments.put("offsetY", offsetY);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void drawImage(String url, double offsetX, double offsetY,
			double width, double height) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "drawImage2");
		arguments.put("url", url);
		arguments.put("offsetX", offsetX);
		arguments.put("offsetY", offsetY);
		arguments.put("width", width);
		arguments.put("height", height);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void drawImage(String url, double sourceX, double sourceY,
			double sourceWidth, double sourceHeight, double destX,
			double destY, double destWidth, double destHeight) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "drawImage3");
		arguments.put("url", url);
		arguments.put("sourceX", sourceX);
		arguments.put("sourceY", sourceY);
		arguments.put("sourceWidth", sourceWidth);
		arguments.put("sourceHeight", sourceHeight);
		arguments.put("destX", destX);
		arguments.put("destY", destY);
		arguments.put("destWidth", destWidth);
		arguments.put("destHeight", destHeight);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void fill() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "fill");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void fillRect(double startX, double startY, double width,
			double height) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "fillRect");
		arguments.put("startX", startX);
		arguments.put("startY", startY);
		arguments.put("width", width);
		arguments.put("height", height);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void lineTo(double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "lineTo");
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void moveTo(double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "moveTo");
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void quadraticCurveTo(double cpx, double cpy, double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "quadraticCurveTo");
		arguments.put("cpx", cpx);
		arguments.put("cpy", cpy);
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void rect(double startX, double startY, double width, double height) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "rect");
		arguments.put("startX", startX);
		arguments.put("startY", startY);
		arguments.put("width", width);
		arguments.put("height", height);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);
		
		requestRepaint();
	}

	public void rotate(double angle) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "rotate");
		arguments.put("angle", angle);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);
		
		requestRepaint();
	}

	public void setGradientFillStyle(String gradient) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setGradientFillStyle");
		arguments.put("gradient", gradient);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setFillStyle(String color) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setFillStyle");
		arguments.put("color", color);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setLineCap(String lineCap) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setLineCap");
		arguments.put("lineCap", lineCap);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);
		
		requestRepaint();
	}

	public void setLineJoin(String lineJoin) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setLineJoin");
		arguments.put("lineJoin", lineJoin);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setLineWidth(double width) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setLineWidth");
		arguments.put("width", width);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setMiterLimit(double miterLimit) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setMiterLimit");
		arguments.put("miterLimit", miterLimit);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setGradientStrokeStyle(String gradient) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setGradientStrokeStyle");
		arguments.put("gradient", gradient);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setColorStrokeStyle(String color) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setColorStrokeStyle");
		arguments.put("color", color);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void strokeRect(double startX, double startY, double width,
			double height) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "strokeRect");
		arguments.put("startX", startX);
		arguments.put("startY", startY);
		arguments.put("width", width);
		arguments.put("height", height);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void transform(double m11, double m12, double m21, double m22,
			double dx, double dy) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "transform");
		arguments.put("m11", m11);
		arguments.put("m12", m12);
		arguments.put("m21", m21);
		arguments.put("m22", m22);
		arguments.put("dx", dx);
		arguments.put("dy", dy);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void arc(double x, double y, double radius, double startAngle,
			double endAngle, boolean antiClockwise) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "arc");
		arguments.put("x", x);
		arguments.put("y", y);
		arguments.put("radius", radius);
		arguments.put("startAngle", startAngle);
		arguments.put("endAngle", endAngle);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void translate(double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "translate");
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void scale(double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "scale");
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void stroke() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "stroke");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void saveContext() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "saveContext");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void restoreContext() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "restoreContext");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setBackgroundColor(String rgb) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setBackgroundColor");
		arguments.put("rgb", rgb);
		commands.add(arguments);

		requestRepaint();
	}

	public void setStrokeColor(String rgb) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setStrokeColor");
		arguments.put("rgb", rgb);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void beginPath() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "beginPath");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	/**
	 * Clear canvas (using clearRect)
	 * All previous drawing commands are removed from the command history (otherwise they are still sent to the browser).
	 * 
	 * @author David R. Damerell
	 */
	public void clear() {
		//TODO
		commands.removeAll(drawingCommands);
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "clear");
		
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void reset() {
		commands.clear();
		//TODO
		drawingCommands.clear(); //Added 08/08/11
		clear();
	}
	//TODO
	public interface DimensionEventListener {
		public void dimensionUpdate(float width, float height, Component component, float parentWidth, float parentHeight, int scrollTop);
	}
	//TODO
	protected List<DimensionEventListener> dimensionEventListeners=new ArrayList<DimensionEventListener>();
	//TODO
	private int lastMinHeight;
	//TODO
	private int lastMinWidth;
	//TODO
	private int lastScrollTop;
	//TODO
	private int lastScrollLeft;
	//TODO
	private boolean isSettingMinimumSize;
	//TODO
	private boolean isSettingScroll;
	//TODO
	public void addListener(DimensionEventListener listener){
		dimensionEventListeners.add(listener);
	}
	//TODO
	public void removeListener(DimensionEventListener listener){
		dimensionEventListeners.remove(listener);
	}
	//TODO
	public void fireDimensionEvent() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "getsize");
		
		commands.add(arguments);
		
		oneRunCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void fireParentDimensionEvent() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "getparentsize");
		
		commands.add(arguments);
		
		oneRunCommands.add(arguments);

		requestRepaint();
	}
	
	//TODO
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		List<Map<String, Object>> allCommands=new ArrayList<Map<String, Object>>();
		allCommands.addAll(commands);
		
		if(isSettingMinimumSize==false && lastMinHeight!=0){
			{
				Map<String, Object> arguments = new HashMap<String, Object>();
				arguments.put("command", "setminimumsize");
				arguments.put("width", lastMinWidth);
				arguments.put("height", lastMinHeight);

				oneRunCommands.add(arguments);
			}
			
			{
				Map<String, Object> arguments = new HashMap<String, Object>();
				arguments.put("command", "startresizethread");

				oneRunCommands.add(arguments);
			}
		}
		
		if(isSettingScroll==false){
			setScroll(lastScrollTop, lastScrollLeft);
		}
		
		allCommands.addAll(oneRunCommands);
		
		for (Map<String, Object> command : allCommands) {
			target.startTag((String) command.get("command"));

			for (String key : command.keySet()) {
				if (key.equals("command")) {
					continue; 
				}

				Object value = command.get(key);
				if (value instanceof Double) {
					target.addAttribute(key, (Double) value);
				} else if (value instanceof Integer) {
					target.addAttribute(key, (Integer) value);
				} else if(value instanceof Boolean){
					target.addAttribute(key, (Boolean) value);
				} else if(value instanceof Float){
					target.addAttribute(key, (Float) value);
				}else {					
					target.addAttribute(key, (String) value);
				}
			}

			target.endTag((String) command.get("command"));
		}
		
		oneRunCommands.clear();
		
		isSettingScroll=false;
		isSettingMinimumSize=false;
	}
	//TODO
	@SuppressWarnings("rawtypes")
	@Override
	public void changeVariables(Object source, Map variables) {
		if (variables.containsKey("sizeChanged")) {
			requestRepaint();
		} else if (variables.containsKey("event")) {
			String eventtype = (String) variables.get("event");
			Integer x = (Integer) variables.get("mx");
			Integer y = (Integer) variables.get("my");

			if (eventtype.equals("mousedown")) {
				fireMouseDown(x, y);
			} else if (eventtype.equals("mouseup")) {
				fireMouseUp(x, y);
			} else if(eventtype.equals("mousemove")) {
				fireMouseMove(x, y);
			}else {
				System.err.println("Unknown event type: " + eventtype);
			}
		} else if(variables.containsKey("request")){
			String eventtype = (String) variables.get("request");
			
			if(eventtype.equals("getsize")){
				float width=Float.parseFloat((String)variables.get("clientWidth"));
				float height=Float.parseFloat((String)variables.get("clientHeight"));
				
				float parentWidth=Float.parseFloat((String)variables.get("clientParentWidth"));
				float parentHeight=Float.parseFloat((String)variables.get("clientParentHeight"));
				
				Integer scrollTop=Integer.parseInt((String)variables.get("clientScrollTop"));
				
				for(DimensionEventListener listener:dimensionEventListeners){
					listener.dimensionUpdate(width, height, this,parentWidth,parentHeight, scrollTop);
				}
			}
		}
	}

	public void setStrokeStyle(int r, int g, int b) {
		setStrokeColor(String.format("#%02x%02x%02x", r, g, b));
	}

	public void setFillStyle(int r, int g, int b) {
		setFillStyle(String.format("#%02x%02x%02x", r, g, b));
	}

	public void setGlobalAlpha(double alpha) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setGlobalAlpha");
		arguments.put("alpha", alpha);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void closePath() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "closePath");
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void setGlobalCompositeOperation(String mode) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setGlobalCompositeOperation");
		arguments.put("mode", mode);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}

	public void addColorStop(String gradient, double offset, String color) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "addColorStop");
		arguments.put("gradient", gradient);
		arguments.put("offset", offset);
		arguments.put("color", color);
		commands.add(arguments);
		//TODO
		drawingCommands.add(arguments);

		requestRepaint();
	}
	
	//TODO
	/**
	 * Render text using the SWTBCanvasText client side implementation.
	 * Limited to A-Z 0-9 and a few other simple characters
	 * @deprecated
	 * @param text
	 * @param x
	 * @param y
	 * @author David R. Damerell
	 */
	@Deprecated
	public void textTo(String text, double x, double y) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "textTo");
		arguments.put("text", text);
		arguments.put("x", x);
		arguments.put("y", y);
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	/**
	 * Render text using the SWTBCanvasText client side implementation.
	 * Limited to A-Z 0-9 and a few other simple characters
	 * @deprecated
	 * @param text
	 * @param x
	 * @param y
	 * @param angle
	 * @param scale
	 * @author David R. Damerell
	 */
	@Deprecated
	public void textTo(String text, double x, double y, double angle, double scale){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "textToAngle");
		arguments.put("text", text);
		arguments.put("x", x);
		arguments.put("y", y);
		arguments.put("angle", angle);
		arguments.put("scale", scale);
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void font(String font){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "nativesetfont");
		arguments.put("font", font);
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void textAlign(String textAlign){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "nativetextalign");
		arguments.put("textalign", textAlign);
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void fillText(String text, float x, float y){
		fillText(text, x, y, TEXT_X_MODE.NORMAL);
	}
	//TODO
	public enum TEXT_X_MODE {
		CENTER_ON_X(),OFF_SET_BY_X(),NORMAL;
	}
	//TODO
	public void fillText(String text, float x, float y, TEXT_X_MODE xMode){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "nativefilltext");
		arguments.put("text", text);
		arguments.put("x", x);
		arguments.put("y", y);
		
		if(xMode==TEXT_X_MODE.CENTER_ON_X){
			arguments.put("center_x", true);
		}else if(xMode==TEXT_X_MODE.OFF_SET_BY_X){
			arguments.put("offset_x_by_width", true);
		}
		
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void fillText(String text, float x, float y, float maxWidth){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "nativefilltextmaxwidth");
		arguments.put("text", text);
		arguments.put("x", x);
		arguments.put("y", y);
		arguments.put("maxwidth", maxWidth);
		commands.add(arguments);
		
		drawingCommands.add(arguments);

		requestRepaint();
	}
	//TODO
	public void setScroll(int top, int left){
		lastScrollTop=top;
		lastScrollLeft=left;
		
		{
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("command", "setscroll");
			arguments.put("scrollTop", top);
			arguments.put("scrollLeft", left);

			oneRunCommands.add(arguments);
		}
		
		isSettingScroll=true;
		
		requestRepaint();
	}
	//TODO
	public void setMinimumSize(int width, int height){
		lastMinWidth=width;
		lastMinHeight=height;
		
		{
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("command", "setminimumsize");
			arguments.put("width", width);
			arguments.put("height", height);

			oneRunCommands.add(arguments);
		}
		
		{
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("command", "startresizethread");

			oneRunCommands.add(arguments);
		}
		
		isSettingMinimumSize=true;
		
		requestRepaint();
	}
	//TODO
	public void stopResizeThread(){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "stopresizethread");

		oneRunCommands.add(arguments);
		
		requestRepaint();
	}
	//TODO
	@Override
	public void setParent(Component parent){
		if(parent==null){
			stopResizeThread();
		}
		
		super.setParent(parent);
	}

	public interface CanvasMouseDownListener {
		public void mouseDown(int x, int y);
	}

	public void addListener(CanvasMouseDownListener listener) {
		if (!downListeners.contains(listener)) {
			downListeners.add(listener);
		}
	}

	public void removeListener(CanvasMouseDownListener listener) {
		if (downListeners.contains(listener)) {
			downListeners.remove(listener);
		}
	}

	private void fireMouseDown(int x, int y) {
		for (CanvasMouseDownListener listener : downListeners) {
			listener.mouseDown(x, y);
		}
	}

	public interface CanvasMouseUpListener {
		public void mouseUp(int x, int y);
	}

	public void addListener(CanvasMouseUpListener listener) {
		if (!upListeners.contains(listener)) {
			upListeners.add(listener);
		}
	}

	public void removeListener(CanvasMouseUpListener listener) {
		if (upListeners.contains(listener)) {
			upListeners.remove(listener);
		}
	}
	
	private void fireMouseUp(int x, int y) {
		for (CanvasMouseUpListener listener : upListeners) {
			listener.mouseUp(x, y);
		}
	}
	//TODO
	public interface CanvasMouseMoveListener {
		public void mouseMove(int x, int y);
	}
	//TODO
	public void addListener(CanvasMouseMoveListener listener) {
		if (!moveListeners.contains(listener)) {
			moveListeners.add(listener);
		}
	}
	//TODO
	public void removeListener(CanvasMouseMoveListener listener) {
		if (moveListeners.contains(listener)) {
			moveListeners.remove(listener);
		}
	}
	//TODO
	private void fireMouseMove(int x, int y) {
		for (CanvasMouseMoveListener listener : moveListeners) {
			listener.mouseMove(x, y);
		}
	}
	//TODO
    /**
     * Allows the user to create a click-and-drag-rectangle within the canvas
     * @param enable
     * @author David R. Damerell
     */
	public void enableMouseSelectionRectangle(boolean enable){
		Map<String, Object> arguments = new HashMap<String, Object>();
		
		arguments.put("command", "mouseselectionmode");
		arguments.put("enable",Boolean.toString(enable));
		commands.add(arguments);

		requestRepaint();
	}
	//TODO
	/**
	 * Send a response to an export request received from the browser
	 * @param exportResponse
	 * @author David R. Damerell
	 */
	public void respondToExportRequest(String exportResponse){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "exportresponse");
		arguments.put("response", exportResponse);
		oneRunCommands.add(arguments);
		
		requestRepaint();
	}
	//TODO
	/**
	 * Method causes client side component to fire mouse move events.
	 * 
	 * By default mouse moves don't trigger an event.
	 * 
	 * @author David R. Damerell
	 */
	public void enableMouseMoveEventFiring(){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setfiremousemoveevents");
		arguments.put("fire", true);
		oneRunCommands.add(arguments);
		
		requestRepaint();
	}
	//TODO
	/**
	 * Method disables the client side component from sending mouse move events.
	 * 
	 * By default mouse moves don't trigger an event.
	 * 
	 * @author David R. Damerell
	 */
	public void disableMouseMoveEventFiring(){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setfiremousemoveevents");
		arguments.put("fire", false);
		oneRunCommands.add(arguments);
		
		requestRepaint();
	}
	//TODO
	/**
	 * Setting the name allows custom JavaScript access to the canvas object, and allows for export requests
	 * @param name
	 * @author David R. Damerell
	 */
	public void setName(String name){
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "changename");
		arguments.put("name", name);
		commands.add(arguments);
		
		requestRepaint();
	}
	//TODO
	@Override
	public void setSizeFull() {
		super.setSizeFull();
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("command", "setsizefull");
		oneRunCommands.add(arguments);
		
		requestRepaint();
	}
}
