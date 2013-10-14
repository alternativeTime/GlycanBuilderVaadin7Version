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
 * Base class defining a Font for GWTCanvas.<br><br>
 * 
 * A Font is an extended ArrayList of FontCharacter.<br>
 * A FontCharacter is an extended ArrayList of FontSegment.<br>
 * A FontSegment is an extended ArrayList of FontPoint.<br>
 * A FontPoint is the x,y coordinate of a point.<br><br>
 * 
 * Note: Only printable characters are supported, 
 * i.e. integer value from 32 to 126. 
 *
 */
@SuppressWarnings("serial")
public class Font extends ArrayList<FontCharacter> implements IsSerializable {

	private String fontName;

	/**
	 * Constructor
	 */
	public Font() {
		super();
		this.fontName = "";
	}
	
	/**
	 * Constructor with field
	 * 
	 * @param fontName name of the font
	 */
	public Font(String fontName) {
		super();
		this.fontName = fontName;
	}

	/**
	 * Constructor with field and initial capacity
	 * 
	 * @param fontName name of the font
	 * @param initialCapacity initial capacity
	 */
	public Font(String fontName, int initialCapacity) {
		super(initialCapacity);
		this.fontName = fontName;
	}

	/**
	 * Constructor with field and initial Collection
	 * 
	 * @param fontName name of the font
	 * @param c initial collection
	 */
	public Font(String fontName, Collection<? extends FontCharacter> c) {
		super(c);
		this.fontName = fontName;
	}

	/**
	 * Get the font name
	 * @return the font name
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * Set the font name
	 * @param fontName the font name
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

}
