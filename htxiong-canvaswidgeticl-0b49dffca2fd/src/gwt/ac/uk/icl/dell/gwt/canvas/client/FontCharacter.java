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
package ac.uk.icl.dell.gwt.canvas.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class defining a character for a Font.<br>
 * 
 * A character has a value between 32 and 126 and a width
 *
 */
@SuppressWarnings("serial")
public class FontCharacter extends ArrayList<FontSegment> implements IsSerializable {

	private char character = '\0';
	private double width = -1;

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
		if (character < 32 || character > 126) {
			throw new FontCharacterException();
		}
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
		super(initialCapacity);
		if (character < 32 || character > 126) {
			throw new FontCharacterException();
		}
		this.character = character;
		this.width = width;
	}

	/**
	 * Constructor with fields and initial collection
	 *  
	 * @param character value of this character
	 * @param width width of the character
	 * @param c initial collection
	 * @throws FontCharacterException 
	 */
	public FontCharacter(char character, double width, Collection<? extends FontSegment> c) throws FontCharacterException {
		super(c);
		if (character < 32 || character > 126) {
			throw new FontCharacterException();
		}
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

}
