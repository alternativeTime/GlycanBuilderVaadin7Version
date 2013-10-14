/*
 * Copyright 2009 Software ToolBox (Bernard Clement)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ac.uk.icl.dell.gwt.canvas.client;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Widget;

/**
 * SWTBCanvasText extends GWTCanvas to draw Text over a canvas.
 *  
 * 
 */
// TODO Provide a real TODO in the Javadoc
public class ClientSideFontCanvas extends Widget{
	ContextExtra contextExtra = new ContextExtra(null, false);
	
	public Canvas canvas=Canvas.createIfSupported();

	private Queue<ContextExtra> arrayOfContextExtra = new LinkedList<ContextExtra>();

	public Context2d context2d;

	/**
	 * Creates a GWTCanvasText element. Element type depends on deferred
	 * binding. Default is CANVAS HTML5 DOM element. In the case of IE it should
	 * be VML.
	 * 
	 * <p>
	 * Screen size of canvas in pixels defaults to <b>300x150</b> pixels.
	 * </p>
	 */
	public ClientSideFontCanvas() {
		setup();
	}

	/**
	 * Creates a GWTCanvas element. Element type depends on deferred binding.
	 * Default is CANVAS HTML5 DOM element. In the case of IE it should be VML.
	 * 
	 * <p>
	 * Different coordinate spaces and pixel spaces will cause aliased scaling.
	 * Use <code>scale(double,double)</code> and consistent coordinate and pixel
	 * spaces for better results.
	 * </p>
	 * 
	 * @param coordX the size of the coordinate space in the x direction
	 * @param coordY the size of the coordinate space in the y direction
	 * @param pixelX the CSS width in pixels of the canvas element
	 * @param pixelY the CSS height in pixels of the canvas element
	 */
	//public SWTBCanvasText(int coordX, int coordY, int pixelX, int pixelY) {
	//	element.setCoordinateSpaceHeight(coordX);
	//	element.setCoordinateSpaceWidth(coordY);
		//super(coordX, coordY, pixelX, pixelY);
	//}

	/**
	 * Creates a GWTCanvas element. Element type depends on deferred binding.
	 * Default is CANVAS HTML5 DOM element. In the case of IE it should be VML.
	 * 
	 * <p>
	 * Screen size of canvas in pixels defaults to the coordinate space
	 * dimensions for this constructor.
	 * </p>
	 * 
	 * @param coordX
	 *            the size of the coordinate space in the x direction
	 * @param coordY
	 *            the size of the coordinate space in the y direction
	 */
	public ClientSideFontCanvas(int coordX, int coordY) {
		setElement(canvas.getElement());
		
		canvas.setCoordinateSpaceHeight(coordX);
		canvas.setCoordinateSpaceWidth(coordY);
		
		setWidth(coordX+"px");
		setHeight(coordY+"px");
	}
	
	protected void setup(){
		setElement(canvas.getElement());
		context2d=canvas.getContext2d();
	}

	/**
	 * Get the current font
	 * @return the current font
	 */
	public Font getFont() {
		return contextExtra.getFont();
	}

	/**
	 * Set the current font.  Required to draw text
	 * @param font to be used to draw text
	 */
	public void setFont(Font font) {
		contextExtra.setFont(font);
	}

	/**
	 * Set if the text is to be drawn in itaclic or not
	 * @param italic text to be drawn in italic
	 */
	public void setItalic(boolean italic) {
		contextExtra.setItalic(italic);
	}

	/**
	 * Character to be drawn on the canvas at position x,y
	 * @param c character to be drawn
	 * @param x position
	 * @param y position
	 * @return the width of the drawn character taking into account the scale
	 */
	private double charTo(Character c, double x, double y) {
		final Font font = contextExtra.getFont();
		final boolean italic = contextExtra.isItalic();

		if (font != null) {
			FontCharacter fontCharacter = font.get(c - ' ');
			for (FontSegment fontSegment : fontCharacter) {
				boolean first = true;
				for (FontPoint fontPoint : fontSegment) {
					if (first) {
						context2d.moveTo(x + fontPoint.getX(italic), y + fontPoint.getY());
						first = false;
					} else {
						context2d.lineTo(x + fontPoint.getX(italic), y + fontPoint.getY());
					}
				}
			}
			return fontCharacter.getWidth();
		}
		return 0.0;
	}

	/**
	 * Text to be drawn on the canvas at position x,y<br>
	 * This method has been depreciated due to a scaling problem.<br>
	 * In effect if the scale method is used before using textTo the
	 * origin of the drawing will also changed which is not the desired effect.
	 * 
	 * @param text text to be drawn
	 * @param x position
	 * @param y position
	 * @return the x position
	 */
	@Deprecated
	public double textTo(String text, double x, double y) {
		double rc = x;
		for (int i = 0; i < text.length(); i++) {
			rc += charTo(text.charAt(i), rc, y);
		}
		return rc;
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
	public double textTo(String text, double x, double y, double angle, double scale) {
		double m11 = Math.cos(angle) * scale;
		double m12 = -Math.sin(angle);
		double m21 = -m12;
		double m22 = m11;
		context2d.transform(m11, m12, m21, m22, x, y);
		
		double rc = 0.0;
		for (int i = 0; i < text.length(); i++) {
			rc += charTo(text.charAt(i), rc, 0.0);
		}
		return rc * scale;
	}

	/**
	 * Saves the current context to the context stack.
	 */
	public void saveContext() {
		context2d.save();
		//context2d.saveContext();
		arrayOfContextExtra.add(new ContextExtra(contextExtra));
	}

	/**
	 * Restores the last saved context from the context stack.
	 */
	public void restoreContext() {
		context2d.restore();
		int index = arrayOfContextExtra.size() - 1;
		if (index > 0) {
			contextExtra = arrayOfContextExtra.poll();
		}
	}

}
