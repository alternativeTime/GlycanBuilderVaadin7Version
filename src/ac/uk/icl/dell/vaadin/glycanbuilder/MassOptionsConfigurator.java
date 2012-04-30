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

import java.util.Collection;

import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.MassOptions;
import org.eurocarbdb.application.glycanbuilder.MassUtils;
import org.eurocarbdb.application.glycanbuilder.ResidueDictionary;
import org.eurocarbdb.application.glycanbuilder.ResidueType;
import org.eurocarbdb.application.glycanbuilder.Union;

public  class MassOptionsConfigurator {
	private MassOptions common_options = new MassOptions();
    @SuppressWarnings("unused")
	private Collection<Glycan> structures = null;
    
    private MassInput inputDevice;
    
    public MassOptionsConfigurator(Collection<Glycan> _structures, MassOptions _default, MassInput inputDevice) {
    	this.inputDevice=inputDevice;
    	
    	structures = _structures;
    	setCommonOptions(_structures,_default);

        inputDevice.initComponents();
        initData();
        enableItems();
    }
    
    public MassOptions getMassOptions() {
    	return common_options;
    }

    private Object[] generateValues(int min, int max, boolean include_und) {
    	if(include_und){
    		Object[] values = new Object[1+(max-min+1)];    

    		values[0] = "---";
    		for(int i=min; i<=max; i++){
    			values[i-min+1] = Integer.valueOf(i);
    		}
    		
    		return values;
    	}

    	Object[] values = new Object[max-min+1];    
    	for(int i=min; i<=max; i++){
    		values[i-min] = Integer.valueOf(i);
    	}
    	return values;
    }
    
    private void setCommonOptions(Collection<Glycan> structures, MassOptions _default) {
        if(structures==null || structures.size()==0){
            common_options = _default;
            return;
        }

        common_options = new MassOptions(true);
        
        boolean first = true;
        for(Glycan structure : structures){
            MassOptions structure_options = structure.getMassOptions();
            if(first){
            	common_options = structure_options.clone();
            	first = false;
            }else {
            	common_options.merge(structure_options);
            }
        }
    }
    
