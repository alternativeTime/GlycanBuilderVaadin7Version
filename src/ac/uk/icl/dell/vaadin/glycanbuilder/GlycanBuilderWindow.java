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


import ac.uk.icl.dell.vaadin.SimpleFileMenu;

import com.vaadin.Application;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Window;

public class GlycanBuilderWindow extends Application{
	private static final long serialVersionUID=-4407090778568443024L;

	@Override
	public void init(){
		Window mainWindow=new Window();
		setMainWindow(mainWindow);
		setTheme("ucdb_2011theme");
		
		SimpleFileMenu menu=new SimpleFileMenu();
		
		CustomLayout layout=new CustomLayout("header_content_footer_layout");
		layout.addComponent(menu, "header");
	}
}
