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
package ac.uk.icl.dell.vaadin.canvas.shapes;

import org.eurocarbdb.application.glycanbuilder.ResidueStyle;
import ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.Canvas;

public class Square extends BaseShape{

	public Square(double x, double y, double w, double h, ResidueStyle style,
			Canvas canvas,boolean selected) {
		super(x, y, w, h, style, canvas, selected);
	}

	@Override
	protected void paintShape() {
		canvas.fillRect(x, y, w, h);
		setStrokeColour(style.getShapeColor());
		canvas.strokeRect(x, y, w, h);
	}
	
	@Override
	protected void internalShapeFull(){
		canvas.fillRect(x, y, w, h);
    	setFillStyle(style.getShapeColor());
	}
	
	@Override
	protected void internalShapeLeft(){
    	canvas.fillRect(x, y, w/2., h);
    	setFillStyle(style.getShapeColor());
    	canvas.strokeRect(x, y, w/2., y);
	}
	
	@Override
	protected void internalShapeTop(){
    	canvas.fillRect(x, y, w, h/2.);
    	setFillStyle(style.getShapeColor());
    	canvas.strokeRect(x, y, w, y/2.);
	}
	
	@Override
	protected void internalShapeRight(){
		canvas.fillRect(x+w/2.,y,w/2.,h);
    	setFillStyle(style.getShapeColor());
    	canvas.strokeRect(x+w/2.,y,w/2.,h);
	}
	
	@Override
	protected void internalShapeBottom(){
		canvas.fillRect(x,y+h/2.,w,h/2.);
    	setFillStyle(style.getShapeColor());
    	canvas.strokeRect(x,y+h/2.,w,h/2.);
	}
	
	@Override
	protected void internalShapeTopLeft(){
		BaseShape.createTriangle(x,y,x+w,y,x,y+h,canvas);
		canvas.fill();
    	setFillStyle(style.getShapeColor());
    	canvas.stroke();
	}
	
	@Override
	protected void internalShapeTopRight(){
		BaseShape.createTriangle(x,y,x+w,y,x+w,y+h,canvas);
    	canvas.fill();
    	setFillStyle(style.getShapeColor());
    	canvas.stroke();
	}
	
	@Override
	protected void internalShapeBottomRight(){
		BaseShape.createTriangle(x+w,y,x+w,y+h,x,y+h,canvas);
    	canvas.fill();
    	setFillStyle(style.getShapeColor());
    	canvas.stroke();
	}
	
	@Override
	protected void internalShapeBottomLeft(){
		BaseShape.createTriangle(x,y,x+w,y+h,x,y+h,canvas);
    	canvas.fill();
    	setFillStyle(style.getShapeColor());
    	canvas.stroke();
	}
}
