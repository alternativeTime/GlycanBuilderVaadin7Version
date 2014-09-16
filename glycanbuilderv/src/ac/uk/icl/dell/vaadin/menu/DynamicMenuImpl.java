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
package ac.uk.icl.dell.vaadin.menu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.vaadin.ui.MenuBar;


public abstract class DynamicMenuImpl extends MenuBar implements DynamicMenu{
	private static final long serialVersionUID=-1297565156689183635L;
	protected boolean modified=false;
	
	HashMap<Object,HashSet<MenuItem>> stateMap=new HashMap<Object,HashSet<MenuItem>>();
	
	@Override
	public void setModified(){
		modified=true;
	}
	
	@Override
	public boolean isModified(){
		return modified;
	}
	
	@Override
	public void refresh(){
		if(modified){
			removeItems();

			setup();
			appendFinalItems();

			modified=false;
		}
	}
	
	@Override
	public synchronized void saveState(MenuItem item, Object obj) {
		HashSet<MenuItem> items=new HashSet<MenuItem>();
		List<MenuItem> children=item.getChildren();
		if(children!=null && children.size()>0){
			items.addAll(item.getChildren());
		}	
		
		stateMap.put(obj,items);
	}

	@Override
	public synchronized void restoreState(MenuItem item, Object obj) {
		for(MenuItem childItem:item.getChildren().toArray(new MenuItem[1])){
			if(stateMap.get(obj).contains(childItem)==false){
				item.removeChild(childItem);
			}
		}
	}

	@Override
	public synchronized void removeMenuItem(MenuItem item) {
		MenuItem parent=item.getParent();
		if(parent==null){
			removeItem(item);
		}else{
			parent.removeChild(item);
		}
	}
}
