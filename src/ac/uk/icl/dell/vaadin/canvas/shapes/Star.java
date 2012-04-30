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

public class Star extends BaseShape{
	protected int points;
	
	public Star(double x, double y, double w, double h, ResidueStyle style, Canvas canvas, int points,boolean selected) {
		super(x, y, w, h, style, canvas,selected);
		this.points=points;
	}

	@Override
	protected void paintShape() {
		BaseShape.createStar(x, y, w, h, canvas,points);
		canvas.fill();
		setStrokeColour(style.getShapeColor());
		canvas.stroke();
	}
	
	@Override
	protected void internalShapeFull(){
		BaseShape.createStar(x, y, w, h, canvas,points);
		canvas.fill();
	}


}
