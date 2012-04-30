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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MessageDialogBox extends Window{
	private static final long serialVersionUID=5330733385455292529L;

	public MessageDialogBox(String message,Window parent){
		init("",message,parent);
	}
	
	public MessageDialogBox(String title, String message,Window parent){
		init(title,message,parent);
	}
	
	public void init(String title, String message,Window parent){
		Label messageLabel=new Label(message);
		
		Panel panel=new Panel();
		
		panel.getContent().addComponent(messageLabel);
		((VerticalLayout)panel.getContent()).setComponentAlignment(messageLabel, Alignment.MIDDLE_CENTER);
		
		getContent().addComponent(panel);
		
		setCaption(title);
		
		parent.addWindow(this);
		
		getContent().setWidth("400px");
		
		center();
	}
}
