package ac.uk.icl.dell.vaadin.canvas.basiccanvas;

import java.util.ArrayList;
import java.util.List;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.Font;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontCharacter;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontPoint;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.FontSegment;
import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas.CanvasMouseDownListener;
import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas.CanvasMouseUpListener;
import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas.CanvasMouseMoveListener;

import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas;

public class BasicCanvas extends Canvas implements CanvasMouseDownListener, CanvasMouseUpListener, CanvasMouseMoveListener{
	private static final long serialVersionUID = 1777004722553284089L;
	
	public Font font;
	public boolean italics=false;
	
	private int mouseDownXValue = 0; 
	private int mouseDownYValue = 0;
	
	private List<SelectionListener> selectionListeners;
	private List<ExportListener> exportListeners;
	
	public boolean awaitingInstructions=false;
	
	public BasicCanvas(){
		this.addListener((CanvasMouseUpListener)this);
		this.addListener((CanvasMouseMoveListener)this);
		this.addListener((CanvasMouseDownListener)this);
		
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
    public TextInfo getRequiredScale(double startingScale, double decrementFactor, double maxWidth, double maxHeight,String text){
    	double scale=startingScale;
		double width;
		while(true){
			width=calculateTextWidth(text, scale);
			if(scale<decrementFactor){
				width=calculateTextWidth(text,  decrementFactor);
				scale=decrementFactor;
			}else if(width>maxWidth){
				scale-=decrementFactor;
			}else{
				break;
			}
		}
		
		double height;
		while(true){
			height=calculateTextWidth(text, scale);
			if(scale<decrementFactor){
				height=calculateTextWidth(text,  decrementFactor);
				scale=decrementFactor;
			}else if(height>maxHeight){
				scale-=decrementFactor;
			}else{
				break;
			}
		}
		
		return new TextInfo(scale,width,height);
    }
    
    public class TextInfo{
    	public double scale;
    	public double height;
    	public double width;
    	
    	public TextInfo(double _scale, double _width, double _height){
    		scale=_scale;
    		width=_width;
    		height=_height;
    	}
    }

    @Override
	public void mouseDown(int x, int y) {
    	mouseDownXValue = x;
    	mouseDownYValue = y;
    	
    	System.err.println("Mouse down event!");
    	for(SelectionListener listener:selectionListeners){
			listener.recieveSelectionUpdate(x, y, 0, 0, false);
		}
	}

	@Override
	public void mouseMove(int x, int y) {
		super.moveTo(x, y);
	}

	@Override
	public void mouseUp(int x, int y) {
		if (mouseDownXValue != x || mouseDownYValue != y)
		{
				notifySelectionListeners(mouseDownXValue < x ? mouseDownXValue : x, mouseDownYValue < y ? mouseDownYValue : y,
						Math.abs(mouseDownXValue - x), Math.abs(mouseDownYValue - y), true);			
		}
		super.moveTo(x, y);
	}
	
	public void notifySelectionListeners(double x, double y, double width, double height,boolean dragged){
		for(SelectionListener listener:selectionListeners){
			listener.recieveSelectionUpdate(x, y, width, height,dragged);
		}
	}
	
	public void addSelectionListener(SelectionListener listener){
		selectionListeners.add(listener);
	}
		
	
    public void notifyExportListeners(String type){
    	for(ExportListener listener:exportListeners){
    		listener.exportRequest(type);
    	}
    }

}
