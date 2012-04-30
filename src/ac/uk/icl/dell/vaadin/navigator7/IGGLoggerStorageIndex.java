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

import java.util.HashMap;

import org.eurocarbdb.application.glycanbuilder.LoggerStorage;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageImpl;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageIndex;
import org.vaadin.navigator7.NavigableApplication;
import org.vaadin.navigator7.window.NavigableAppLevelWindow;

final class IGGLoggerStorageIndex implements LoggerStorageIndex{
	HashMap<NavigableAppLevelWindow,LoggerStorage> windowSpecificLogger=new HashMap<NavigableAppLevelWindow,LoggerStorage>();

	@Override
	public synchronized LoggerStorage getLogger(){
		
		if(windowSpecificLogger.get(NavigableApplication.getCurrentNavigableAppLevelWindow())==null){
			addLogger();
		}
		
		return windowSpecificLogger.get(NavigableApplication.getCurrentNavigableAppLevelWindow());
	}

	public synchronized void addLogger(){
		windowSpecificLogger.put(NavigableApplication.getCurrentNavigableAppLevelWindow(), new LoggerStorageImpl());
	}

	public synchronized void removeLogger(){
		windowSpecificLogger.remove(NavigableApplication.getCurrentNavigableAppLevelWindow());
	}

	public int getNumberOfLoggers(){
		return windowSpecificLogger.size();
	}
}