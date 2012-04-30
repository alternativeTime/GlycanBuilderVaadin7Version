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
import java.util.Collection;
import java.util.List;

import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.MassOptions;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class MassOptionsDialog extends Panel implements MassOptionsConfigurator.MassInput{
	private static final long serialVersionUID=8707959087794407560L;
	
	GridLayout layout=new GridLayout(3,15);
	Collection<Glycan> glycans;
	
	public MassOptionsDialog(Collection<Glycan> glycans, MassOptions massOptions){
		setContent(layout);
		this.glycans=glycans;
		
		config = new MassOptionsConfigurator(glycans, massOptions, this);
		
		layout.setSpacing(true);
		layout.setMargin(true);
	}

	@Override
	public String getReducingEnd() {
		return (String) reducingEndSelect.getValue();
	}

	@Override
	public String getOtherName() {
		return (String) otherNameField.getValue();
	}

	@Override
	public String getOtherMass() {
		return (String) otherMassField.getValue();
	}

	@Override
	public String getIsotope() {
		return (String) isotopeSelect.getValue();
	}

	@Override
	public String getDerivatization() {
		return (String) derivatizationSelect.getValue();
	}

	@Override
	public boolean isNegativeMode() {
		return (Boolean) negativeModeField.getValue();
	}

	@Override
	public int getHIonCount() {
		return MassOptionsDialog.getValueAsInt(hIonCountSelect);
	}

	@Override
	public int getNAIonCount() {
		return MassOptionsDialog.getValueAsInt(naIonCountSelect);
	}

	@Override
	public int getLIIonCount() {
		return MassOptionsDialog.getValueAsInt(liIonCountSelect);
	}

	@Override
	public int getKIonCount() {
		return MassOptionsDialog.getValueAsInt(kIonCountSelect);
	}

	@Override
	public int getexNAIonCount() {
		return MassOptionsDialog.getValueAsInt(exNAIonCountSelect);
	}

	@Override
	public int getexLIIonCount() {
		return MassOptionsDialog.getValueAsInt(exLIIonCountSelect);
	}

	@Override
	public int getexKIonCount() {
		return MassOptionsDialog.getValueAsInt(exKIonCountSelect);
	}

	@Override
	public void setIsotopValues(String[] list) {
		addAllItems(isotopeSelect,list);
	}

	@Override
	public void setDerivatizationValues(String[] list) {
		addAllItems(derivatizationSelect,list);
	}

	@Override
	public void setReducingEndValues(String[] list) {
		addAllItems(reducingEndSelect,list);
	}

	@Override
	public void setHIonRange(Object[] list) {
		addAllItems(hIonCountSelect,list);
	}

	@Override
	public void setNAIonRange(Object[] list) {
		addAllItems(naIonCountSelect,list);
	}

	@Override
	public void setLIIonRange(Object[] list) {
		addAllItems(liIonCountSelect,list);		
	}

	@Override
	public void setKIonRange(Object[] list) {
		addAllItems(kIonCountSelect,list);
	}

	@Override
	public void setexNAIonRange(Object[] list) {
		addAllItems(exNAIonCountSelect,list);
	}

	@Override
	public void setexLIIonRange(Object[] list) {
		addAllItems(exLIIonCountSelect,list);
	}

	@Override
	public void setexKIonRange(Object[] list) {
		addAllItems(exKIonCountSelect,list);
	}

	@Override
	public void setSelectedIsotope(String isotope) {
		isotopeSelect.setValue(isotope);
	}

	@Override
	public void setSelectedDerivatization(String derivatization) {
		derivatizationSelect.setValue(derivatization);
	}

	@Override
	public void setSelectedReducingEnd(String reducingEnd) {
		reducingEndSelect.setValue(reducingEnd);		
	}

	@Override
	public void setOtherName(String otherName) {
		otherNameField.setValue(otherName);
	}

	@Override
	public void setOtherMass(String otherMass) {
		otherMassField.setValue(otherMass);
	}

	@Override
	public void setNegativeMode(boolean isNegative) {
		negativeModeField.setValue(isNegative);
	}

	@Override
	public void setHIonCount(int count) {
		hIonCountSelect.setValue(count);
	}

	@Override
	public void setNAIonCount(int count) {
		naIonCountSelect.setValue(count);
	}

	@Override
	public void setLIIonCount(int count) {
		liIonCountSelect.setValue(count);
	}

	@Override
	public void setKIonCount(int count) {
		kIonCountSelect.setValue(count);
	}

	@Override
	public void setexNAIonCount(int count) {
		exNAIonCountSelect.setValue(count);
	}

	@Override
	public void setexLIIonCount(int count) {
		exLIIonCountSelect.setValue(count);
	}

	@Override
	public void setexKIonCount(int count) {
		exKIonCountSelect.setValue(count);
	}

	@Override
	public void enableIsotopField(boolean enable) {
		isotopeSelect.setEnabled(enable);
	}

	@Override
	public void enableOtherMassField(boolean enable) {
		otherMassField.setEnabled(enable);
	}

	@Override
	public void enableOtherNameField(boolean enable) {
		otherNameField.setEnabled(enable);
	}
	
	Select isotopeSelect;
	Select derivatizationSelect;
	Select reducingEndSelect;
	
	TextField otherNameField;
	TextField otherMassField;
	
	CheckBox negativeModeField;
	
	Select hIonCountSelect, exHIonCountSelect;
	Select naIonCountSelect, exNAIonCountSelect;
	Select liIonCountSelect, exLIIonCountSelect;
	Select kIonCountSelect, exKIonCountSelect;
	Select clIonCountSelect, exClIonCountSelect;
	Select h2po4IonCountSelect, exH2po4IonCountSelect;
	private MassOptionsConfigurator config;
	
	@Override
	public void initComponents() {
		isotopeSelect=new Select();
		isotopeSelect.addStyleName("igg-mass-options-panel-item");
		isotopeSelect.setNullSelectionAllowed(false);
		isotopeSelect.setNewItemsAllowed(false);
		isotopeSelect.setWidth("120px");
		
		derivatizationSelect=new Select();
		derivatizationSelect.addStyleName("igg-mass-options-panel-item");
		derivatizationSelect.setNullSelectionAllowed(false);
		derivatizationSelect.setNewItemsAllowed(false);
		derivatizationSelect.setWidth("120px");
		
		reducingEndSelect=new Select();
		reducingEndSelect.addStyleName("igg-mass-options-panel-item");
		reducingEndSelect.setNullSelectionAllowed(false);
		reducingEndSelect.setImmediate(true);
		reducingEndSelect.setNewItemsAllowed(false);
		reducingEndSelect.setWidth("120px");
		
		reducingEndSelect.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID=1067195208212460144L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(((String)reducingEndSelect.getValue()).equals("Other...")){
					otherNameField.setEnabled(true);
					otherMassField.setEnabled(true);
				}else{
					otherNameField.setEnabled(false);
					otherMassField.setEnabled(false);
				}
			}
		});
		
		
		otherNameField=new TextField();
		otherNameField.addStyleName("igg-mass-options-panel-item");
		otherNameField.setWidth("120px");
		
		otherMassField=new TextField();
		otherMassField.addStyleName("igg-mass-options-panel-item");
		otherMassField.setWidth("120px");
		
		negativeModeField=new CheckBox();
		negativeModeField.addStyleName("igg-mass-options-panel-item");
	
		hIonCountSelect=new Select("#H ions");
		hIonCountSelect.addStyleName("igg-mass-options-panel-item");
		hIonCountSelect.setNewItemsAllowed(false);
		hIonCountSelect.setWidth("120px");
		
		naIonCountSelect=new Select("#Na ions");
		naIonCountSelect.addStyleName("igg-mass-options-panel-item");
		naIonCountSelect.setNewItemsAllowed(false);
		naIonCountSelect.setWidth("120px");
		
		exNAIonCountSelect=new Select("ext. #Na ions");
		exNAIonCountSelect.addStyleName("igg-mass-options-panel-item");
		exNAIonCountSelect.setNewItemsAllowed(false);
		exNAIonCountSelect.setWidth("120px");
		
		liIonCountSelect=new Select("#Li ions");
		liIonCountSelect.addStyleName("igg-mass-options-panel-item");
		liIonCountSelect.setNewItemsAllowed(false);
		liIonCountSelect.setWidth("120px");
		
		exLIIonCountSelect=new Select("ext. #Li ions");
		exLIIonCountSelect.addStyleName("igg-mass-options-panel-item");
		exLIIonCountSelect.setNewItemsAllowed(false);
		exLIIonCountSelect.setWidth("120px");
		
		kIonCountSelect=new Select("#K ions");
		kIonCountSelect.addStyleName("igg-mass-options-panel-item");
		kIonCountSelect.setNewItemsAllowed(false);
		kIonCountSelect.setWidth("120px");
		
		exKIonCountSelect=new Select("ext. #K ions");
		exKIonCountSelect.addStyleName("igg-mass-options-panel-item");
		exKIonCountSelect.setNewItemsAllowed(false);
		exKIonCountSelect.setWidth("120px");
		
		clIonCountSelect=new Select("#Cl ions");
		clIonCountSelect.addStyleName("igg-mass-options-panel-item");
		clIonCountSelect.setNewItemsAllowed(false);
		clIonCountSelect.setWidth("120px");
		
		exClIonCountSelect=new Select("ext. #Cl ions");
		exClIonCountSelect.addStyleName("igg-mass-options-panel-item");
		exClIonCountSelect.setNewItemsAllowed(false);
		exClIonCountSelect.setWidth("120px");
		
		h2po4IonCountSelect=new Select("#H2PO4 ions");
		h2po4IonCountSelect.addStyleName("igg-mass-options-panel-item");
		h2po4IonCountSelect.setNewItemsAllowed(false);
		h2po4IonCountSelect.setWidth("120px");
		
		exH2po4IonCountSelect=new Select("ext. #H2PO4 ions");
		exH2po4IonCountSelect.addStyleName("igg-mass-options-panel-item");
		exH2po4IonCountSelect.setNewItemsAllowed(false);
		exH2po4IonCountSelect.setWidth("120px");
		
		//column,row
		layout.addComponent(new Label("Isotope"),0,0,0,0);
		layout.addComponent(isotopeSelect,1,0,1,0);
		layout.addComponent(new Label("Derivatization"),0,1,0,1);
		layout.addComponent(derivatizationSelect,1,1,1,1);
		layout.addComponent(new Label("Reducing end"),0,2,0,2);
		layout.addComponent(reducingEndSelect,1,2,1,2);
		
		{
			Label nameLabel=new Label("name");
			nameLabel.setWidth("40px");
			layout.addComponent(nameLabel,1,3,1,3);
			layout.addComponent(otherNameField,2,3,2,3);
		
			layout.setComponentAlignment(nameLabel, Alignment.MIDDLE_RIGHT);
		}
		
		{
			Label massLabel=new Label("mass");
			massLabel.setWidth("40px");
			layout.addComponent(massLabel,1,4,1,4);
			layout.addComponent(otherMassField,2,4,2,4);	
			layout.setComponentAlignment(massLabel, Alignment.MIDDLE_RIGHT);
		}
		
		
		
		int row=5;
		
		{
			Label divider=new Label("<hr/>",Label.CONTENT_XHTML);
		
			layout.addComponent(divider,0,row,2,row++);
		}
		
		layout.addComponent(new Label("Negative mode"),0,row,0,row);
		layout.addComponent(negativeModeField,1,row,1,row++);
		
		layout.addComponent(hIonCountSelect,0,row,0,row++);
		
		layout.addComponent(naIonCountSelect,0,row,0,row);
		
		layout.addComponent(exNAIonCountSelect,1,row,1,row++);
		
		layout.addComponent(liIonCountSelect,0,row,0,row);
		
		layout.addComponent(exLIIonCountSelect,1,row,1,row++);
		
		layout.addComponent(kIonCountSelect,0,row,0,row);
		
		layout.addComponent(exKIonCountSelect,1,row,1,row++);
		
		{
			Label divider=new Label("<hr/>",Label.CONTENT_XHTML);
		
			layout.addComponent(divider,0,row,2,row++);
		}
		
		layout.addComponent(clIonCountSelect,0,row,0,row);
		
		layout.addComponent(exClIonCountSelect,1,row,1,row++);
		
		layout.addComponent(h2po4IonCountSelect,0,row,0,row);
		
		layout.addComponent(exH2po4IonCountSelect,1,row,1,row++);
		
		NativeButton update=new NativeButton("Apply mass options");
		update.addListener(new ClickListener(){
			private static final long serialVersionUID=-6188200798103156691L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					config.retrieveData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fireMassOptionsChanged(config.getMassOptions(),glycans);
			}
		});
		
		layout.addComponent(update,0,row,0,row);
	}
	
	public void addAllItems(AbstractSelect select,Object [] objects){
		for(Object obj:objects){
			select.addItem(obj);
		}
	}
	
	public static int getValueAsInt(AbstractSelect select){
		return (Integer) select.getValue();
	}
	
	public interface MassOptionListener {
		public void massOptions(MassOptions massOptions,Collection<Glycan> structures);
	}
	
	List<MassOptionListener> massOptionListeners=new ArrayList<MassOptionListener>();
	
	public void addMassOptionListener(MassOptionListener listener){
		massOptionListeners.add(listener);
	}
	
	public void removeMassOptionListener(MassOptionListener listener){
		massOptionListeners.remove(listener);
	}
	
	public void fireMassOptionsChanged(MassOptions massOptions,Collection<Glycan> structures){
		for(MassOptionListener listener:massOptionListeners){
			listener.massOptions(massOptions,structures);
		}
	}

	@Override
	public int getCLIonCount() {
		return MassOptionsDialog.getValueAsInt(clIonCountSelect);
	}

	@Override
	public int getexCLIonCount() {
		return MassOptionsDialog.getValueAsInt(exClIonCountSelect);
	}

	@Override
	public void setCLIonRange(Object[] list) {
		addAllItems(clIonCountSelect,list);
	}

	@Override
	public void setexCLIonRange(Object[] list) {
		addAllItems(exClIonCountSelect,list);
	}

	@Override
	public void setCLIonCount(int count) {
		clIonCountSelect.setValue(count);
	}

	@Override
	public void setexCLIonCount(int count) {
		exClIonCountSelect.setValue(count);
	}
	
	@Override
	public int getH2PO4IonCount() {
		return MassOptionsDialog.getValueAsInt(h2po4IonCountSelect);
	}

	@Override
	public int getexH2PO4IonCount() {
		return MassOptionsDialog.getValueAsInt(exH2po4IonCountSelect);
	}

	@Override
	public void setH2PO4IonRange(Object[] list) {
		addAllItems(h2po4IonCountSelect,list);
	}

	@Override
	public void setexH2PO4IonRange(Object[] list) {
		addAllItems(exH2po4IonCountSelect,list);
	}

	@Override
	public void setH2PO4IonCount(int count) {
		h2po4IonCountSelect.setValue(count);
	}

	@Override
	public void setexH2PO4IonCount(int count) {
		exH2po4IonCountSelect.setValue(count);
	}
}
