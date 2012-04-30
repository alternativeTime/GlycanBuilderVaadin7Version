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
package ac.uk.icl.dell.vaadin.navigator7.pages;

import java.net.MalformedURLException;
import java.net.URL;

import org.vaadin.navigator7.Page;
import org.vaadin.navigator7.window.PageWrapper;

import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanBuilder;
import ac.uk.icl.dell.vaadin.menu.CustomMenuBar;
import ac.uk.icl.dell.vaadin.navigator7.IGGAppLevelWindow;
import ac.uk.icl.dell.vaadin.navigator7.IGGApplication;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Page
public class GlycanBuilderPage implements PageWrapper{
	VerticalLayout layout;
	
	GlycanBuilder theGlycanBuilder;
	
	public GlycanBuilderPage(){
		layout=new VerticalLayout();
		layout.setSizeFull();
		
		IGGAppLevelWindow window=(IGGAppLevelWindow)IGGApplication.getCurrentNavigableAppLevelWindow();
		theGlycanBuilder=new GlycanBuilder(window.getApplicationMenu());
	}

	@Override
	public Component getComponent() {
		return theGlycanBuilder.getComponent();
	}
	
	
	
	
	//CssLayout layout;
//	GlycanBuilder theGlycanBuilder;
//	
//	public GlycanBuilderPage(){
//		IGGAppLevelWindow window=(IGGAppLevelWindow)IGGApplication.getCurrentNavigableAppLevelWindow();
//		theGlycanBuilder=new GlycanBuilder(window.getApplicationMenu());
//		
//		
//		CustomMenuBar.MenuItem helpItem=theGlycanBuilder.getMenuBar().addItem("Help",null);
//		helpItem.addItem("Manual", new CustomMenuBar.Command(){
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = -7641505913124834642L;
//
//			@Override
//			public void menuSelected(CustomMenuBar.MenuItem selectedItem){
//				Window window=new Window();
//				window.setHeight("60%");
//				window.setWidth("60%");
//				
//				window.setCaption("Manual");
//				try{
//					Embedded browser=new Embedded("", new ExternalResource(new URL("http://wiki.glycoworkbench.org")));
//					browser.setType(Embedded.TYPE_BROWSER);
//					browser.setSizeFull();
//					
//					window.getContent().setSizeFull();
//					window.getContent().addComponent(browser);
//				}catch(MalformedURLException e){
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				IGGApplication.getCurrentNavigableAppLevelWindow().addWindow(window);
//			}
//		});
//		
//		helpItem.addItem("About", new CustomMenuBar.Command(){
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1552280257083917640L;
//
//			@Override
//			public void menuSelected(CustomMenuBar.MenuItem selectedItem){
//				Window window=new Window();
//				window.setHeight("30%");
//				window.setWidth("30%");
//				
//				window.setCaption("About:");
//				
//				try{
//					Embedded browser=new Embedded("", new ExternalResource(new URL("http://wiki.glycoworkbench.org")));
//					browser.setType(Embedded.TYPE_BROWSER);
//					browser.setSizeFull();
//					
//					window.getContent().setSizeFull();
//					window.getContent().addComponent(browser);
//				}catch(MalformedURLException e){
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				IGGApplication.getCurrentNavigableAppLevelWindow().addWindow(window);
//			}
//		});
//		
//		theGlycanBuilder.getVaadinGlycanCanvas().setLocalResourceWatcher(((IGGApplication)IGGApplication.getCurrent()));
//	}
//
//	@Override
//	public Component getComponent(){
//		return theGlycanBuilder;
//	}
}
