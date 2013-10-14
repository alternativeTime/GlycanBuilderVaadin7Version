package ac.uk.icl.dell.vaadin.canvas.basiccanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.hezamu.canvas.Canvas.CanvasClickListener;

import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.Font;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontCharacter;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontPoint;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontSegment;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.listeners.BasicCanvasClickListener;
import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.vaadin.shared.MouseEventDetails;

public class BasicCanvas extends Canvas implements BasicCanvasClickListener
{
	private static final long serialVersionUID = -3660156744769804081L;

	 public Font font;
     public boolean italics=false;
     
     private List<SelectionListener> selectionListeners;
     private List<ExportListener> exportListeners;
     
     public boolean awaitingInstructions=false;
     
     public BasicCanvas(){
	    	 this.addListener((BasicCanvasClickListener)this);
             
             selectionListeners=new ArrayList<SelectionListener>();
             exportListeners=new ArrayList<ExportListener>();
     }
     
     public void addExportListener(ExportListener listener){
             exportListeners.add(listener);
     }
     
     public void removeExportListener(ExportListener listener){
             exportListeners.remove(listener);
     }
     
     public interface SelectionListener{
             public void recieveSelectionUpdate(double x, double y, double width, double height,boolean mouseMoved);
     }
     
     public interface ExportListener {
             public void exportRequest(String type);
     }
     
     /**
      * 
      * @param character
      * @param x
      * @param y
      * @return
      */
     public double renderChar(char character,double x, double y){
             FontCharacter fontCharacter=font.get(character);
             
     for(FontSegment fontSegment:fontCharacter){
             boolean first=true;
         for(FontPoint fontPoint:fontSegment){
             if(first){
                     this.moveTo(x + fontPoint.getX(italics), y + fontPoint.getY());
                 first = false;
             }else{
                     this.lineTo(x + fontPoint.getX(italics), y + fontPoint.getY());
             }
         }
     }
     
     return fontCharacter.getWidth();
     }
     
     /**
      * Text to be drawn on the canvas at position x,y with a rotation of angle radian and scaled
      * @param text text to be drawn
      * @param x position
      * @param y position
      * @param angle in radian (put 0 to draw text horizontally)
      * @param scale (put 1 for character 12 pixels high)
      * @return the width (in pixels) of the text
      */
     public double renderText(String text, double x, double y, double angle, double scale) {
             super.translate(x, y);
             super.rotate(angle);
             super.scale(scale, scale);
             
             double rc = 0.0;
             for (int i = 0; i < text.length(); i++) {
                     rc += renderChar(text.charAt(i), rc, 0.0);
             }
             return rc * scale;
     }
     
     /**
      * Calculate text width
      * @param text
      * @param scale
      * @return
      */
     public double calculateTextWidth(String text,double scale){
             double rc=0.0;
             for(int i=0;i<text.length();i++) {
                     rc+=font.get(text.charAt(i)).getWidth();
             }
             return rc*scale;
     }
     
     public void cubicCurveTo(double cp1x, double cp1y, double cp2x,
        double cp2y, double x, double y) {
//			Map<String, Object> arguments = new HashMap<String, Object>();
//			arguments.put("command", "cubicCurveTo");
//			arguments.put("cp1x", cp1x);
//			arguments.put("cp1y", cp1y);
//			arguments.put("cp2x", cp2x);
//			arguments.put("cp2y", cp2y);
//			arguments.put("x", x);
//			arguments.put("y", y);
//			commands.add(arguments);
//
//			requestRepaint();
                }
     
     public Font getFont() {
             return font;
     }

     public void setFont(Font font) {
             this.font = font;
     }

     public boolean isItalics() {
             return italics;
     }

     public void setItalics(boolean italics) {
             this.italics = italics;
     }
     
