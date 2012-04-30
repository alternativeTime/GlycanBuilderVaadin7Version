/*
*   EuroCarbDB, a framework for carbohydrate bioinformatics
*
*   Copyright (c) 2006-2011, Eurocarb project, or third-party contributors as
*   indicated by the @author tags or express copyright attribution
*   statements applied by the authors.  
*
*   This copyrighted material is made available to anyone wishing to use, modify,
*   copy, or redistribute it subject to the terms and conditions of the GNU
*   Lesser General Public License, as published by the Free Software Foundation.
*   A copy of this license accompanies this distribution in the file LICENSE.txt.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
*   for more details.
*   
*   @author David R. Damerell (david@nixbioinf.org)
*/
package ac.uk.icl.dell.vaadin.menu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public abstract class DynamicMenuImpl extends CustomMenuBar implements DynamicMenu{
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
