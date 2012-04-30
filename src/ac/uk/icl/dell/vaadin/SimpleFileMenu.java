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
package ac.uk.icl.dell.vaadin;

import org.vaadin.navigator7.NavigableApplication;

import ac.uk.icl.dell.vaadin.menu.ApplicationMenu;
import ac.uk.icl.dell.vaadin.menu.CustomMenuBar;
import ac.uk.icl.dell.vaadin.menu.DynamicMenuImpl;

public class SimpleFileMenu extends DynamicMenuImpl implements ApplicationMenu{
	private static final long serialVersionUID=4968428219495753712L;
	
	private MenuItem fileMenuItem;
	private MenuItem editMenuItem;
	private MenuItem viewMenuItem;

	private MenuItem restartFileMenuItem;
	
	public SimpleFileMenu(){
		setup();
	}

	@Override
	public CustomMenuBar.MenuItem getFileMenu(){
		return fileMenuItem;
	}

	@Override
	public MenuItem getEditMenu() {
		return editMenuItem;
	}

	@Override
	public MenuItem getViewMenu() {
		return viewMenuItem;
	}

	@Override
	public CustomMenuBar getMenuBar() {
		return this;
	}

	@Override
	public void setup() {
		fileMenuItem=addItem("File",null);
		editMenuItem=addItem("Edit",null);
		viewMenuItem=addItem("View",null);
		
		fileMenuItem.setVisible(false);
		editMenuItem.setVisible(false);
		viewMenuItem.setVisible(false);
		
		restartFileMenuItem=getFileMenu().addItem("Restart", new Command(){
			private static final long serialVersionUID = -5533996368035711854L;

			@Override
			public void menuSelected(MenuItem selectedItem){
				NavigableApplication.getCurrent().close();
			}
		});
	}

	@Override
	public MenuItem getRestartFileMenuItem() {
		return restartFileMenuItem;
	}

	@Override
	public void appendFinalItems() {
		// TODO Auto-generated method stub
		
	}
}