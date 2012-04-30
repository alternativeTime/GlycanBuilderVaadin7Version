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

import static ac.uk.icl.dell.vaadin.glycanbuilder.Geometry.angle;
import static ac.uk.icl.dell.vaadin.glycanbuilder.Geometry.center;
import static ac.uk.icl.dell.vaadin.glycanbuilder.Geometry.distance;
import static ac.uk.icl.dell.vaadin.glycanbuilder.Geometry.getExclusionRadius;
import static ac.uk.icl.dell.vaadin.glycanbuilder.Geometry.translate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.eurocarbdb.application.glycanbuilder.AbstractLinkageRenderer;
import org.eurocarbdb.application.glycanbuilder.Geometry;
import org.eurocarbdb.application.glycanbuilder.GlycanRenderer;
import org.eurocarbdb.application.glycanbuilder.Linkage;
import org.eurocarbdb.application.glycanbuilder.LinkageStyle;
import org.eurocarbdb.application.glycanbuilder.Paintable;
import org.eurocarbdb.application.glycanbuilder.Residue;
import org.eurocarbdb.application.glycanbuilder.TextUtils;
import org.vaadin.damerell.canvas.BasicCanvas;

import ac.uk.icl.dell.vaadin.canvas.shapes.BaseShape;

public class LinkageRendererCanvas extends AbstractLinkageRenderer{
	public LinkageRendererCanvas() {
		super();
    }

    public LinkageRendererCanvas(GlycanRenderer src) {
    	super(src);
    }

	@Override
	public void paintEdge(Paintable paintable, Linkage link, boolean selected,
			Rectangle parent_bbox, Rectangle parent_border_bbox,
			Rectangle child_bbox, Rectangle child_border_bbox) {
		if (link == null)
			return;
		
		BasicCanvas theCanvas=(BasicCanvas) paintable.getObject();
		
		if(selected){
			theCanvas.setLineWidth(3.);
		}else{
			theCanvas.setLineWidth(2.);
		}
		
		
		createShape(paintable,link, parent_bbox, child_bbox);

		// paint linkage info
		if(theGraphicOptions.SHOW_INFO){
			paintInfo(paintable,link,parent_bbox,parent_border_bbox,child_bbox,child_border_bbox);
		}
	}

	@Override
	public void paintInfo(Paintable paintable, Linkage link,
			Rectangle parent_bbox, Rectangle parent_border_bbox,
			Rectangle child_bbox, Rectangle child_border_bbox) {
		if (link == null || !theGraphicOptions.SHOW_INFO)
			return;

		LinkageStyle style = theLinkageStyleDictionary.getStyle(link);

		Residue child = link.getChildResidue();
		if (style.showParentLinkage(link)){
			paintInfo(paintable,link.getParentPositionsString(), parent_bbox,
					parent_border_bbox, child_bbox, child_border_bbox, true,
					false, link.hasMultipleBonds());
		}
		
		if (style.showAnomericCarbon(link)){
			paintInfo(paintable,link.getChildPositionsString(), parent_bbox,
					parent_border_bbox, child_bbox, child_border_bbox, false,
					true, link.hasMultipleBonds());
		}
		
		if (style.showAnomericState(link, child.getAnomericState())){
			paintInfo(paintable,TextUtils.toGreek(child.getAnomericState()),
					parent_bbox, parent_border_bbox, child_bbox,
					child_border_bbox, false, false, link.hasMultipleBonds());
		}
		
	}

