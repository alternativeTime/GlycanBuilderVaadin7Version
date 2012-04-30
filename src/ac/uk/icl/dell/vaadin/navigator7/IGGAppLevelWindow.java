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
package ac.uk.icl.dell.vaadin.navigator7;

import org.vaadin.navigator7.window.PageWrapper;

import ac.uk.icl.dell.vaadin.SimpleFileMenu;
import ac.uk.icl.dell.vaadin.menu.ApplicationMenu;
import ac.uk.icl.dell.vaadin.navigator7.common.HeaderFooterFluidAppLevelWindow;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class IGGAppLevelWindow  extends HeaderFooterFluidAppLevelWindow{
	private static final long serialVersionUID=1916786084379459856L;
	
	private SimpleFileMenu theMainMenu;

	public IGGAppLevelWindow(){
		setSizeFull();
		getContent().setSizeFull();
		setScrollable(false);
	}
	
	@Override
	protected Component createHeader() {
		return theMainMenu=new SimpleFileMenu();
	}

	@Override
	protected Component createFooter() {
		HorizontalLayout layout=new HorizontalLayout();
		layout.setWidth("100%");
		
		layout.addStyleName("white-background");
		
		layout.addComponent(new Label("GlycanBuilder"));
		
		return layout;
	}
	
	synchronized public void changePage(PageWrapper pageParam) {
        pageContainer.removeAllComponents();  // It is supposed to contain only the previous page (which we don't know the class at all, except it's a Component).
        this.page = pageParam;
        
        pageContainer.addComponent(page.getComponent());
    }

	public ApplicationMenu getApplicationMenu(){
		return theMainMenu;
	}
}
