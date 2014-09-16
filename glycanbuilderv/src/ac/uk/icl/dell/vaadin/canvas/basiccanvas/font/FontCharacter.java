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


/**
 * Class defining a character for a Font.<br>
 * 
 * A character has a value between 32 and 126 and a width
 *
 */
public class FontCharacter extends ArrayList<FontSegment> implements Serializable{
	private static final long serialVersionUID=3477644698460874299L;
	
	private char character = '\0';
	private double width = -1;
	public float height;
	/**
	 * Constructor
	 * 
	 * @see java.util.ArrayList#ArrayList()
	 */
	public FontCharacter() {
		super();
	}

	/**
	 * Constructor with fields
	 * 
	 * @param character value of this character 
	 * @param width width of the character
	 * @throws FontCharacterException 
	 */
	public FontCharacter(char character, double width) throws FontCharacterException {
		super();
		
		this.character = character;
		this.width = width;
	}

	/**
	 * Constructor with fields and initial capacity
	 * 
	 * @param character value of this character
	 * @param width width of the character
	 * @param initialCapacity initial capacity
	 * @throws FontCharacterException 
	 */
	public FontCharacter(char character, double width, int initialCapacity) throws FontCharacterException {
		this.character = character;
		this.width = width;
	}

	/**
	 * Get the width of the character
	 * @return width of the character
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Set the width of the character
	 * @param width of the character
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Get the character value
	 * @return value of the character
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * Set the character value
	 * @param character value
	 */
	public void setCharacter(char character) {
		this.character = character;
	}
	
	/**
	 * @author David R. Damerell
	 */
	public FontCharacter clone(){
		FontCharacter cloneCharacter=new FontCharacter();
		for(FontSegment segment:this){
			cloneCharacter.add(segment.clone());
		}
		
		cloneCharacter.character=character;
		cloneCharacter.height=height;
		cloneCharacter.width=width;
		
		
		return cloneCharacter;
	}
}
