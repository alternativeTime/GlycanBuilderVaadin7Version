/*
 * Copyright (c) 2014. Matthew Campbell <matthew.campbell@mq.edu.au>, David R. Damerell <david@nixbioinf.org>.
 *
 * This file is part of GlycanBuilder Vaadin Release and its affliated projects EUROCarbDB, UniCarb-DB and UniCarbKB.
 *
 * This program is free software free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GlycanBuilder Vaadin Release is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License (LICENSE.txt) for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GlycanBuilder Vaadin Release.  If not, see <http ://www.gnu.org/licenses/>.
 */
package ac.uk.icl.dell.vaadin.canvas.basiccanvas.font;

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
