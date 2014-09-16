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
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class defining part (segment) of a character 
 *
 */
public class FontSegment extends ArrayList<FontPoint> implements Cloneable, Serializable{
	private static final long serialVersionUID=4082234453099148664L;

	/**
	 * Constructor
	 */
	public FontSegment() {
		super();
	}

	/**
	 * Constructor with initial capacity
	 * 
	 * @param initialCapacity initial capacity
	 */
	public FontSegment(int initialCapacity) {
		super(initialCapacity);
	}
	
	/**
	 * Constructor with initial collection
	 * 
	 * @param c initial collection
	 */
	public FontSegment(Collection<? extends FontPoint> c) {
		super(c);
	}

	/**
	 * @author David R. Damerell
	 */
	public FontSegment clone(){
		FontSegment cloneSeg=new FontSegment();
		for(FontPoint point:this){
			cloneSeg.add(point.clone());
		}
		
		return cloneSeg;
	}
}
