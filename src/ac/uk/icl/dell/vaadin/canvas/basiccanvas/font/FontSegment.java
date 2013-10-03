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
