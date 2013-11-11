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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.GlycanParserFactory;
import org.eurocarbdb.application.glycanbuilder.LogUtils;
import org.eurocarbdb.application.glycanbuilder.Residue;


import ac.uk.icl.dell.vaadin.LocalResourceWatcher;
import ac.uk.icl.dell.vaadin.SimpleFileMenu;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.BasicCanvas.ExportListener;
import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanCanvas.GlycanCanvasUpdateListener;
import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanCanvas.NotationChangedListener;
import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanCanvas.ResidueHistoryListener;
import ac.uk.icl.dell.vaadin.menu.ApplicationMenu;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.ResizeEvent;

/**
 * callBack=[]
 * callBack.run=function(response){alert(response);}
 * window.glycanCanvasExport("glycoct_condensed",callBack)
 * 
 * 
 * @author david
 *
 */
public class GlycanBuilder extends CustomComponent implements com.vaadin.ui.Window.ResizeListener, ResidueHistoryListener{
	private static final long serialVersionUID=-1310424874910700087L;

	protected static SimpleFileMenu menu;
	
	protected VerticalLayout mainLayout;

	protected VaadinGlycanCanvas theCanvas;
	protected VaadinGlycanCanvas theResidueCanvas;
	
	//public org.eurocarbdb.application.glycanbuilder.BuilderWorkspace theWorkspace;
	
	protected Panel theToolBarPanel;
	protected List<MenuItem> menuItems=new ArrayList<MenuItem>();
	
	
	protected ApplicationMenu theAppMenu;

	private MenuItem structureItem;

	private MenuItem fileMenu;
	
	public GlycanBuilder(){
		this(menu=new SimpleFileMenu());
		
		//menu.addStyleName("icl-grey-menu");
	}
	
	public GlycanBuilder(ApplicationMenu appMenu){
		mainLayout=new VerticalLayout();
		mainLayout.setSizeFull();
		
		theAppMenu=appMenu;
		theAppMenu.setModified();
		
		theCanvas=new VaadinGlycanCanvas();
		theCanvas.setSizeFull();
		
		theCanvas.enableMouseSelectionRectangle(true);
		theCanvas.theCanvas.addResidueHistoryListener(this);	
		
		mainLayout.addComponent(menu);
		
		
		initToolBars();
		Panel panel = new Panel();
		panel.setSizeFull();
		panel.setContent(theCanvas);
		mainLayout.addComponent(panel);
		mainLayout.setExpandRatio(panel, 1);
		
		theCanvas.theCanvas.addNotationListener(new NotationChangedListener() {
			@Override
			public void notationChanged(String notation) {
				theCanvas.menuItemsWithResidueSelectionDependency.removeAll(structureItem.getChildren());
				
				theResidueCanvas.theCanvas.setNotation(notation);
				
				theAppMenu.getMenuBar().removeItem(structureItem);
				menuItems.remove(structureItem);
				
				initStructureMenu();
	
				theResidueCanvas.theCanvas.fireUpdatedSelection();
			}
		});
		
		theCanvas.setBackgroundColor("#CCF");
		
		theCanvas.addExportListener(new ExportListener(){
			@Override
			public void exportRequest(String fullCommand) {
				try{
					if(fullCommand.contains("~")){
						String cols[]=fullCommand.split("~");						
						String command=cols[0];
						if(command.equals("export")){
							if(cols.length!=2){
								//TODO: refactor logging to be vaadin 7 compliant 
								//theCanvas.respondToExportRequest("Export command without type");
							}else{
								String type=cols[1];

								String sequence=null;

								if(type.equals("glycoct_condensed")){
									sequence=theCanvas.theCanvas.theDoc.toGlycoCTCondensed();
								}else if(type.equals("glycoct")){
									sequence=theCanvas.theCanvas.theDoc.toGlycoCT();
								}else{
									sequence=theCanvas.theCanvas.theDoc.toString(GlycanParserFactory.getParser(type));
								}

								if(sequence!=null){
									//theCanvas.respondToExportRequest(sequence);
								}else{
									//theCanvas.respondToExportRequest("sequence is null");
								}
							}
						}else if(command.equals("import")){
							if(cols.length!=3){
								//theCanvas.respondToExportRequest("Import command without format and or value");
							}else{
								String type=cols[1];
								
								String value=cols[2].replaceAll("<br/>","\n"); //for debugger
								
								if(!theCanvas.theCanvas.theDoc.importFromString(value,type)){
									//theCanvas.respondToExportRequest("ERROR:"+LogUtils.getLastError());
									
									LogUtils.clearLastError();
								}
							}
						}else{
							//theCanvas.respondToExportRequest("ERROR:Unable to extract command");
						}
					}else{
						//theCanvas.respondToExportRequest("ERROR:Unable to extract command");
					}
				}catch(Exception ex){
					///TODO-PP theCanvas.respondToExportRequest("ERROR:An exception as occurred");
				}
			}
		});
		
		initFileMenu();
		initViewMenu();
		initStructureMenu();
		setCompositionRoot(mainLayout);
		
		//TODO:create minimumSize method.
		//TODO-PP theCanvas.setMinimumSize(1, 1);
		
		theResidueCanvas.setSizeFull();
		// TODO-PPtheResidueCanvas.setMinimumSize(1, 25);
	}
	
