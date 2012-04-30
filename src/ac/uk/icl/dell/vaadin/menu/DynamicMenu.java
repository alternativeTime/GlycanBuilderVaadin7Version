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


public interface DynamicMenu{
	/**
	 * Store a snapshot of the current menu
	 */
	public void saveState(CustomMenuBar.MenuItem item, Object obj);
	
	/**
	 * Remove all items not present in the snapshot
	 */
	public void restoreState(CustomMenuBar.MenuItem item, Object obj);
	
	public void removeMenuItem(CustomMenuBar.MenuItem item);
	
	public void setup();
	
	public void appendFinalItems();

	boolean isModified();

	void refresh();

	void setModified();
	
	public CustomMenuBar getMenuBar();
}
