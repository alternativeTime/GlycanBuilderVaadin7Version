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

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public abstract class HeaderFooterFluidAppLevelWindow extends FluidAppLevelWindow{
	private static final long serialVersionUID=-5505006913843998562L;
	
	protected Component header;   // Fluid width
    protected Layout headerBand;  // Directly contains header. Goes from the left edge of the browser to the right edge.

    protected Component footer;   // Fluid width 
    protected Layout footerBand;  // Directly contains footer. Goes from the left edge of the browser to the right edge.

    @Override
    protected ComponentContainer createComponents() {
        VerticalLayout mainContainer=(VerticalLayout) getContent(); //set in FluidAppLevelWindow::attach via createMainLayout
        mainContainer.setSizeFull();
        
        header = createHeader();
        header.setWidth("100%");
        
        mainContainer.addComponent(header);
        
        footer=createFooter();
        footer.setWidth("100%");
        
        VerticalLayout pageBand=new VerticalLayout();
        pageBand.setSizeFull();
        
        mainContainer.addComponent(pageBand);
        mainContainer.setExpandRatio(pageBand, 1);
        
        mainContainer.addComponent(footer);

        return pageBand;
    }

    /** called by the template method createComponents. Override to provide your header component. */
    protected abstract Component createHeader();

    /** called by the template method createComponents. Override to provide your footer component */
    protected abstract Component createFooter();

    public Component getHeader() {
        return header;
    }

    public Layout getHeaderBand() {
        return headerBand;
    }

    public Component getFooter() {
        return footer;
    }

    public Layout getFooterBand() {
        return footerBand;
    }
}
