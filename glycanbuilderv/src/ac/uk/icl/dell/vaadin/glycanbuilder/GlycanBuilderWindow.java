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
package ac.uk.icl.dell.vaadin.glycanbuilder;

import javax.servlet.annotation.WebServlet;

import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.GlycanRendererAWT;
import org.eurocarbdb.application.glycanbuilder.LogUtils;
import org.eurocarbdb.application.glycanbuilder.LoggerStorage;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageImpl;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageIndex;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Extension;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("ucdb_2011theme")
public class GlycanBuilderWindow extends UI{

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = GlycanBuilderWindow.class, widgetset = "ac.uk.icl.dell.vaadin.glycanbuilder.widgetset.GlycanbuilderWidgetset")
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = 4956233270191237091L;
	}
	private GlycanBuilder theBuilder; 
	private static final long serialVersionUID=-4407090778568443024L;
	private static LoggerStorage loggerStorage = new LoggerStorageImpl();
	private static LoggerStorageIndex logger = new LoggerStorageIndex() {
		
		@Override
		public LoggerStorage getLogger() {
			return loggerStorage;
		}
	};


	@Override
	public void init(VaadinRequest request){
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		theBuilder = new GlycanBuilder();
		layout.addComponentAsFirst(theBuilder);
		theBuilder.setSizeFull();
		setContent(layout);
	}
	
	public void addExtension (Extension extension)
	{
		super.addExtension(extension);
	}
	
	public static void initialiseStaticResources(){
		/**
		 * There are some static dictionary type resources that must be initialised for glycan parsing etc..
		 * 
		 * BuilderWorkspace takes care to initialise these resources if it's own static field "loaded" is false
		 * during initialisation.
		 * 
		 * Not all uses of these static resources are bounded by BuilderWorkspace instances, so we create a new
		 * instance here so that they are loaded.
		 * 
		 * BuilderWorkspace and all other GlycanBuilder and GlycoWorkbench code wasn't meant to be run within the
		 * context of a web server.  There are therefore potential issues anywhere a static field is not final and
		 * initialised on declaration - loaded is one such example (it needs synchronised access).
		 */
		@SuppressWarnings("unused")
		BuilderWorkspace workspace=new BuilderWorkspace(new GlycanRendererAWT());
		
		LogUtils.setLookupLogger(logger);
	}
	
}
