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

public class Diamond extends BaseShape{

	public Diamond(double x, double y, double w, double h, ResidueStyle style,Canvas canvas ,boolean selected) {
		super(x, y, w, h, style, canvas,selected);
	}

	@Override
	protected void paintShape() {
		BaseShape.createDiamond(x, y, w, h, canvas);
		canvas.fill();
		setStrokeColour(style.getShapeColor());
		canvas.stroke();
	}
	
	@Override 
	protected void internalShapeFull(){
		BaseShape.createDiamond(x, y, w, h, canvas);
		canvas.fill();
	}
	
	@Override
	protected void internalShapeTop(){
		BaseShape.createTriangle(x, y+h/2., x+w/2., y, x+w, y+h/2., canvas);
		canvas.fill();
		canvas.setLineWidth(1);
		canvas.beginPath();
		canvas.moveTo(x, y+h/2.);
		canvas.lineTo(x+w, y+h/2.);
		canvas.stroke();
		canvas.closePath();
	}
	
	@Override
	protected void internalShapeLeft(){
		BaseShape.createTriangle(x+w/2., y, x+w/2., y+h, x, y+h/2., canvas);
		canvas.fill();
		canvas.setLineWidth(1);
		canvas.beginPath();
		canvas.moveTo(x+w/2., y);
		canvas.lineTo(x+w/2., y+h);
		canvas.stroke();
		canvas.closePath();
		
	}
	
	@Override
	protected void internalShapeBottom(){
		BaseShape.createTriangle(x, y+h/2., x+w, y+h/2., x+w/2., y+h, canvas);
		canvas.fill();
		canvas.setLineWidth(1);
		canvas.beginPath();
		canvas.moveTo(x, y+h/2.);
		canvas.lineTo(x+w, y+h/2.);
		canvas.stroke();
		canvas.closePath();
	}
	
	@Override
	protected void internalShapeRight(){
		BaseShape.createTriangle(x+w/2., y, x+w, y+h/2., x+w/2., y+h, canvas);
		canvas.fill();
		canvas.setLineWidth(1);
		canvas.beginPath();
		canvas.moveTo(x+w/2., y);
		canvas.lineTo(x+w/2., y+h);
		canvas.stroke();
		canvas.closePath();
	}
}