    public boolean retrieveData() throws Exception{
        if(inputDevice.getReducingEnd().equals("Other...")){
            ResidueType red_end_type = ResidueType.createOtherReducingEnd(inputDevice.getOtherName(),Double.valueOf(inputDevice.getOtherMass()));
            if( ResidueDictionary.findResidueType(red_end_type.getResidueName())!=null ) {
            	throw new Exception("The name specified for the reducing end is already existing.");
            }
            common_options.REDUCING_END_TYPE = red_end_type;
        }else{
            common_options.REDUCING_END_TYPE = ResidueDictionary.findResidueType(inputDevice.getReducingEnd());
        }
        
        common_options.ISOTOPE = inputDevice.getIsotope();
        common_options.DERIVATIZATION = inputDevice.getDerivatization();
        
        int multiplier = inputDevice.isNegativeMode() ?-1 :1;
        
        common_options.ION_CLOUD.set(MassOptions.ION_H,multiplier*inputDevice.getHIonCount());
        common_options.ION_CLOUD.set(MassOptions.ION_NA,multiplier*inputDevice.getNAIonCount());
        common_options.ION_CLOUD.set(MassOptions.ION_LI,multiplier*inputDevice.getLIIonCount());
        common_options.ION_CLOUD.set(MassOptions.ION_K,multiplier*inputDevice.getKIonCount());
        
        //Always set quantity of ions positive when adding negatively charged ions to cloud
        common_options.ION_CLOUD.set(MassOptions.ION_CL,1*inputDevice.getCLIonCount());
        common_options.ION_CLOUD.set(MassOptions.ION_H2PO4,1*inputDevice.getH2PO4IonCount());
    
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_H,
        		-inputDevice.getexNAIonCount()
        		-inputDevice.getexLIIonCount()
        		-inputDevice.getexKIonCount()
        		-inputDevice.getexCLIonCount()
        		-inputDevice.getexH2PO4IonCount()
        );
        
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_NA,inputDevice.getexNAIonCount());
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_LI,inputDevice.getexLIIonCount());
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_K,inputDevice.getexKIonCount());
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_CL,inputDevice.getexCLIonCount());
        common_options.NEUTRAL_EXCHANGES.set(MassOptions.ION_H2PO4,inputDevice.getexH2PO4IonCount());
        
        return true;
    }
    
    
    
    
    private void initData(){
    	inputDevice.setIsotopValues(new String[] {"---",MassOptions.ISOTOPE_MONO,MassOptions.ISOTOPE_AVG});
    	inputDevice.setDerivatizationValues(new String[] {"---",MassOptions.NO_DERIVATIZATION,MassOptions.PERMETHYLATED,MassOptions.PERDMETHYLATED, MassOptions.HEAVYPERMETHYLATION,MassOptions.PERACETYLATED,MassOptions.PERDACETYLATED});
    	inputDevice.setReducingEndValues(new Union<String>().and("---").and(ResidueDictionary.getReducingEndsString()).and("Other...").toArray(new String[0]));
    	
    	inputDevice.setNAIonRange(generateValues(0,10,true));
    	inputDevice.setHIonRange(generateValues(0,10,true));
    	inputDevice.setLIIonRange(generateValues(0,10,true));
    	inputDevice.setKIonRange(generateValues(0,10,true));
    	inputDevice.setCLIonRange(generateValues(0,10,true));
    	inputDevice.setH2PO4IonRange(generateValues(0,10,true));

    	inputDevice.setexNAIonRange(generateValues(0,50,true));
    	inputDevice.setexLIIonRange(generateValues(0,50,true));
    	inputDevice.setexKIonRange(generateValues(0,50,true));
    	inputDevice.setexCLIonRange(generateValues(0,50,true));
    	inputDevice.setexH2PO4IonRange(generateValues(0,50,true));

    	inputDevice.setSelectedIsotope(common_options.ISOTOPE);
    	inputDevice.setSelectedDerivatization(common_options.DERIVATIZATION);

        if(common_options.REDUCING_END_TYPE==null){
        	inputDevice.setSelectedReducingEnd("---");
        	inputDevice.setOtherName("");
        	inputDevice.setOtherMass("0");
        }else if(common_options.REDUCING_END_TYPE.isCustomType()) {
        	inputDevice.setSelectedReducingEnd("Other...");
        	inputDevice.setOtherName(common_options.REDUCING_END_TYPE.getResidueName());
        	inputDevice.setOtherMass("" + (common_options.REDUCING_END_TYPE.getResidueMassMain()-MassUtils.water.getMainMass()));
        }else{
        	inputDevice.setSelectedReducingEnd(common_options.REDUCING_END_TYPE.getName());
        	inputDevice.setOtherName("");
        	inputDevice.setOtherMass("0");
        }
        
        inputDevice.setNegativeMode(common_options.ION_CLOUD.isNegative());
        
        inputDevice.setHIonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_H)));
        inputDevice.setNAIonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_NA)));
        inputDevice.setLIIonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_LI)));
        inputDevice.setKIonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_K)));
        inputDevice.setCLIonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_CL)));
        inputDevice.setH2PO4IonCount(Math.abs(common_options.ION_CLOUD.get(MassOptions.ION_H2PO4)));

        inputDevice.setexNAIonCount(common_options.NEUTRAL_EXCHANGES.get(MassOptions.ION_NA));
        inputDevice.setexLIIonCount(common_options.NEUTRAL_EXCHANGES.get(MassOptions.ION_LI));
        inputDevice.setexKIonCount(common_options.NEUTRAL_EXCHANGES.get(MassOptions.ION_K));
        inputDevice.setexCLIonCount(common_options.NEUTRAL_EXCHANGES.get(MassOptions.ION_CL));
        inputDevice.setexH2PO4IonCount(common_options.NEUTRAL_EXCHANGES.get(MassOptions.ION_H2PO4));
    }
    
    
    
    private void enableItems() {
    	inputDevice.enableIsotopField(false);
    	
    	inputDevice.enableOtherNameField(inputDevice.getReducingEnd().equals("Other..."));
    	inputDevice.enableOtherMassField(inputDevice.getReducingEnd().equals("Other..."));
    }
    
    public interface MassInput{
    	 String getReducingEnd();
         String getOtherName();
         String getOtherMass();
         String getIsotope();
         String getDerivatization();
         boolean isNegativeMode();
        
         int getHIonCount();
         int getNAIonCount();
         int getLIIonCount();
         int getKIonCount();
         int getCLIonCount();
         int getH2PO4IonCount();
        
         int getexNAIonCount();
         int getexLIIonCount();
         int getexKIonCount();
         int getexCLIonCount();
         int getexH2PO4IonCount();
        
         void initComponents();
        
         void setIsotopValues(String[] list);
         void setDerivatizationValues(String[] list);
         void setReducingEndValues(String[] list);
        
         void setHIonRange(Object[] list);
         void setNAIonRange(Object[] list);
         void setLIIonRange(Object[] list);
         void setKIonRange(Object[] list);
         void setCLIonRange(Object[] list);
         void setH2PO4IonRange(Object[] list);
        
         void setexNAIonRange(Object[] list);
         void setexLIIonRange(Object[] list);
         void setexKIonRange(Object[] list);
         void setexCLIonRange(Object[] list);
         void setexH2PO4IonRange(Object[] list);
        
         void setSelectedIsotope(String isotope);
         void setSelectedDerivatization(String derivatization);
         void setSelectedReducingEnd(String reducingEnd);
         void setOtherName(String otherName);
         void setOtherMass(String otherMass);
         void setNegativeMode(boolean isNegative);
        
         void setHIonCount(int count);
         void setNAIonCount(int count);
         void setLIIonCount(int count);
         void setKIonCount(int count);
         void setCLIonCount(int count);
         void setH2PO4IonCount(int count);
        
         void setexNAIonCount(int count);
         void setexLIIonCount(int count);
         void setexKIonCount(int count);
         void setexCLIonCount(int count);
         void setexH2PO4IonCount(int count);
        
         void enableIsotopField(boolean enable);
         void enableOtherMassField(boolean enable);
         void enableOtherNameField(boolean enable);
    }
}