	public static void removeMenuItems(com.vaadin.ui.MenuBar.MenuItem structureItem2){
		if(structureItem2!=null){
			for(com.vaadin.ui.MenuBar.MenuItem child:structureItem2.getChildren()){
				structureItem2.removeChild(child);
				removeMenuItems(child);
			}
		}
		
	}
	
	public GlycanCanvas getGlycanCanvas(){
		return theCanvas.theCanvas;
	}
	
	public void registerLocalResourceWatcher(LocalResourceWatcher watcher){
		theCanvas.setLocalResourceWatcher(watcher);
	}
	
	private void initToolBars(){
		theToolBarPanel=new Panel();
		
		
			UI.getCurrent().addActionHandler(new Handler(){
			private static final long serialVersionUID=1735392108529734256L;
			
			Action actionDelete = new ShortcutAction("Delete",ShortcutAction.KeyCode.DELETE, null);
			Action actionCopy = new ShortcutAction("Copy",ShortcutAction.KeyCode.C, new int[]{ShortcutAction.ModifierKey.CTRL});
			Action actionPaste = new ShortcutAction("Paste",ShortcutAction.KeyCode.V, new int[]{ShortcutAction.ModifierKey.CTRL});
			Action actionCut = new ShortcutAction("Cut",ShortcutAction.KeyCode.X, new int[]{ShortcutAction.ModifierKey.CTRL});
			Action actionSelectAll = new ShortcutAction("Select all",ShortcutAction.KeyCode.A, new int[]{ShortcutAction.ModifierKey.CTRL});
			Action actionUnSelectAll = new ShortcutAction("UnSelect all",ShortcutAction.KeyCode.A, new int[]{ShortcutAction.ModifierKey.CTRL,ShortcutAction.ModifierKey.SHIFT});
			
			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[]{actionDelete,actionCopy,actionPaste,actionCut,actionSelectAll,actionUnSelectAll};
			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				
				GlycanCanvas canvas=theCanvas.theCanvas;
				if(theCanvas.theCanvas.hasSelected()){
					if(action==actionDelete){
						canvas.delete();
					}else if(action==actionCopy){
						canvas.copy();
					}else if(action==actionCut){
						canvas.cut();
					}
				}
				
				if(action==actionSelectAll){
					canvas.selectAll();
					canvas.documentUpdated();
				}else if(action==actionUnSelectAll){
					canvas.resetSelection();
					canvas.documentUpdated();
				}else if(action==actionPaste){
					System.err.println("Paste picked up!");
					canvas.paste();
				}
			}
		});
		
		theCanvas.appendGeneralToolBar(theToolBarPanel);
		
		//theToolBarPanel.setScrollable(true);
		
		mainLayout.addComponent(theToolBarPanel);
		
		Panel theLinkageToolBarPanel = new Panel();
		//theLinkageToolBarPanel.setScrollable(true);
		
		theCanvas.appendLinkageToolBar(theLinkageToolBarPanel);
		
		//theLinkageToolBarPanel.setScrollable(false);
		
		mainLayout.addComponent(theLinkageToolBarPanel);
		