     public void renderAllFontSymbols(double x, double y,double maximumWidth){
             for(FontCharacter fontCharacter:font.orderedFontList){
                     
                     this.saveContext();
                     this.beginPath();
                     setStrokeStyle(0,0,0);
             
                     for(FontSegment fontSegment : fontCharacter){
                             boolean first = true;
             for(FontPoint fontPoint : fontSegment){
                     if(first){
                             this.moveTo(x + fontPoint.getX(false), y + fontPoint.getY());
                     first = false;
                 }else{
                     this.lineTo(x + fontPoint.getX(false), y + fontPoint.getY());
                 }
             }
                     }
                     
                     this.stroke();
                     this.restoreContext();
             
                     double width=fontCharacter.getWidth();
     
                     x+=width;
     
                     if(x>=maximumWidth){
                             x=0;
                             y+=font.characterSetMaxHeight;
                     }
             }
     }
     
	     /**
	  * 
	  * @param startingScale
	  * @param decrementFactor
	  * @param maxWidth
	  * @param maxHeight
	  * @param text
	  * @return
	  * @TODO Add support for scaling up, split in list of methods
	  */
	 public TextInfo getRequiredScale(double startingScale, double decrementFactor, double maxWidth, double maxHeight,String text)
	 {
	     double scale=startingScale;
         double width;
         while(true)
         {
             width=calculateTextWidth(text, scale);
             if(scale<decrementFactor)
             {
                 width=calculateTextWidth(text,  decrementFactor);
                 scale=decrementFactor;
             }
             else if(width>maxWidth)
             {
                 scale-=decrementFactor;
             }
             else
             {
                 break;
             }
         }
             
         double height;
         while(true)
         {
             height=calculateTextWidth(text, scale);
             if(scale<decrementFactor)
             {
                 height=calculateTextWidth(text,  decrementFactor);
                 scale=decrementFactor;
             }
             else if(height>maxHeight)
             {
                 scale-=decrementFactor;
             }else
             {
                 break;
             }
         }
         
         return new TextInfo(scale,width,height);
	 }
 
	 public class TextInfo
	 {
	     public double scale;
	     public double height;
	     public double width;
	     
	     public TextInfo(double _scale, double _width, double _height)
	     {
             scale=_scale;
             width=_width;
             height=_height;
	     }
	 }
 
     
     public void notifySelectionListeners(double x, double y, double width, double height,boolean dragged)
     {
         for(SelectionListener listener:selectionListeners)
         {
             listener.recieveSelectionUpdate(x, y, width, height,dragged);
         }
     }
     
     public void addSelectionListener(SelectionListener listener)
     {
         selectionListeners.add(listener);
     }
     
     
     
	 public void notifyExportListeners(String type)
	 {
	     for(ExportListener listener:exportListeners)
	     {
	         listener.exportRequest(type);
	     }
	 }

	@Override
	public void clicked(MouseEventDetails med) {
		// TODO Auto-generated method stub
		
	}
     
//	 @SuppressWarnings("rawtypes")
//     @Override
//	 public void changeVariables(Object source, Map variables) 
//	 {
//	     boolean consumedMessage=false;
//	     if(variables.containsKey("event"))
//	     {
//             if(((String) variables.get("event")).equals("mousemoveselection"))
//             {
//                 notifySelectionListeners((Integer) variables.get("mx"),(Integer) variables.get("my"), (Integer) variables.get("lastwidth"), (Integer) variables.get("lastheight"),(Boolean) variables.get("mousemoved"));
//                 consumedMessage=true;
//             }
//             else if(((String) variables.get("event")).equals("export"))
//             {
//                 System.err.println("Export request: "+variables.get("type"));                     
//                 notifyExportListeners((String) variables.get("type"));
//             }
//	     }
//	     
//	     if(!consumedMessage)
//	     {
//             super.changeVariables(source,variables);
//	     }
//	 }

	//@Override
//	public void onMouseMove(MouseMoveEvent event) {
//		// TODO Auto-generated method stub
//        super.moveTo(event.getX(), event.getY());
//	}
//
//	@Override
//	public void onMouseUp(MouseUpEvent event) {
//		// TODO Auto-generated method stub
//        super.moveTo(event.getX(), event.getY());
//	}
//
//	@Override
//	public void onMouseDown(MouseDownEvent event) {
//		// TODO Auto-generated method stub
//	     System.err.println("Mouse down event!");
//	     for(SelectionListener listener:selectionListeners)
//	     {
//            listener.recieveSelectionUpdate(event.getX(), event.getY(), 0, 0, false);
//	     }
//	}
	
}
