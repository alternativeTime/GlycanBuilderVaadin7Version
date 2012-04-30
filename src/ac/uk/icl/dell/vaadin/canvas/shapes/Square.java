/*
*   EuroCarbDB, a framework for carbohydrate bioinformatics
*
*   Copyright (c) 2006-2011, Eurocarb project, or third-party contributors as
*   indicated by the @author tags or express copyright attribution
*   statements applied by the authors.  
*
*   This copyrighted material is made available to anyone wishing to use, modify,
*   copy, or redistribute it subject to the terms and conditions of the GNU
*   Lesser General Public License, as published by the Free Software Foundation.
*   A copy of this license accompanies this distribution in the file LICENSE.txt.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
*   for more details.
*   
*   @author David R. Damerell (david@nixbioinf.org)
*/
package ac.uk.icl.dell.vaadin.canvas.shapes;

import org.eurocarbdb.application.glycanbuilder.ResidueStyle;
import org.vaadin.hezamu.canvas.Canvas;

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