		theResidueCanvas=new VaadinGlycanCanvas();
		theResidueCanvas.setBackgroundColor("#CCF");
		
		//theResidueCanvas.setHeight("25px");
		//theResidueCanvas.setWidth("100%");
		
		theResidueCanvas.enableMouseSelectionRectangle(false);
		theResidueCanvas.theCanvas.getWorkspace().getGlycanRenderer().getGraphicOptions().MARGIN_TOP=2;
		
		VerticalLayout layout=new VerticalLayout();
		layout.setHeight("30px");
		layout.setWidth("100%");
	
		layout.addComponent(theResidueCanvas);
		
		mainLayout.addComponent(layout);
		
		final VaadinGlycanCanvas finalCanvas=theResidueCanvas;
		finalCanvas.enableResidueToolBarMode();
		
		theResidueCanvas.theCanvas.addGlycanCanvasUpdateListener(new GlycanCanvasUpdateListener(){
			@Override
			public void glycanCanvasUpdated() {
				Residue selectedResidues[]=theResidueCanvas.theCanvas.selectedResidues.toArray(new Residue[0]);

				theResidueCanvas.theCanvas.selectedResidues.clear();
				
				theCanvas.theCanvas.setDocumentChangedEventFiring(false);
				
				
				for(Residue toinsert:selectedResidues){
					System.err.println("Selected residue: "+toinsert.getTypeName());
					theCanvas.theCanvas.addResidueByNameToSelected(toinsert.getTypeName());
				}
				
				theCanvas.theCanvas.setDocumentChangedEventFiring(true);
				theCanvas.theCanvas.documentUpdated();
			}
		});
	}
	
	private void initFileMenu(){
		fileMenu=theAppMenu.getFileMenu();
		
		theAppMenu.saveState(fileMenu, this);
		
		theCanvas.appendExportMenu(fileMenu,theAppMenu.getRestartFileMenuItem());
		theCanvas.appendImportMenu(fileMenu,theAppMenu.getRestartFileMenuItem());
		
		fileMenu.setVisible(true);
	}
	
	public MenuItem getFileMenu(){
		return fileMenu;
	}
	
	public MenuBar getMenuBar(){
		return theAppMenu.getMenuBar();
	}
	
	private void initStructureMenu(){
		MenuBar dockMenuBar=theAppMenu.getMenuBar();
		
		//structureItem=dockMenuBar.addItemAfter("Structure", null, null, theAppMenu.getViewMenu());
		structureItem=dockMenuBar.addItem("Structure", null);
		
		theCanvas.appendStructureMenu(structureItem);
		menuItems.add(structureItem);
	}
	
	private void initViewMenu(){
		MenuItem structureItem=theAppMenu.getViewMenu();
		
		theAppMenu.getViewMenu().setVisible(true);
		
		theCanvas.appendNotationMenu(structureItem);
		menuItems.add(structureItem);
	}

	@Override
	public void windowResized(ResizeEvent e) {	
		
		//getWindow().executeJavaScript("$('.gwt-PopupPanel canvas')[0].style.height=$('.v-panel-content-glycan_canvas_1')[0].style.height;");
		//getWindow().executeJavaScript("$('.gwt-PopupPanel canvas')[0].style.width=$('.v-panel-content-glycan_canvas_1')[0].style.width;");
	}

	@Override
	public void respondToResidueHistoryChanged(Vector<String> residueHistoryList) {
		theResidueCanvas.setResidueHistoryList(residueHistoryList);
		theResidueCanvas.appendResidueToolBar();
	}
	
	public VaadinGlycanCanvas getVaadinGlycanCanvas(){
		return theCanvas;
	}
	
	public void onUndock(){
		theAppMenu.restoreState(theAppMenu.getFileMenu(), this);
		theAppMenu.getFileMenu().setVisible(false);
		
		for(MenuItem item:menuItems){
			theAppMenu.removeMenuItem(item);
		}
	}
	
	public Component getComponent(){
		return mainLayout;
	}
	
	public void setWorkspace(BuilderWorkspace workspace){
		theCanvas.setWorkspace(workspace);
	}
	
	public void setNotation(String notation){
		theCanvas.setNotation(notation);
	}
	
	public Panel getGeneralToolBar(){
		return theToolBarPanel;
	}
}
