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

import java.util.ArrayList;
import java.util.List;

import org.vaadin.weelayout.WeeLayout;
import org.vaadin.weelayout.WeeLayout.Direction;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Select;

public class ImportStructureFromStringDialog extends Panel {
	private static final long serialVersionUID=-5458440361659452562L;
	
	Select importTypeSelectField;
	GlycanCanvas theCanvas;
	RichTextArea sequenceInputField;
	NativeButton ok;
	WeeLayout layout;
	
	List<UserInputEndedListener> userInputEndedListeners=new ArrayList<UserInputEndedListener>();
	
	ImportStructureFromStringDialog(GlycanCanvas canvas){
		theCanvas=canvas;
		
		layout=new WeeLayout(Direction.VERTICAL);
		
		setContent(layout);
		
		initComponents();
		installClickListeners();
		layoutComponents();
		
		setSizeFull();
		layout.setSizeFull();
	}
	
	private String sequence;
	private void initComponents(){
		importTypeSelectField=new Select("Sequence format",theCanvas.getImportFormats());
		importTypeSelectField.setNewItemsAllowed(false);
		importTypeSelectField.setNullSelectionAllowed(false);
		
		sequenceInputField=new RichTextArea();
		sequenceInputField.setImmediate(true);
		sequenceInputField.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID=-6654910749910048984L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				sequenceInputField.commit();
				sequence=(String)event.getProperty().getValue();
			}
		});
		
		sequenceInputField.setHeight("90%");
		sequenceInputField.setWidth("100%");
		
		sequenceInputField.addStyleName("hide-richtext-toolbar");
		
		ok=new NativeButton("Import");
	}
	
	private void installClickListeners(){
		ok.addListener(new ClickListener(){
			private static final long serialVersionUID=4434491616110701535L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireUserInputFinished(false);
			}
		});
	}
	
	private void layoutComponents(){
		layout.addComponent(sequenceInputField);
		layout.addComponent(importTypeSelectField);
		layout.addComponent(ok);
		
		layout.setComponentAlignment(sequenceInputField, Alignment.TOP_CENTER);
		layout.setComponentAlignment(importTypeSelectField, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
	}
	
	public void addListener(UserInputEndedListener listener){
		userInputEndedListeners.add(listener);
	}
	
	public void removeListener(UserInputEndedListener listener){
		userInputEndedListeners.remove(listener);
	}
	
	public void fireUserInputFinished(boolean cancelled){
		for(UserInputEndedListener listener:userInputEndedListeners){
			listener.done(cancelled);
		}
	}
	
	public String getSequenceFormat(){
		return (String) importTypeSelectField.getValue(); 
	}
	
	public String getSequenceString(){
		sequence=sequence.replaceAll("<pre>", "");
		sequence=sequence.replaceAll("</pre>", "");
		sequence=sequence.replaceAll("</div>", "\n");
		sequence=sequence.replaceAll("<div>", "");
		sequence=sequence.replaceAll("<br>", "");
		sequence=sequence.replaceAll("&lt;","<");
		sequence=sequence.replaceAll("&gt;",">");
		sequence=sequence.replaceAll("&nbsp;","\t");
	
		sequence=sequence.replaceAll("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">", "");
		return sequence;
	}
}