	@Override
	protected void paintInfo(Paintable paintable, String text, Rectangle p,
			Rectangle pb, Rectangle c, Rectangle cb, boolean toparent,
			boolean above, boolean multiple) {
		BasicCanvas theCanvas=(BasicCanvas) paintable.getObject();
		try{
			double scale=0.4;
			double width=theCanvas.calculateTextWidth(text, scale);		
			Point pos = computePosition(new Dimension((int)width,(int)(12.*scale)), p, pb, c, cb, toparent, above, multiple);
 
			
			theCanvas.font("9pt Calibri");
			theCanvas.setFillStyle("black");
			theCanvas.textAlign("left");
			theCanvas.fillText(text, pos.x, pos.y+2);
			
//			//theGraphicOptions.LINKAGE_INFO_SIZE => will be the font scale factor
//			theCanvas.saveContext();
//			theCanvas.setLineWidth(1.3);
//			theCanvas.beginPath(); 
//			//This is an arbitrary correction factor, something's not compatible in computePosition with the HTML5 placement
//			theCanvas.renderText(text, pos.x, pos.y-8.2, .0,scale);
//			setStrokeColour(paintable,Color.BLACK);
//			theCanvas.stroke();
//			theCanvas.restoreContext();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	@Override
	protected Point computePosition(Dimension tb, Rectangle p, Rectangle pb,Rectangle c, Rectangle cb, boolean toparent, boolean above,boolean multiple) {
		Point cp = Geometry.center(p);
    	Point cc = Geometry.center(c);

    	double r = 0.5 * 12;
    	double cx=0.,cy=0.,R=0.,angle=0.;
    	if( toparent ) {
    		cx = cp.x;
    		cy = cp.y;
    		angle = Geometry.angle(cc,cp);
    		R = getExclusionRadius(cp,angle,pb)+2;
    	}
    	else {
    		cx = c.x+c.width/2;
    		cy = c.y+c.height/2;
    		angle = Geometry.angle(cp,cc);
    		R = getExclusionRadius(cc,angle,cb)+2;
    	}
    	double space = (multiple) ?4. :2.;

    	boolean add = above;
    	if( toparent )
    		add = !add;

    	double tx=0.,ty=0.;
    	if( add ) {
    		tx = cx+(R+r)*Math.cos(angle)+(r+space)*Math.cos(angle-Math.PI/2.);
    		ty = cy+(R+r)*Math.sin(angle)+(r+space)*Math.sin(angle-Math.PI/2.);
    	}
    	else {
    		tx = cx+(R+r)*Math.cos(angle)+(r+space)*Math.cos(angle+Math.PI/2.);
    		ty = cy+(R+r)*Math.sin(angle)+(r+space)*Math.sin(angle+Math.PI/2.);
    	}    

    	tx -= tb.getWidth()/2;
    	ty += tb.getHeight()/2;

    	return new Point((int)tx,(int)ty);
		
//		Point cp = center(p);
//		Point cc = center(c);
//
//		double r = .3 * 12;
//		//**double r = 0.5 * theGraphicOptions.LINKAGE_INFO_SIZE;
//		double cx = 0., cy = 0., R = 0., angle = 0.;
//		if (toparent) {
//			cx = cp.x;
//			cy = cp.y;
//			angle = angle(cc, cp);
//			R = getExclusionRadius(cp, angle, pb) + 2;
//		} else {
//			cx = c.x + c.width / 2;
//			cy = c.y + c.height / 2;
//			angle = angle(cp, cc);
//			R = getExclusionRadius(cc, angle, cb) + 2;
//		}
//		double space = (multiple) ? 4. : 2.;
//
//		boolean add = above;
//		if (toparent)
//			add = !add;
//
//		double tx = 0., ty = 0.;
//		if (add) {
//			tx = cx + (R + r) * Math.cos(angle) + (r + space)
//					* Math.cos(angle - Math.PI / 2.);
//			ty = cy + (R + r) * Math.sin(angle) + (r + space)
//					* Math.sin(angle - Math.PI / 2.);
//		} else {
//			tx = cx + (R + r) * Math.cos(angle) + (r + space)
//					* Math.cos(angle + Math.PI / 2.);
//			ty = cy + (R + r) * Math.sin(angle) + (r + space)
//					* Math.sin(angle + Math.PI / 2.);
//		}
//		
//		tx -= tb.getWidth() / 2;
//		ty += tb.getHeight() / 2;

		//return new Point((int) tx, (int) ty);
	}

	@SuppressWarnings("unused")
	private Stroke createStroke(Linkage link, boolean selected) {
		LinkageStyle style = theLinkageStyleDictionary.getStyle(link);

		if (style.isDashed()) {
			float[] dashes = { 5.f, 5.f };
			return new BasicStroke((selected) ? 2.f : 1.f,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.f, dashes,
					0.f);
		}

		return new BasicStroke((selected) ? 2.f : 1.f);
	}

	private void paintLine(Paintable paintable,Point p1, Point p2, boolean multiple, Linkage link) {
		if(multiple){
			//GeneralPath gp = new GeneralPath();
			double a = angle(p1, p2);

			paintLine(paintable,
					translate(p1, 2. * Math.cos(a + Math.PI / 2),
							2. * Math.sin(a + Math.PI / 2)),
					translate(p2, 2. * Math.cos(a + Math.PI / 2),
							2. * Math.sin(a + Math.PI / 2)), false,link);

			paintLine(paintable,
					translate(p1, 2. * Math.cos(a - Math.PI / 2),
							2. * Math.sin(a - Math.PI / 2)),
					translate(p2, 2. * Math.cos(a - Math.PI / 2),
							2. * Math.sin(a - Math.PI / 2)), false,link);
		}else{
			paintLine(paintable,p1, p2,link);
		}
	}

	private void paintLine(Paintable paintable,Point p1, Point p2, Linkage link) {
		BasicCanvas theCanvas=(BasicCanvas) paintable.getObject();
		theCanvas.beginPath();
		
		LinkageStyle style = theLinkageStyleDictionary.getStyle(link);
		
		if(style.isDashed()){
			BaseShape.paintDashedLine(theCanvas, p1.x,p1.y,p2.x,p2.y,3);
		}else{
			theCanvas.moveTo(p1.x, p1.y);
			theCanvas.lineTo(p2.x, p2.y);
		}
		
		
		
		theCanvas.closePath();
		setStrokeColour(paintable,Color.BLACK);
		theCanvas.stroke();
	}

	private void createCurve(Paintable paintable,Point p1, Point p2) {
		double cx = (p1.x + p2.x) / 2.;
		double cy = (p1.y + p2.y) / 2.;
		double r = distance(p1, p2) / 2.;
		double angle = angle(p1, p2);

		// start point
		double x1 = cx + r * Math.cos(angle);
		double y1 = cy + r * Math.sin(angle);

		// end point
		double x2 = cx + r * Math.cos(angle + Math.PI);
		double y2 = cy + r * Math.sin(angle + Math.PI);

		// ctrl point 1
		double cx1 = cx + 0.1 * r * Math.cos(angle);
		double cy1 = cy + 0.1 * r * Math.sin(angle);
		double tx1 = cx1 + r * Math.cos(angle + Math.PI / 2.);
		double ty1 = cy1 + r * Math.sin(angle + Math.PI / 2.);

		// ctrl point 2
		double cx2 = cx + 0.1 * r * Math.cos(angle + Math.PI);
		double cy2 = cy + 0.1 * r * Math.sin(angle + Math.PI);
		double tx2 = cx2 + r * Math.cos(angle - Math.PI / 2.);
		double ty2 = cy2 + r * Math.sin(angle - Math.PI / 2.);
		
		//Bit of a guess at this point
		BasicCanvas theCanvas=(BasicCanvas) paintable.getObject();
		
		theCanvas.beginPath();
		theCanvas.moveTo(x1, y1);
		theCanvas.cubicCurveTo(tx1, ty1, tx2, ty2, x2, y2);
		setStrokeColour(paintable,Color.BLACK);
		theCanvas.stroke();
		theCanvas.closePath();

		//return new CubicCurve2D.Double(x1, y1, );
	}

	private void paintCurve(Paintable paintable,Point p1, Point p2, boolean multiple, Linkage link) {
		if (multiple) {
			double a = angle(p1, p2);

			paintCurve(paintable,
					translate(p1, 2. * Math.cos(a + Math.PI / 2),
							2. * Math.sin(a + Math.PI / 2)),
					translate(p2, 2. * Math.cos(a + Math.PI / 2),
							2. * Math.sin(a + Math.PI / 2)), false,link);

			paintCurve(paintable,
					translate(p1, 2. * Math.cos(a - Math.PI / 2),
							2. * Math.sin(a - Math.PI / 2)),
					translate(p2, 2. * Math.cos(a - Math.PI / 2),
							2. * Math.sin(a - Math.PI / 2)), false,link);
		}
		createCurve(paintable,p1, p2);
	}

	private void createShape(Paintable paintable,Linkage link, Rectangle parent_bbox,
			Rectangle child_bbox) {
		LinkageStyle style = theLinkageStyleDictionary.getStyle(link);
		String edge_style = style.getShape();

		Point parent_center = center(parent_bbox);
		Point child_center = center(child_bbox);

		if (edge_style.equals("none")){
			//return null;
		}
		if (edge_style.equals("empty")){
			//return null;
		}
		if (edge_style.equals("line")){
			paintLine(paintable,parent_center, child_center,
					link.hasMultipleBonds(),link);
		}else if (edge_style.equals("curve")){
			paintCurve(paintable,parent_center, child_center,
					link.hasMultipleBonds(),link);
		}else{
			paintLine(paintable,parent_center, child_center,link);
		}
	}
	
	final protected void setStrokeColour(Paintable paintable,Color colour){
		BasicCanvas theCanvas=(BasicCanvas) paintable.getObject();
		theCanvas.setStrokeStyle(colour.getRed(),colour.getGreen(), colour.getBlue());
	}
}
