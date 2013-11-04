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
package ac.uk.icl.dell.vaadin.navigator7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.navigator7.NavigableApplication;
import org.vaadin.navigator7.Navigator;
import org.vaadin.navigator7.window.NavigableAppLevelWindow;

import ac.uk.icl.dell.vaadin.LocalResourceWatcher;
import ac.uk.icl.dell.vaadin.MessageDialogBox;

import com.vaadin.Application;
import com.vaadin.terminal.Terminal;

public class IGGApplication extends NavigableApplication implements  LocalResourceWatcher{
	private static final long serialVersionUID=4370691195660076222L;
	
	private List<File> localFilesAttachedToSession=new ArrayList<File>();
	
	public IGGApplication(){
		setTheme("ucdb_2011theme");
	}
	
	@Override
	public NavigableAppLevelWindow createNewNavigableAppLevelWindow() {
		return new IGGAppLevelWindow();
	}
	
	public static Navigator getNavigator(){
		return getCurrentNavigableAppLevelWindow().getNavigator();
	}
	
	@Override
	public void addLocalResource(File file) {
		localFilesAttachedToSession.add(file);
	}
	
	@Override
	public void close(){
		for(File file:localFilesAttachedToSession){
			System.out.println("Deleting file: "+file.getPath());
			file.delete();
		}
		
		super.close();
	}

	@Override
	public void removeLocalResource(File file) {
		localFilesAttachedToSession.remove(file);
	}
	
    @Override
    public void transactionStart(Application application, Object transactionData) {
        if (this != application) { // It does not concern us.
            return;
        }
        
        super.transactionStart(application, transactionData);
    }
    
    /**
     * Transaction per-request
     * 
     * The Vaadin framework presents a very different environment than Structs used for the EUROCarbDB and 
     * UniCarb-DB sites.  Both of these existing databases use the simple transaction per-request technique
     * to handle database work.  We will start of the with the same model, adapting it if it becomes a problem.
     * 
     * In particular, note that the nature of the Vaadin framework means that we have many more requests, and
     * thus open and close database connections more frequently.
     */

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        if (this != application) { // It does not concern us.
            return;
        }
        
        /**
         * GlycanBuilder uses the LogUtils class to log some exceptions that it doesn't want to pass up the call
         * stack.  I wasn't the original author of this package so I'm not 100% sure why this technique was adopted;
         * best guess is that it enables multiple threads to log to the same place and allows calling code to ignore
         * exceptions.
         * 
         * The new version of LogUtils that David R. Damerell created (me) allows for better control of were logging
         * information is actually stored.  This Vaadin application uses a technique that stores logger information
         * in a separate object for each Navigator7 Window.  This instance is created automatically for us, below
         * we just make sure that we remove it if it has been created.
         */
        IGGWebApplication.removeLogger();
        
        super.transactionEnd(application, transactionData);
    }
    
    @Override
    public void terminalError(Terminal.ErrorEvent event){
    	super.terminalError(event);
    
    	MessageDialogBox box=new MessageDialogBox("Unexpected exception", "An unexpected exception has just been detected", NavigableApplication.getCurrentNavigableAppLevelWindow());
    	box.setVisible(true);
    	box.center();
    }
    
    
    public static void reportMessage(String message,Exception ex){
    	IGGApplication.reportMessage(message);
    	
    	IGGApplication.reportException(ex);
    }
    
    public static void reportException(Exception ex){
    	ex.printStackTrace();    
    }
    
    public static void reportMessage(String message){
    	MessageDialogBox box=new MessageDialogBox("Notification", message, IGGApplication.getCurrentNavigableAppLevelWindow());
    	box.setVisible(true);
    	box.center();
    }
}