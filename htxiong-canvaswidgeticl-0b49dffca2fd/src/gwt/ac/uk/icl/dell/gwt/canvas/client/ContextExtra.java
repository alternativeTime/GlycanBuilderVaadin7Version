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

/**
 * Context data related to Text/Font  
 *
 */
public class ContextExtra {

	private Font font = null;
	private boolean italic = false;

	/**
	 * Constructor with fields
	 * 
	 * @param font - font of the context (default null)
	 * @param italic - to get italic (default false)
	 */
	public ContextExtra(Font font, boolean italic) {
		super();
		this.font = font;
		this.italic = italic;
	}

	/**
	 * Copy constructor
	 * 
	 * @param contextExtra - context to be copied 
	 */
	public ContextExtra(ContextExtra contextExtra) {
		this.font = contextExtra.getFont();
		this.italic = contextExtra.isItalic();
	}

	/**
	 * Get Font
	 * 
	 * @return the font in this context
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Set Font
	 * 
	 * @param font - font to be set in this context 
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Is the text to be drawn in Italic
	 * 
	 * @return true if the text is to be dranw in italic
	 */
	public boolean isItalic() {
		return italic;
	}

	/**
	 * Set the italic flag
	 * 
	 * @param italic true if the font is to be drawn in italic
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	
}
