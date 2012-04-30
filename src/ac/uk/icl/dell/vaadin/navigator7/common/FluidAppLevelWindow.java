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
package ac.uk.icl.dell.vaadin.navigator7.common;

import org.vaadin.navigator7.Navigator;
import org.vaadin.navigator7.window.NavigableAppLevelWindow;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public abstract class FluidAppLevelWindow extends NavigableAppLevelWindow{
	private static final long serialVersionUID=-7045300488515169537L;

	@Override
	public void attach() {
		Layout main = createMainLayout();
		this.setContent(main);  
		
		// Must be done after calling this.setConent(main), as for any component added to the window.
		this.navigator = new Navigator();
		this.addComponent(navigator);

		pageContainer = createComponents();
	}
	
	@Override
	protected Layout createMainLayout() {
		return new VerticalLayout();
	}
}