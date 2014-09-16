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
package ac.uk.icl.dell.vaadin.glycanbuilder;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;

import org.eurocarbdb.application.glycanbuilder.AbstractGlycanRenderer;
import org.eurocarbdb.application.glycanbuilder.BBoxManager;
import org.eurocarbdb.application.glycanbuilder.Geometry;
import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.Paintable;
import org.eurocarbdb.application.glycanbuilder.PositionManager;
import org.eurocarbdb.application.glycanbuilder.Residue;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.BasicCanvas;

public class GlycanRendererCanvas extends AbstractGlycanRenderer{
	
	public GlycanRendererCanvas() {
		super();
	}

	public GlycanRendererCanvas(GlycanRendererCanvas src) {
		super(src);
	}

	@Override
	protected void initialiseRenderers() {
		theResidueRenderer = new ResidueRendererCanvas(this);
		theLinkageRenderer = new LinkageRendererCanvas(this);
	}

	@Override
	protected void displayMass(Paintable paintable, Glycan structure,
			boolean show_redend, BBoxManager bboxManager) {
		
		BasicCanvas canvas=(BasicCanvas)paintable.getObject();

		if(structure.getRoot(show_redend)==null){
			System.err.println("It is null!!!!!");
		}
		
		Rectangle structure_all_bbox = bboxManager.getComplete(structure.getRoot(show_redend));

		canvas.setFont("14pt Calibri");
		canvas.setFillStyle("black");
		canvas.textAlign("left");
		canvas.fillText(getMassText(structure), Double.valueOf(Geometry.left(structure_all_bbox)), Double.valueOf(Geometry.bottom(structure_all_bbox)+20));
		
//		canvas.saveContext();
//		canvas.setLineWidth(2.0);
//		canvas.beginPath(); 
//		canvas.renderText(getMassText(structure), Geometry.left(structure_all_bbox),
//				Geometry.bottom(structure_all_bbox)+theGraphicOptions.MASS_TEXT_SPACE,0,0.5);
//		Color colour=Color.BLACK;
//		canvas.setStrokeStyle(colour.getRed(),colour.getGreen(), colour.getBlue());
//		
//		canvas.stroke();
//		canvas.restoreContext();
	}

	@Override
	protected void paintComposition(Paintable paintable, Residue root,
			Residue bracket, HashSet<Residue> selected_residues,
			PositionManager posManager, BBoxManager bboxManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void paintQuantity(Paintable paintable, Residue antenna,
			int quantity, BBoxManager bboxManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BufferedImage getImage(Collection<Glycan> structures,
			boolean opaque, boolean show_masses, boolean show_redend,
			double scale, PositionManager posManager, BBoxManager bboxManager) {
		// TODO Auto-generated method stub
		return null;
	}

}
