/*
 * Copyright 2011 Imperial College London (David R. Damerell)
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
package ac.uk.icl.dell.vaadin.canvas.basiccanvas.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
public class Font extends HashMap<Character,FontCharacter> implements Serializable{
	private static final long serialVersionUID=5024626343913116045L;
	
	private String fontName;
	public float characterSetMaxHeight=-1f;
	public float characterSetMinHeight=1000f;
	
	public ArrayList<FontCharacter> orderedFontList=new ArrayList<FontCharacter>();
	
	enum FontFormat {
		HERSHEY(),HERSHEY_CORD()
	}

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
		super();
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
	
	/**
	 * @author David R. Damerell
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws FontCharacterException
	 */
	public static Font parseFontDefinition(InputStream stream) throws IOException, NumberFormatException, FontCharacterException{
		return parseFontDefinition(new InputStreamReader(stream));
	}
	
	/**
	 * @author David R. Damerell
	 * @param simplexFileName
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws FontCharacterException
	 */
	public static Font parseFontDefinition(String simplexFileName) throws IOException, NumberFormatException, FontCharacterException{
		return parseFontDefinition(new FileReader(new File(simplexFileName)));
	}
	
	/**
	 * @author David R. Damerell
	 * @param stream
	 * @param utf8CharacterList
	 * @param format
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws FontCharacterException
	 */
	public static Font parseFontDefinition(InputStream stream,InputStream utf8CharacterList,FontFormat format) throws NumberFormatException, IOException, FontCharacterException{
		if(format==FontFormat.HERSHEY_CORD){
			return parseFontDefinition(stream);
		}else if(format==FontFormat.HERSHEY){
			
			System.err.println("here");
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(stream,"UTF8"));
			
			List<HersheyFontCharacter> fontCharacters=new ArrayList<HersheyFontCharacter>();
			
			String characterDefinition=null;
			
			float minimumY=Float.MAX_VALUE;
			
			Pattern startOfEntry=Pattern.compile("^\\s*[0-9]+");
			Pattern emptyLine=Pattern.compile("^\\s*$");
			
			String line;
			while((line=reader.readLine())!=null){
				if(startOfEntry.matcher(line).find()){
					if(characterDefinition!=null){
						HersheyFontCharacter character=parseHesheyCode(characterDefinition);
						fontCharacters.add(character);
						minimumY=updateMinimumY(character, minimumY);
						
						characterDefinition=null;
					}
					
					characterDefinition=line;
				}else{
					if(!emptyLine.matcher(line).find()){
						characterDefinition=characterDefinition.concat(line);
					}
				}
			}
			
			reader.close();
			
			System.err.println("Number of characters: "+fontCharacters.size());
			
			if(characterDefinition!=null){
				HersheyFontCharacter character=parseHesheyCode(characterDefinition);
				fontCharacters.add(character);
				minimumY=updateMinimumY(character, minimumY);
			}
			
			for(HersheyFontCharacter hersheyChar:fontCharacters){
				for(Float vertice[]:hersheyChar.vertices){
					if(vertice!=null && vertice.length!=0){
						vertice[1]=vertice[1]-minimumY;
					}
				}
			}
			
			reader=new BufferedReader(new InputStreamReader(utf8CharacterList,"UTF8"));
			line=null;
			
			int charPos=-1;
			int numberOfCharacters=fontCharacters.size();
			
			System.err.println("Number of characters: "+numberOfCharacters);
			
			while((line=reader.readLine())!=null){
				charPos++;
				
				if(charPos>numberOfCharacters-1){
					break;
				}else{
					if(line.length()>0){
						fontCharacters.get(charPos).character=line.charAt(0);
					}else{
						//fontCharacters.get(charPos).character=line.charAt(0);
					}
					
				}
			}
			
			return createFontFromHersheyFontCharacters(fontCharacters);
		}else{
			return null;
		}
	}
	
	/**
	 * @author David R. Damerell
	 */
	public void printCharactersInFont(){
		for(char character:keySet()){
			System.out.println(">"+character);
		}
	}
	
	/**
	 * @author David R. Damerell
	 * @param hersheyFontCharacterList
	 * @return
	 */
	private static Font createFontFromHersheyFontCharacters(List<HersheyFontCharacter> hersheyFontCharacterList){
		Font font=new Font();
		
		for(HersheyFontCharacter hersheyCharacter:hersheyFontCharacterList){
			float maxHeight=0f;
			float minHeight=1000f;
			
			FontCharacter fontCharacter=new FontCharacter();
			FontSegment segment=new FontSegment();
			fontCharacter.add(segment);
			
			for(Float vertice[]:hersheyCharacter.vertices){
				if(vertice==null || vertice.length==0){
					segment=new FontSegment();
					fontCharacter.add(segment);
				}else{
					FontPoint fontPoint=new FontPoint(vertice[0],vertice[1]);
					
					segment.add(fontPoint);
					
					if(maxHeight < fontPoint.getY()){
						maxHeight=(float)fontPoint.getY();
					}
					
					if(minHeight > fontPoint.getY()){
						minHeight=(float)fontPoint.getY();
					}
				}
			}
			
			fontCharacter.height=maxHeight-minHeight;
			fontCharacter.setCharacter(hersheyCharacter.character);
			
			if(font.characterSetMinHeight > fontCharacter.height){
				font.characterSetMinHeight=fontCharacter.height;
			}
			
			if(font.characterSetMaxHeight < fontCharacter.height){
				font.characterSetMaxHeight=fontCharacter.height;
			}
			
			fontCharacter.setWidth(hersheyCharacter.width);
			
			font.put(hersheyCharacter.character, fontCharacter);
			font.orderedFontList.add(fontCharacter);
		}
		
		return font;
	}
	
	/**
	 * @author David R. Damerell
	 * @param character
	 * @param minimumY
	 * @return
	 */
	private static float updateMinimumY(HersheyFontCharacter character,float minimumY){
		for(Float vertice[]:character.vertices){
			if(vertice!=null && vertice.length!=0 && vertice[1] < minimumY){
				minimumY=vertice[1];
			}
		}
		
		return minimumY;
	}
	
	/**
	 * @author David R. Damerell
	 * @param hersheyCode
	 * @return
	 */
	private static HersheyFontCharacter parseHesheyCode(String hersheyCode){
		System.err.println("Hershey code: "+hersheyCode);
		
		//Pattern whiteSpace=Pattern.compile("\\s+");
		Pattern startWhiteSpace=Pattern.compile("^\\s+");
		Pattern endWhiteSpace=Pattern.compile("\\s+$");
		
		
		String glyphNumS=hersheyCode.substring(0, 5);
		
		System.err.println("Glyph number: "+glyphNumS);
		
		String numberOfVerticesS=hersheyCode.substring(6,8);
		
		System.err.println("Number of vertices: "+numberOfVerticesS);
		
		//int glyphNum=Integer.parseInt(whiteSpace.matcher(glyphNumS).replaceAll(""));
		//int numberOfVertices=Integer.parseInt(whiteSpace.matcher(numberOfVerticesS).replaceAll(""));
		
		int rAsInt='R';
		
	    int leftPosition=hersheyCode.charAt(8)-rAsInt;
	    
	    System.err.println("Lenght: "+hersheyCode.length());
	    
	    int rightPosition=hersheyCode.charAt(9)-rAsInt;

	    System.err.println("Left position: "+hersheyCode.charAt(8));
	    System.err.println("Right position: "+hersheyCode.charAt(9));
	    
	    List<Float[]> vertices=new ArrayList<Float[]>();
	    
	    if(hersheyCode.length()>10){
	    	String points=hersheyCode.substring(10,hersheyCode.length());

	    	points=startWhiteSpace.matcher(points).replaceAll("");
	    	points=endWhiteSpace.matcher(points).replaceAll("");

	    	for(int i=0;i<points.length()-1;i++){
	    		int posI=points.charAt(i)-rAsInt;

	    		if(points.charAt(i)==' ' && points.charAt(i+1)=='R'){
	    			vertices.add(null);
	    			i++;
	    		}else{
	    			int posJ=points.charAt(++i)-rAsInt;
	    			vertices.add(new Float[]{(float) posI,(float) posJ});
	    		}
	    	}

	    	float maximumNegativeX=0f,maximumNegativeY=0f;

	    	for(Float vertice[]:vertices){
	    		if(vertice!=null && vertice.length>0){
	    			if(vertice[0]<0){
	    				if(maximumNegativeX > vertice[0]){
	    					maximumNegativeX=vertice[0];
	    				}
	    			}

	    			if(vertice[1]<0){
	    				if(maximumNegativeY>vertice[1]){
	    					maximumNegativeY=vertice[1];
	    				}
	    			}
	    		}
	    	}

	    	if(maximumNegativeX!=0){
	    		float offSetX=Math.abs(maximumNegativeX);
	    		for(Float[] vertice:vertices){
	    			if(vertice!=null && vertice.length>0){
	    				vertice[0]+=offSetX;
	    			}
	    		}
	    	}
	    
	    }
	    
	    int width=rightPosition-leftPosition;
	    
	    return new HersheyFontCharacter(vertices,width);
	}
	
	
	
	/**
	 * @author David R. Damerell
	 * @param readerInstance
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws FontCharacterException
	 */
	public static Font parseFontDefinition(Reader readerInstance) throws IOException, NumberFormatException, FontCharacterException{
		Font font=new Font();

		BufferedReader reader=new BufferedReader(readerInstance);
		
		String line;
		while((line=reader.readLine())!=null){
			boolean at=false;
			boolean ti=false;
			if(line.startsWith("@")){
				at=true;
				line=line.replaceFirst("@", "at");
			}else if(line.startsWith("~")){
				ti=true;
				line=line.replaceFirst("~", "line");
			}
			
			
			String components[]=line.split("@");
			String infoCols[]=components[0].split("~");
			
			if(at){
				infoCols[0]="@";
			}else if(ti){
				infoCols[0]="~";
			}
			
			FontCharacter fontCharacter;
			FontSegment fontSegment;
			fontCharacter = new FontCharacter(infoCols[0].charAt(0), Double.parseDouble(infoCols[1]));
			
			float maxHeight=0f;
			float minHeight=1000f;
			
			for(int i=1;i<components.length;i++){
				fontSegment=new FontSegment();
				fontCharacter.add(fontSegment);
				components[i]=components[i].replaceFirst("@", "");
				components[i]=components[i].replaceFirst(",$", "");
				String points[]=components[i].split(",");
				for(int j=0;j<points.length;j++){
					FontPoint fontPoint=new FontPoint(Double.parseDouble(points[j]),Double.parseDouble(points[++j]));
					fontSegment.add(fontPoint);
					
					if(maxHeight < fontPoint.getY()){
						maxHeight=(float)fontPoint.getY();
					}
					
					if(minHeight > fontPoint.getY()){
						minHeight=(float)fontPoint.getY();
					}
				}	
			}
			
			fontCharacter.height=maxHeight-minHeight;
			
			if(font.characterSetMinHeight > fontCharacter.height){
				font.characterSetMinHeight=fontCharacter.height;
			}
			
			if(font.characterSetMaxHeight < fontCharacter.height){
				font.characterSetMaxHeight=fontCharacter.height;
			}
			
			
			font.put(infoCols[0].charAt(0), fontCharacter);
			font.orderedFontList.add(fontCharacter);
		}
		
		return font;
	}
	
	/**
	 * @author David R. Damerell
	 */
	static HashMap<FONT,Font>  fonts=new HashMap<FONT,Font>();
	
	/**
	 * @author David R. Damerell
	 * @author david
	 *
	 */
	public enum FONT{
		GREEK(),ROMAN(),STANDARD();
	}
	
	/**
	 * @author David R. Damerell
	 * @param font
	 * @return
	 */
	public static Font getFont(FONT font){
		if(fonts.containsKey(font)){
			return fonts.get(font);
		}else{
			return null;
		}
	}
	
	/**
	 * @author David R. Damerell
	 */
	@Override
	public Font clone(){
		Font cloneFont=new Font();
		for(FontCharacter character:orderedFontList){
			FontCharacter cloneCharacter=character.clone();
			
			cloneFont.orderedFontList.add(cloneCharacter);
			cloneFont.put(cloneCharacter.getCharacter(), cloneCharacter);
		}
		
		cloneFont.characterSetMaxHeight=characterSetMaxHeight;
		cloneFont.characterSetMinHeight=characterSetMinHeight;
		cloneFont.fontName=fontName;
		
		return cloneFont;
	}
	
	/**
	 * @author David R. Damerell
	 * @param fonts
	 * @return
	 */
	public static Font mergeFonts(List<Font> fonts){
		Font mergedFont=new Font();
		
		mergedFont.characterSetMaxHeight=Float.MIN_VALUE;
		mergedFont.characterSetMinHeight=Float.MAX_VALUE;	
		
		for(Font fontX:fonts){
			fontX=fontX.clone();
			for(FontCharacter character:fontX.orderedFontList){
				if(!mergedFont.containsKey(character.getCharacter())){
					mergedFont.put(character.getCharacter(), character);
					mergedFont.orderedFontList.add(character);
					
					if(mergedFont.characterSetMinHeight > character.height){
						mergedFont.characterSetMinHeight=character.height;
					}
					
					if(mergedFont.characterSetMaxHeight < character.height){
						mergedFont.characterSetMaxHeight=character.height;
					}
				}
			}
		}
		
		return mergedFont;
	}
	
	/**
	 * @author David R. Damerell
	 */
	static {
		try{
			Font font=Font.parseFontDefinition(Font.class.getResourceAsStream("/ac/uk/icl/dell/vaadin/canvas/basiccanvas/font/hershey/romans.jhf"),
					Font.class.getResourceAsStream("/ac/uk/icl/dell/vaadin/canvas/basiccanvas/font/maps/romans_character_map"), FontFormat.HERSHEY);
			
			fonts.put(FONT.ROMAN,font);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			Font font=Font.parseFontDefinition(Font.class.getResourceAsStream("/ac/uk/icl/dell/vaadin/canvas/basiccanvas/font/hershey/greeks.jhf"),
					Font.class.getResourceAsStream("/ac/uk/icl/dell/vaadin/canvas/basiccanvas/font/maps/greeks_character_map"), FontFormat.HERSHEY);
			
			fonts.put(FONT.GREEK,font);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<Font> mergeFontList=new ArrayList<Font>();
		mergeFontList.add(fonts.get(FONT.ROMAN));
		mergeFontList.add(fonts.get(FONT.GREEK));
		
		fonts.put(FONT.STANDARD, Font.mergeFonts(mergeFontList));
	}
	
	public static void main(String args[]){
		
	}
}
