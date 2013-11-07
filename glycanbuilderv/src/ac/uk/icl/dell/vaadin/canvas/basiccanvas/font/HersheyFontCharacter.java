/* 
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

import java.util.List;

class HersheyFontCharacter{
	List<Float[]> vertices;
	float width;
	char character;
	
	public HersheyFontCharacter(List<Float[]> vertices, int width) {
		this.vertices=vertices;
		this.width=width;
	}
}