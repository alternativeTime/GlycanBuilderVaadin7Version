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

import com.vaadin.ui.MenuBar;


public interface DynamicMenu{
	/**
	 * Store a snapshot of the current menu
	 */
	public void saveState(MenuBar.MenuItem item, Object obj);
	
	/**
	 * Remove all items not present in the snapshot
	 */
	public void restoreState(MenuBar.MenuItem item, Object obj);
	
	public void removeMenuItem(MenuBar.MenuItem item);
	
	public void setup();
	
	public void appendFinalItems();

	boolean isModified();

	void refresh();

	void setModified();
	
	public MenuBar getMenuBar();
}
