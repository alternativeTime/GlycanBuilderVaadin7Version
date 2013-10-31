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
package ac.uk.icl.dell.vaadin.damerell.canvas.font;

import java.io.Serializable;


/**
 * Class defining a point.<br><br>
 * 
 *  Notes:<br>
 *  1- Italic is achieved with a liner transform of x depending upon y<br>
 *  2- java.awt.Point cannot be used in the context of GWT
 *
 */
public class FontPoint implements Serializable{
	private static final long serialVersionUID=-8412475010740725881L;
	
	/**
	 * Note: A value of 10 provide the nicest italic shape.<br>
	 */
	private static double italicSlope = 10;
	private double x;
	private double y;

	/**
	 * Default constructor
	 */
	public FontPoint() {
		this.x = -1;
		this.y = -1;
	}
	
	/**
	 * Constructor with fields
	 * 
	 * @param x value of the point
	 * @param y value of the point
	 */
	public FontPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the value of x with the italic transform
	 * @param italic truee if italic transform is to be done
	 * @return the value of x
	 */
	public double getX(boolean italic) {
		return x + (italic ? (italicSlope * ((32 - y) / 32)) : 0);
	}

	/**
	 * Get the value of x without any italic transform
	 * @return the value of x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Get the value of y
	 * @return the value of y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Set the value of x
	 * @param x value
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Set the value of y
	 * @param y value
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * @author David R. Damerell
	 */
	public FontPoint clone(){
		FontPoint clonePoint=new FontPoint();
		clonePoint.x=x;
		clonePoint.y=y;
		
		return clonePoint;
	}
}
