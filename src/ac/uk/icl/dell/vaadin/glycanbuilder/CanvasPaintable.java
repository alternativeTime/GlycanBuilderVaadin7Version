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
package ac.uk.icl.dell.vaadin.glycanbuilder;

import java.awt.Graphics2D;

import org.eurocarbdb.application.glycanbuilder.Paintable;
import org.vaadin.damerell.canvas.BasicCanvas;

public class CanvasPaintable implements Paintable{
	BasicCanvas canvas;
	
	public CanvasPaintable(BasicCanvas canvas){
		this.canvas=canvas;
	}
	
	@Override
	public Graphics2D getGraphics2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject() {
		return canvas;
	}

	@Override
	public void clear() {
		
		
		
		canvas.clear();
	}
}
