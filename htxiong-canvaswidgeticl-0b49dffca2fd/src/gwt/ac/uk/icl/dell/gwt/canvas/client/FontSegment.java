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
 * Class defining part (segment) of a character 
 *
 */
@SuppressWarnings("serial")
public class FontSegment extends ArrayList<FontPoint> implements IsSerializable {

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

}
