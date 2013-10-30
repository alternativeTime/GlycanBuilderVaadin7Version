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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import ac.uk.icl.dell.vaadin.SimpleFileMenu;
import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanBuilder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClientMethodInvocation;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.server.ServerRpcManager;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class GlycanBuilderPage extends CustomComponent
{	
	private static final long serialVersionUID = -7879211835958495404L;
	
	GlycanBuilder theGlycanBuilder;
	
	public GlycanBuilderPage(){
		//final CustomLayout layout = new CustomLayout("header_content_footer");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		//SimpleFileMenu menu=new SimpleFileMenu();
		//layout.addComponent(menu, "header");

	
		//IGGAppLevelWindow window=(IGGAppLevelWindow)IGGApplication.getCurrentNavigableAppLevelWindow();
		theGlycanBuilder=new GlycanBuilder();
		theGlycanBuilder.setSizeFull();
	
		layout.addComponent(theGlycanBuilder);
		setCompositionRoot(layout);
		setSizeFull();
	
	}

//	@Override
//	public Component getComponent() {
//		return theGlycanBuilder.getComponent();
	//}
	
	
	
	
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
