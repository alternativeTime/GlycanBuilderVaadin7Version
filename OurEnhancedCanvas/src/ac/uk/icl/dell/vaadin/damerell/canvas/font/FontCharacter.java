/*
 * Copyright 2009 Software ToolBox (Bernard Clement)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ac.uk.icl.dell.vaadin.damerell.canvas.font;

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
