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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eurocarbdb.application.glycanbuilder.AbstractGlycanRenderer;
import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.CoreDictionary;
import org.eurocarbdb.application.glycanbuilder.CoreType;
import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.GlycanDocument;
import org.eurocarbdb.application.glycanbuilder.GlycanRendererAWT;
import org.eurocarbdb.application.glycanbuilder.GlycanRendererMode;
import org.eurocarbdb.application.glycanbuilder.GraphicOptions;
import org.eurocarbdb.application.glycanbuilder.Linkage;
import org.eurocarbdb.application.glycanbuilder.LogUtils;
import org.eurocarbdb.application.glycanbuilder.MassOptions;
import org.eurocarbdb.application.glycanbuilder.RepetitionPropertiesDialog;
import org.eurocarbdb.application.glycanbuilder.Residue;
import org.eurocarbdb.application.glycanbuilder.ResidueDictionary;
import org.eurocarbdb.application.glycanbuilder.ResiduePropertiesDialog;
import org.eurocarbdb.application.glycanbuilder.ResidueType;
import org.eurocarbdb.application.glycanbuilder.SVGUtils;
import org.eurocarbdb.application.glycanbuilder.TerminalDictionary;
import org.eurocarbdb.application.glycanbuilder.TerminalType;

import ac.uk.icl.dell.vaadin.LocalResourceWatcher;
import ac.uk.icl.dell.vaadin.MessageDialogBox;
import ac.uk.icl.dell.vaadin.ProducesLocalResources;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.BasicCanvas;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.Font;
import ac.uk.icl.dell.vaadin.canvas.basiccanvas.font.Font.FONT;
import ac.uk.icl.dell.vaadin.glycanbuilder.MassOptionsDialog.MassOptionListener;
import ac.uk.icl.dell.vaadin.navigator7.pages.buildingblocks.NavigatorFileUpload;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VaadinGlycanCanvas extends BasicCanvas implements BasicCanvas.SelectionListener, GlycanCanvas.GlycanCanvasUpdateListener, GlycanCanvas.SelectionChangeListener, ProducesLocalResources, MassOptionListener{
	private static final long serialVersionUID=-4055030966241059255L;
	
	private List<Component> componentsWithResidueSelectionDependency;
	public List<MenuItem> menuItemsWithResidueSelectionDependency;
	
	private List<LocalResourceWatcher> localResourceWatchers=new ArrayList<LocalResourceWatcher>();
	
	protected GlycanCanvas theCanvas;
	
	protected boolean automaticallyAdjustHeight=true;

	private OptionGroup field_anomeric_state;
	private OptionGroup field_anomeric_carbon;
	private OptionGroup field_linkage_position;
	private OptionGroup field_chirality;
	private OptionGroup field_ring_size;
	private CheckBox field_second_bond;
	private OptionGroup field_second_child_position;
	private OptionGroup field_second_parent_position;
	private PopupView linkage_one_panel;
	private PopupView linkage_two_panel;
	
	float realWidth;
	float realHeight;
	
	public VaadinGlycanCanvas() {
		theCanvas=new GlycanCanvas(new GlycanRendererCanvas(),new CanvasPaintable(this));
		theCanvas.addGlycanCanvasUpdateListener(this);
		
		
		this.addListener((CanvasMouseUpListener)this);
		this.addListener((CanvasMouseDownListener)this);
		
		this.setFont(Font.getFont(FONT.STANDARD));
		
		componentsWithResidueSelectionDependency=new ArrayList<Component>();
		menuItemsWithResidueSelectionDependency=new ArrayList<MenuItem>();
		
		addSelectionListener(this);
		
		theCanvas.addSelectionChangeListener(this);
		theCanvas.theWorkspace.getGraphicOptions().SHOW_REDEND_CANVAS=false;
		theCanvas.theWorkspace.getGraphicOptions().SHOW_MASSES_CANVAS=false;
		
		setImmediate(true);
	}
	
	public void setDocument(GlycanDocument doc){
		theCanvas.setDocument(doc);
		
		updateActions();
	}

	public void appendStructureMenu(MenuItem parent){
		MenuItem structureMenu=parent.addItem("Add structure", null);

		for(String superclass:CoreDictionary.getSuperclasses()){
			MenuItem superClassMenu=structureMenu.addItem(superclass,null);
			for(final CoreType t:CoreDictionary.getCores(superclass)){
				superClassMenu.addItem(t.getName(), new Command(){
					private static final long serialVersionUID=-148395291969656325L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						theCanvas.addSequenceByName(t.getName());
					}
				});
			}
		}
		
		createAddResidueMenu(parent);
		createInsertResidueMenu(parent);
		createChangeResidueMenu(parent);
		createTerminalMenu(parent);
	}
	
	private void createTerminalMenu(MenuItem parent){
		MenuItem structureMenu=parent.addItem("Add terminal", null);
		for(String superclass : TerminalDictionary.getSuperclasses()){
			MenuItem superClassMenu=structureMenu.addItem(superclass,null);
			for (TerminalType t : TerminalDictionary.getTerminals(superclass)) {
				MenuItem terminalType=superClassMenu.addItem(t.getDescription(),null);
				
				final String type=t.getName();
				Command addTerminal=new Command(){
					private static final long serialVersionUID=6872577027235164629L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						String longForm=selectedItem.getText();
						
						if(longForm.equals("Unknown linkage")){
							theCanvas.addTerminal(type);
						}else{
							theCanvas.addTerminal(longForm.charAt(0)+"-"+type);
						}
					}
				};
				
				terminalType.addItem("Unknown linkage", addTerminal);
				for (int l = 1; l < 9; l++) {
					terminalType.addItem(l+"-linked", addTerminal);
				}
			}
		}
	}
	
	public void appendImportMenu(MenuItem parent, MenuItem beforeItem){
		final Window importWindow=new Window(){
			private static final long serialVersionUID=-397373017493034496L;

			@Override
			public void close(){
				setVisible(false);
			}
		};
		importWindow.setCaption("Import from sequence");
		importWindow.setResizable(false);

		
		final CanvasImport canvasImport=new CanvasImport();
		VerticalLayout canvasImportLayout = new VerticalLayout();
		canvasImportLayout.setMargin(true);
		canvasImportLayout.addComponent(canvasImport);
		importWindow.setContent(canvasImportLayout);
		importWindow.center();
		importWindow.setVisible(false);
		
		@SuppressWarnings("unused")
		MenuItem importMenu=parent.addItemBefore("Import",null, new Command(){
			private static final long serialVersionUID=-6735134306275926140L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(!importWindow.isVisible()){
					if(UI.getCurrent().getWindows().contains(importWindow)==false){
						UI.getCurrent().addWindow(importWindow);
					}
					
					importWindow.setVisible(true);
				}
			}
		},beforeItem);
		
		importWindow.setSizeUndefined();
		importWindow.getContent().setSizeUndefined();
		
		final Window importFromStringWindow=new Window(){
			private static final long serialVersionUID=7035248961169308096L;

			@Override
			public void close(){
				setVisible(false);
			}
		};
		
		importFromStringWindow.setCaption("Import sequence from string");
		importFromStringWindow.setWidth("50%");
		importFromStringWindow.setHeight("50%");
		final ImportStructureFromStringDialog importStructureStringDialog=new ImportStructureFromStringDialog(theCanvas);
		
		importStructureStringDialog.addListener(new UserInputEndedListener() {
			@Override
			public void done(boolean cancelled) {
				if(!cancelled){
					if(!theCanvas.theDoc.importFromString(importStructureStringDialog.getSequenceString(),theCanvas.getImportFormatShortFormat(importStructureStringDialog.getSequenceFormat()))){
						reportMessage(LogUtils.getLastError());				    							
						LogUtils.clearLastError();
					}
				}
			}
		});
		
		VerticalLayout layout=new VerticalLayout();
		layout.setSizeFull();
		importFromStringWindow.setContent(layout);
		layout.addComponent(importStructureStringDialog);
		layout.setComponentAlignment(importStructureStringDialog, Alignment.MIDDLE_CENTER);

		importFromStringWindow.center();
		importFromStringWindow.setVisible(false);
		
		@SuppressWarnings("unused")
		MenuItem importFromStringMenu=parent.addItemBefore("Import from string",null, new Command(){
			private static final long serialVersionUID=1586089744665899803L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(!importFromStringWindow.isVisible()){
					if(UI.getCurrent().getWindows().contains(importFromStringWindow)==false){
						UI.getCurrent().addWindow(importFromStringWindow);
					}
					
					importFromStringWindow.setVisible(true);
				}
			}
		},beforeItem);
	}
	
	public class CanvasImport extends Panel {
		private static final long serialVersionUID=8487960720141044728L;
		
		NavigatorFileUpload uploader;
		ListSelect importTypes;
		
		public CanvasImport(){
			
			VerticalLayout canvasImportLayout = new VerticalLayout();
			canvasImportLayout.setMargin(true);
			setContent(canvasImportLayout);
			uploader=new NavigatorFileUpload("Upload"){
				@Override
				public void uploadFailed(FailedEvent failedEvent){
					reportMessage("Upload failed");
				}

				@Override
				public void uploadSucceeded(SucceededEvent succeededEvent){
					File file=getUploadedFile();
					
					String format=theCanvas.getImportFormatShortFormat((String)importTypes.getValue());
					try{
						theCanvas.theDoc.importFrom(file.getPath(), format);
					}catch(Exception ex){
						reportMessage("Wrong format or invalid content");
					}
					
					if(LogUtils.hasLastError()){
						reportMessage("Wrong format or invalid content");
					}
					
					LogUtils.clearLastError();
					
					if(file.delete()==false){
						reportException(new Exception("Unable to delete file: "+file.getPath()));
					}
					
					removeLocalResource(file);
				}

				@Override
				public void uploadFailed(Message msg){
					reportMessage(msg.getMessage());
				}
			};
			
			uploader.setLocalResourceWatchers(localResourceWatchers);
			
			List<String> importTypeList=theCanvas.getImportFormats();
			
			importTypes=new ListSelect("Format", importTypeList);
			importTypes.setNullSelectionAllowed(false);
			importTypes.setValue(importTypeList.get(0));
			
			canvasImportLayout.addComponent(importTypes);
			canvasImportLayout.addComponent(uploader.getUploadComponent());
		}
	}
	
	public void showMessage(String message,String width, String height){
		showMessage(message,width,height,"");
	}
	
	public void showMessage(String message,String width, String height,String caption){
    	Label messageLabel=new Label(message);	    		
    	Window window = new Window(caption);
		
		VerticalLayout windowContent = new VerticalLayout();
		windowContent.addComponent(messageLabel);
		windowContent.setMargin(true);
		windowContent.setComponentAlignment(messageLabel, Alignment.MIDDLE_CENTER);
		window.setContent(windowContent);
		window.setWidth(width);
		window.setHeight(height);
		
		window.center();
		UI.getCurrent().addWindow(window);
	}
	
	public void addLocalResource(File file){
		for(LocalResourceWatcher watcher:localResourceWatchers){
			watcher.addLocalResource(file);
		}
	}
	
	public void removeLocalResource(File file){
		for(LocalResourceWatcher watcher:localResourceWatchers){
			watcher.removeLocalResource(file);
		}
	}
	
	public void appendExportMenu(MenuItem parent, MenuItem beforeItem){
		Command command=new Command(){
			/**
			 * 
			 */
			private static final long serialVersionUID=-3300979660830126504L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(theCanvas.theDoc.getStructures().size()==0){
					return;
				}
				
				String format=theCanvas.getInternalFormat(selectedItem.getText());
				try {
					File file = File.createTempFile("seq", "."+format);
					
					addLocalResource(file);
					
					theCanvas.theDoc.exportTo(file.getPath(), format);
					
					String mimeType="text/plain";
					
					if(format.equals("glycoct_xml") || format.equals("glyde")){
						mimeType="text/xml";
					}
					
					final String finalMimeType=mimeType;
					 
					Page.getCurrent().open(new FileResource(file){
						private static final long serialVersionUID=6816460214678812683L;

						@Override
						public DownloadStream getStream(){
							try{
								final DownloadStream ds=new DownloadStream(new FileInputStream(getSourceFile()),finalMimeType,getFilename());
								ds.setParameter("Content-Disposition", "attachment; filename=\"" +getFilename() + "\"");
								ds.setCacheTime(getCacheTime());
								return ds;
							}catch(Exception ex){
								reportMessage("An error has occured attempting to export the glycan canvas", ex);
							}
							
							return null;
						}
					}, "_blank", true);
					
				} catch (IOException e) {
					reportMessage("An error has occured attempting to export the glycan canvas", e);
				}				
			}
		};
		
		MenuItem exportMenu=parent.addItemBefore("Export", null, null, beforeItem);
		
		for(String format:theCanvas.getSequenceExportFormats()){
			exportMenu.addItem(format, command);
		}
		
		Command onImageExport=new Command(){
			private static final long serialVersionUID=-3304865968113708422L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(theCanvas.theDoc.getStructures().size()==0){
					return;
				}
				
				String format=theCanvas.getImageExportShortFormat(selectedItem.getText());
				try {
					File file = File.createTempFile("seq", "."+format);
					
					for(LocalResourceWatcher watcher:localResourceWatchers){
						watcher.addLocalResource(file);
					}
					
					GlycanRendererAWT renderer=new GlycanRendererAWT((AbstractGlycanRenderer)theCanvas.getWorkspace().getGlycanRenderer());
					
					SVGUtils.export(renderer,file.getPath(),theCanvas.theDoc.getStructures(),theCanvas.theWorkspace.getGraphicOptions().SHOW_MASSES_CANVAS,theCanvas.theWorkspace.getGraphicOptions().SHOW_REDEND_CANVAS,format);
											
					String mimeType="image/"+format;
					
					if(format.equals("pdf")){
						mimeType="application/pdf";
					}else if(format.equals("ps")){
						mimeType="application/postscript";
					}else if(format.equals("svg")){
						mimeType=mimeType.concat("+xml");
					}
					
					final String finalMimeType=mimeType;
					
					Page.getCurrent().open(new FileResource(file){
						private static final long serialVersionUID=7843576185418877104L;

						@Override
						public DownloadStream getStream(){
							try{
								final DownloadStream ds=new DownloadStream(new FileInputStream(getSourceFile()),finalMimeType,getFilename());
								ds.setParameter("Content-Disposition", "attachment; filename=\"" +getFilename() + "\"");
								ds.setCacheTime(getCacheTime());
								return ds;
							}catch(Exception ex){
								reportMessage("An error has occured attempting to export the glycan canvas", ex);
							}
							
							return null;
						}
					}, "_blank", true);
					
				} catch (IOException e) {
					reportMessage("An error has occured attempting to export the glycan canvas", e);
				}
			}
		};
		
		MenuItem exportToImageMenu=parent.addItemBefore("Image export", null,null,beforeItem);
		for(String format:theCanvas.getImageExportFormats()){
			exportToImageMenu.addItem(format, onImageExport);
		}
	}
	
	public void createAddResidueMenu(MenuItem parent) {
		String notation=theCanvas.getWorkspace().getGlycanRenderer().getGraphicOptions().NOTATION;
		
		MenuItem structureMenu=parent.addItem("Add residue", null);

		for (String superclass : ResidueDictionary.getSuperclasses()) {
			MenuItem superClassMenu=structureMenu.addItem(superclass,null);
			for (ResidueType t : ResidueDictionary.getResidues(superclass)) {
				if (t.canHaveParent()){
					superClassMenu.addItem(t.getName(), new Command(){
						private static final long serialVersionUID=4750928193466060500L;

						@Override
						public void menuSelected(MenuItem selectedItem) {
							theCanvas.addResidueByNameToSelected(selectedItem.getText());
						}		
					}).setIcon(new ThemeResource("icons"+File.separator+"residues"+File.separator+notation+File.separator+t.getName()+".png"));
				}		
			}
			
			if(superClassMenu.getChildren()==null){
				structureMenu.removeChild(superClassMenu);
			}
		}
	}
	
	public void createInsertResidueMenu(MenuItem parent){
		MenuItem structureMenu=parent.addItem("Insert residue", null);
		
		String notation=theCanvas.getWorkspace().getGlycanRenderer().getGraphicOptions().NOTATION;
		
		for (String superclass : ResidueDictionary.getSuperclasses()) {
			MenuItem superClassMenu=structureMenu.addItem(superclass,null);
			for (ResidueType t : ResidueDictionary.getResidues(superclass)) {
				if (t.canHaveParent() && t.canHaveChildren()
						&& t.getMaxLinkages() >= 2){
					superClassMenu.addItem(t.getName(), new Command(){
						private static final long serialVersionUID=2685831199169131556L;

						@Override
						public void menuSelected(MenuItem selectedItem) {
							theCanvas.insertResidueByNameBeforeSelected(selectedItem.getText());
						}
					}).setIcon(new ThemeResource("icons"+File.separator+"residues"+File.separator+notation+File.separator+t.getName()+".png"));
					
				}	
			}
			if(superClassMenu.getChildren()==null){
				structureMenu.removeChild(superClassMenu);
			}
		}
		structureMenu.setEnabled(false);
		menuItemsWithResidueSelectionDependency.add(structureMenu);
	}
	
	private void createChangeResidueMenu(MenuItem parent) {
		MenuItem structureMenu=parent.addItem("Change residue", null);

		String notation=theCanvas.getWorkspace().getGlycanRenderer().getGraphicOptions().NOTATION;
		
		for (String superclass : ResidueDictionary.getSuperclasses()) {
			MenuItem superClassMenu=structureMenu.addItem(superclass,null);
			for (ResidueType t : ResidueDictionary.getResidues(superclass)){
				superClassMenu.addItem(t.getName(), new Command(){
					private static final long serialVersionUID=-7886271503255704127L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						theCanvas.changeSelectedToResidueByName(selectedItem.getText());
					}
				}).setIcon(new ThemeResource("icons"+File.separator+"residues"+File.separator+notation+File.separator+t.getName()+".png"));
			}
			if(superClassMenu.getChildren()==null){
				structureMenu.removeChild(superClassMenu);
			}
		}
		
		structureMenu.setEnabled(false);
		menuItemsWithResidueSelectionDependency.add(structureMenu);
	}
	
	public void appendNotationMenu(MenuItem parent) {
		final HashMap<String,String> notationIndex=new HashMap<String,String>();
		
		Command notationChangeCommand=new Command(){
			private static final long serialVersionUID=5081687058270283137L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				String notation=notationIndex.get(selectedItem.getText());
				theCanvas.setNotation(notation);
			}
		};

		parent.addItem("CFG notation", notationChangeCommand);
		notationIndex.put("CFG notation", GraphicOptions.NOTATION_CFG);
		
		parent.addItem("CFG black and white notation", notationChangeCommand);
		notationIndex.put("CFG black and white notation", GraphicOptions.NOTATION_CFGBW);
		
		parent.addItem("CFG with linkage placement notation", notationChangeCommand);
		notationIndex.put("CFG with linkage placement notation", GraphicOptions.NOTATION_CFGLINK);

		parent.addItem("UOXF notation", notationChangeCommand);
		notationIndex.put("UOXF notation", GraphicOptions.NOTATION_UOXF);
		
		parent.addItem("UOXFCOL notation", notationChangeCommand);
		notationIndex.put("UOXFCOL notation", GraphicOptions.NOTATION_UOXFCOL);

		parent.addItem("Text only notation", notationChangeCommand);
		notationIndex.put("Text only notation", GraphicOptions.NOTATION_TEXT);
		
		parent.addItem("Show Masses", new ThemeResource("icons/uncheckedbox.png"),new Command(){
			private static final long serialVersionUID=6140157670134115820L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				//selectedItem.setIcon(arg0);
				
				boolean showMasses=theCanvas.theWorkspace.getGraphicOptions().SHOW_MASSES_CANVAS;
				
				if(showMasses){
					theCanvas.theWorkspace.getGraphicOptions().SHOW_MASSES_CANVAS=false;
					selectedItem.setIcon(new ThemeResource("icons/uncheckedbox.png"));
				}else{
					theCanvas.theWorkspace.getGraphicOptions().SHOW_MASSES_CANVAS=true;
					selectedItem.setIcon(new ThemeResource("icons/checkbox.png"));
				}
				
				theCanvas.documentUpdated();
			}
		});
		
		parent.addItem("Show reducing end symbol",new ThemeResource("icons/uncheckedbox.png"), new Command(){
			private static final long serialVersionUID=-5209359926737326181L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				boolean showRedEnd=theCanvas.theWorkspace.getGraphicOptions().SHOW_REDEND_CANVAS;
				
				if(showRedEnd){
					theCanvas.theWorkspace.getGraphicOptions().SHOW_REDEND_CANVAS=false;
					selectedItem.setIcon(new ThemeResource("icons/uncheckedbox.png"));
				}else{
					theCanvas.theWorkspace.getGraphicOptions().SHOW_REDEND_CANVAS=true;
					selectedItem.setIcon(new ThemeResource("icons/checkbox.png"));
				}
				
				theCanvas.documentUpdated();
			}
		});
		
		
		final Window massOptionsDialog=new Window(){
			private static final long serialVersionUID=-5094399884130705221L;

			@Override
			public void close(){
				setVisible(false);
			}
		};
		
		massOptionsDialog.setResizable(false);
		//massOptionsDialog.setIcon(new ThemeResource("icons/massoptions.png"));
		
		massOptionsDialog.setCaption("Mass options");
		
		MassOptionsDialog dialog=new MassOptionsDialog(theCanvas.theDoc.getStructures(), theCanvas.theWorkspace.getDefaultMassOptions());
		
		dialog.addMassOptionListener(this);
		
		VerticalLayout massOptionsDialogLayout = new VerticalLayout(dialog);
		massOptionsDialogLayout.setMargin(true);
		massOptionsDialogLayout.setComponentAlignment(dialog, Alignment.MIDDLE_CENTER);
		
		massOptionsDialog.setContent(massOptionsDialogLayout);
				
		massOptionsDialog.setVisible(false);
		
		massOptionsDialog.center();
		
		parent.addItem("Mass options",new Command(){
			private static final long serialVersionUID=-589321392382766804L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(massOptionsDialog.getParent()==null){
					UI.getCurrent().addWindow(massOptionsDialog);
				}
				
				massOptionsDialog.setVisible(true);	
			}
		});
		
		massOptionsDialog.setSizeUndefined();
		massOptionsDialog.getContent().setSizeUndefined();
	}
	
	public void enableResidueToolBarMode(){
		theCanvas.getWorkspace().getGlycanRenderer().setRenderMode(GlycanRendererMode.TOOLBAR);
		appendResidueToolBar();
	}
	
	public void clearAll(){
		clear(); //clear remote canvas
		theCanvas.clear(); //clear residue/linkage selection state and the glycan document of all structures
	}
	
	public void appendResidueToolBar(){
		theCanvas.setDocumentChangedEventFiring(false);
		
		HashSet<String> selectedResiduesByName=new HashSet<String>();
		for(Residue res:theCanvas.selectedResidues){
			selectedResiduesByName.add(res.getTypeName());
		}
	
		clearAll();
		
		HashSet<String> drawn=new HashSet<String>();
		
		for(Iterator<ResidueType> i=ResidueDictionary.directResidues().iterator();i.hasNext();) {
			ResidueType t = i.next();
				
			if(t.canHaveParent()){
				theCanvas.addResidueByNameToSelected(t.getName());
				drawn.add(t.getName());
			}
		}
		
		int numberOfResiduesInHistory=theCanvas.residueTypeHistoryList.size();
		if(theCanvas.residueTypeHistoryList.size()>0){
			for(int i=0;i<numberOfResiduesInHistory;i++){
				String name=theCanvas.residueTypeHistoryList.get(i);
				if(!drawn.contains(name)){
					theCanvas.addResidueByNameToSelected(name);
				}
			}
		}
		
		theCanvas.selectAllResiduesByName(selectedResiduesByName);
		
		theCanvas.documentUpdated();
		
		theCanvas.setDocumentChangedEventFiring(true);
	}
	
	public void setResidueHistoryList(Vector<String> residueHistoryList){
		theCanvas.residueTypeHistoryList=residueHistoryList;
	}
	
	/**
	 * Set the properties of the residue with the focus using the values in the
	 * properties toolbar
	 * 
	 * @see #getToolBarProperties
	 */
	public void onSetProperties() {
		Residue current = theCanvas.getCurrentResidue();
		if (current != null) {
			current.setAnomericState(getSelectedValueChar(field_anomeric_state));
			current.setAnomericCarbon(getSelectedValueChar(field_anomeric_carbon));
			current.setChirality(getSelectedValueChar(field_chirality));
			current.setRingSize(getSelectedValueChar(field_ring_size));

			Linkage parent_linkage = current.getParentLinkage();
			if (parent_linkage != null) {
				char[] sel_linkage_positions = getSelectedPositions(field_linkage_position);
				if((Boolean)field_second_bond.getValue()){
					char[] sel_second_parent_positions = getSelectedPositions(field_second_parent_position);
					char sel_second_child_position = getSelectedValueChar(field_second_child_position);
					parent_linkage.setLinkagePositions(sel_linkage_positions,
							sel_second_parent_positions,
							sel_second_child_position);
				}else{
					parent_linkage.setLinkagePositions(sel_linkage_positions);
				}
			}

			theCanvas.documentUpdated();
		}
	}
	
	/**
	 * Change the properties of the residue with the focus. Display a
	 * {@link ResiduePropertiesDialog} or a {@link RepetitionPropertiesDialog}
	 * depending of the type of the residue.
	 */
	public void onProperties() {
		Residue current = theCanvas.getCurrentResidue();
		if (current == null)
			return;
		if (current.getParent() != null
				&& (!current.isSpecial() || current.isCleavage())) {
			theCanvas.setSelection(current);
			theCanvas.documentUpdated();
		} else if (current.isStartRepetition()) {
			theCanvas.setSelection(current);
			theCanvas.documentUpdated();
		} else if (current.isEndRepetition()) {
			theCanvas.setSelection(current);
			theCanvas.documentUpdated();
		}
	}
	
	private char[] getSelectedPositions(AbstractSelect field) {
		@SuppressWarnings("rawtypes")
		Set sel = (Set) field.getValue();
		
		if(sel.size() == 0){
			return new char[] { '?' };
		}else{
			char[] ret = new char[sel.size()];
			int i=0;
			for(Object obj:sel){
				ret[i] = ((String) obj).charAt(0);
				i++;
			}
			
			return ret;
		}
	}
	
	private char getSelectedValueChar(AbstractSelect field) {
		if (field.getValue() == null)
			return '?';
		return ((String) field.getValue()).charAt(0);
	}
	
	private void updateLinkageToolBar() {
		if(linkagePanel==null){
			return;
		}else{
			final Residue current = theCanvas.getCurrentResidue();
			
			if(field_anomeric_state!=null){
				field_anomeric_state.removeValueChangeListener(defaultListener);
				field_anomeric_state.removeValueChangeListener(defaultListener);
				field_anomeric_carbon.removeValueChangeListener(defaultListener);
				field_linkage_position.removeValueChangeListener(defaultListener);
				field_chirality.removeValueChangeListener(defaultListener);
				field_ring_size.removeValueChangeListener(defaultListener);
				field_second_child_position.removeValueChangeListener(defaultListener);
				field_second_parent_position.removeValueChangeListener(defaultListener);
			}
			
			//linkagePanel.removeAllComponents();
			popupLayout2 = new HorizontalLayout();
			linkage_two_panel = new PopupView("2nd Linkage", popupLayout2);
			
			popupLayout2.addStyleName("2nd_linkage_panel");
			linkage_two_panel.setEnabled(false);
			
			field_anomeric_state = new OptionGroup("Anomeric state",Arrays.asList(new String[] { "?","a", "b" }));
			field_anomeric_state.setStyleName("linkage_component_select");
			field_anomeric_state.setEnabled(false);
			
			field_anomeric_carbon = new OptionGroup("Anomeric carbon",Arrays.asList(new String[] { "?","1", "2", "3" }));
			field_anomeric_carbon.setEnabled(false);
					
			field_linkage_position = new OptionGroup("Linkage position",Arrays.asList(new String[] { "?", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
			field_linkage_position.setMultiSelect(true);
			field_linkage_position.setEnabled(false);
			
			field_chirality = new OptionGroup("Chirality",Arrays.asList(new String[] {  "?","D","L" }));
			field_chirality.setMultiSelect(false);
			field_chirality.setEnabled(false);
			
			field_ring_size = new OptionGroup("Ring",Arrays.asList(new String[] {  "?", "p","f", "o" }));
			field_ring_size.setMultiSelect(false);
			field_ring_size.setEnabled(false);
			
			field_second_bond=new CheckBox("");
			field_second_bond.setEnabled(false);
			field_second_bond.addStyleName("glycanbuilder-2nd-cov-checkbox");
			
			field_second_bond.setValue(false);
			field_second_bond.setImmediate(true); //forces a value change event to be fired immediately
			
			field_second_child_position = new OptionGroup("Linkage Position",Arrays.asList(new String[] {  "?", "1", "2", "3" }));
			field_second_child_position.setMultiSelect(false);
			field_second_child_position.setEnabled(false);
			
			field_second_parent_position = new OptionGroup("Linkage Position",Arrays.asList(new String[] {  "?", "1", "2", "3", "4", "5", "6", "7","8", "9" }));
			field_second_parent_position.setMultiSelect(true);
			field_second_parent_position.setEnabled(false);
		
			if(current != null && (!current.isSpecial() || current.isCleavage() || current.isStartRepetition())){
				//linkagePanel.setVisible(true);
				
				Linkage parent_link = current.getParentLinkage();
				
				if(parent_link != null){
					field_linkage_position.removeAllItems();
					List<String> positions=theCanvas.createPositions(parent_link.getParentResidue());
					for(String position:positions){
						field_linkage_position.addItem(position);
					}

					field_second_parent_position.removeAllItems();
					for(String position:positions){
						field_second_parent_position.addItem(position);
					}
				}

				boolean can_have_parent_linkage = (parent_link != null && parent_link.getParentResidue() != null && 
						(parent_link.getParentResidue().isSaccharide() || parent_link.getParentResidue().isBracket() || 
								parent_link.getParentResidue().isRepetition() || parent_link.getParentResidue().isRingFragment()));

				field_linkage_position.setEnabled(can_have_parent_linkage);
				field_anomeric_state.setEnabled(current.isSaccharide());
				field_anomeric_carbon.setEnabled(current.isSaccharide());
				field_chirality.setEnabled(current.isSaccharide());
				field_ring_size.setEnabled(current.isSaccharide());
				field_second_bond.setEnabled(can_have_parent_linkage);
				
				linkage_two_panel.setEnabled(can_have_parent_linkage && parent_link.hasMultipleBonds());
				
				field_second_parent_position.setEnabled(can_have_parent_linkage && parent_link.hasMultipleBonds());
				field_second_child_position.setEnabled(can_have_parent_linkage && parent_link.hasMultipleBonds());

				if (parent_link != null){
					field_linkage_position.setValue(Arrays.asList(theCanvas.toStrings(parent_link.glycosidicBond().getParentPositions())));
				}else{
					field_linkage_position.setValue(null);
				}

				field_anomeric_state.setValue(""
						+ current.getAnomericState());
				field_anomeric_carbon.setValue(""
						+ current.getAnomericCarbon());
				field_chirality.setValue("" + current.getChirality());
				field_ring_size.setValue("" + current.getRingSize());
				if (parent_link != null) {
					field_second_bond.setValue(parent_link.hasMultipleBonds());
					field_second_parent_position.setValue(Arrays.asList(theCanvas.toStrings(parent_link.getBonds()
							.get(0).getParentPositions())));
					field_second_child_position.setValue(""+ parent_link.getBonds().get(0).getChildPosition());
				}else{
					field_second_parent_position.setValue(null);
					field_second_child_position.setValue("?");
				}
						
				forceLinkagePopRepaint();
			}else{
				//linkagePanel.setVisible(false);
				
				field_linkage_position.setEnabled(false);
				field_anomeric_state.setEnabled(false);
				field_anomeric_carbon.setEnabled(false);
				field_chirality.setEnabled(false);
				field_ring_size.setEnabled(false);
				field_second_bond.setEnabled(false);
				field_second_parent_position.setEnabled(false);
				field_second_child_position.setEnabled(false);

				field_linkage_position.setValue(null);
				field_anomeric_state.setValue("?");
				field_anomeric_carbon.setValue("");
				field_chirality.setValue("?");
				field_ring_size.setValue("?");
				field_second_parent_position.setValue(null);
				field_second_child_position.setValue("?");
				
				forceLinkagePopRepaint();
			}
		}
	}
	
	public void registerLinkageListeners(){
		defaultListener = new Property.ValueChangeListener() {
			private static final long serialVersionUID=375264676842546361L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				onSetProperties();
				System.err.println("Value change listener");
			}
		};
		
		field_anomeric_state.addValueChangeListener(defaultListener);
		field_anomeric_carbon.addValueChangeListener(defaultListener);
		field_linkage_position.addValueChangeListener(defaultListener);
		field_chirality.addValueChangeListener(defaultListener);
		field_ring_size.addValueChangeListener(defaultListener);
		field_second_child_position.addValueChangeListener(defaultListener);
		field_second_parent_position.addValueChangeListener(defaultListener);
		
		field_second_bond.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID=1577488601988220206L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean checked=(Boolean) event.getProperty().getValue();
				
				onSetProperties();
				
				Residue current=theCanvas.getCurrentResidue();
				
				if(checked){
					Linkage parent_link=current.getParentLinkage();
					boolean can_have_parent_linkage = (parent_link != null && parent_link.getParentResidue() != null && 
							(parent_link.getParentResidue().isSaccharide() || parent_link.getParentResidue().isBracket() || 
									parent_link.getParentResidue().isRepetition() || parent_link.getParentResidue().isRingFragment()));
					
					field_second_parent_position.setEnabled(can_have_parent_linkage && parent_link.hasMultipleBonds());
					field_second_child_position.setEnabled(can_have_parent_linkage && parent_link.hasMultipleBonds());
					
					linkage_two_panel.setEnabled(true);
				}else{
					linkage_two_panel.setEnabled(false);
				}
			}
		});
	}
	
	
	public void forceLinkagePopRepaint(){
		registerLinkageListeners();
		
		HorizontalLayout toolBar=new HorizontalLayout();
		toolBar.setWidth("100%");
		
		HorizontalLayout popupLayout = new HorizontalLayout();
		linkage_one_panel = new PopupView("1st Linkage",popupLayout);
		if(field_linkage_position.isEnabled() || field_anomeric_carbon.isEnabled() || 
					field_anomeric_state.isEnabled() || field_chirality.isEnabled() || field_ring_size.isEnabled()){
			linkage_one_panel.setEnabled(true);
		}else{
			linkage_one_panel.setEnabled(false);
		}
		
		popupLayout.addStyleName("1st_linkage_panel");
		
		popupLayout.addComponent(field_anomeric_state);
		popupLayout.addComponent(field_anomeric_carbon);
		popupLayout.addComponent(field_linkage_position);
		popupLayout.addComponent(field_chirality);
		popupLayout.addComponent(field_ring_size);
		
		
//		boolean enabled=linkage_two_panel.isEnabled();
//		linkage_two_panel = new PopupButton("2nd Linkage");
//		linkage_two_panel.setEnabled(enabled);
//		linkage_two_panel.setComponent(popupLayout2);
		
//		linkage_one_panel.addStyleName(BaseTheme.BUTTON_LINK);
//		linkage_two_panel.addStyleName("link");
		
		linkage_two_panel.addStyleName("igg-glycan-builder-linkage-toolbar-panel-item");
		linkage_one_panel.addStyleName("igg-glycan-builder-linkage-toolbar-panel-item");
		field_second_bond.addStyleName("igg-glycan-builder-linkage-toolbar-panel-item");
		
		toolBar.addComponent(linkage_one_panel);
		
		popupLayout2.addComponent(field_second_child_position);
		popupLayout2.addComponent(field_second_parent_position);
		
		toolBar.addComponent(field_second_bond);
		
		toolBar.addComponent(linkage_two_panel);
		HorizontalLayout filler = new HorizontalLayout();
		toolBar.addComponent(filler);
		toolBar.setExpandRatio(filler, 1f);
		
		//linkagePanel.removeAllComponents();
		linkagePanel.setContent(toolBar);
	}
	
	Panel linkagePanel;

	private HorizontalLayout popupLayout2;

	private Property.ValueChangeListener defaultListener;
	
	public void appendLinkageToolBar(Panel theLinkageToolBarPanel){
		linkagePanel=theLinkageToolBarPanel;
		
		updateLinkageToolBar();	
	}
	
	public void appendGeneralToolBar(Panel theToolBarPanel){
		HorizontalLayout toolBar=new HorizontalLayout();	
		toolBar.setStyleName("toolbar");
		
		NativeButton deleteButton=new NativeButton("Delete");
		deleteButton.setIcon(new ThemeResource("icons/deleteNew.png"));
		deleteButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=1289257412952359727L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.delete();
			}
		});
		deleteButton.setEnabled(false);
		
		NativeButton copyButton=new NativeButton("Copy");
		final NativeButton pasteButton=new NativeButton("Paste");
		copyButton.setIcon(new ThemeResource("icons/editcopy.png"));
		copyButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=-1740735587078805580L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.copy();
				pasteButton.setEnabled(true);
			}
		});
		copyButton.setEnabled(false);
		
		pasteButton.setIcon(new ThemeResource("icons/editpaste.png"));
		pasteButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=-8732259244009686729L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.paste();
				pasteButton.setEnabled(false);
			}
		});
		pasteButton.setEnabled(false);
		
		final NativeButton bracketButton=new NativeButton("Bracket");
		bracketButton.setIcon(new ThemeResource("icons/bracket.png"));
		bracketButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=5201094306113759901L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.addBracket();
			}
		});
		bracketButton.setEnabled(false);
		
		final NativeButton repeatButton=new NativeButton("Repeat");
		repeatButton.setIcon(new ThemeResource("icons/repeat.png"));
		repeatButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=-23302591439643695L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					theCanvas.addRepeat();
				} catch (Exception ex) {
					showMessage(ex.getMessage(), "400px","100px","Error creating repeat");
				}
			}
		});
	    repeatButton.setEnabled(false);
		
	    componentsWithResidueSelectionDependency.add(repeatButton);
		componentsWithResidueSelectionDependency.add(bracketButton);
		componentsWithResidueSelectionDependency.add(deleteButton);
		componentsWithResidueSelectionDependency.add(copyButton);
		
		final NativeButton orientationButton=new NativeButton("Orientation");
		
		orientationButton.setIcon(new ThemeResource("icons/"+theCanvas.getOrientationIcon()));
		orientationButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=6621021858668446143L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.changeOrientation();
				orientationButton.setIcon(new ThemeResource("icons/"+theCanvas.getOrientationIcon()));
			}
		});
		
		final NativeButton selectAllButton=new NativeButton("Select all");
		
		selectAllButton.setIcon(new ThemeResource("icons/selectall.png"));
		selectAllButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=-5848923636575805154L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.selectAll();
				theCanvas.documentUpdated();
			}
		});
		
		final NativeButton deSelectAllButton=new NativeButton("Select none");
		
		deSelectAllButton.setIcon(new ThemeResource("icons/deselect.png"));
		deSelectAllButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID=8339468775345706027L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.resetSelection();
				theCanvas.documentUpdated();
			}
		});
		
		final NativeButton moveCWButton=new NativeButton("Move CW");
		
		moveCWButton.setIcon(new ThemeResource("icons/rotatecw.png"));
		moveCWButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID = -6061975045440741204L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.onMoveCW();
			}
		});
		
		componentsWithResidueSelectionDependency.add(moveCWButton);
		
		final NativeButton moveCCWButton=new NativeButton("Move CCW");
		
		moveCCWButton.setIcon(new ThemeResource("icons/rotateccw.png"));
		moveCCWButton.addClickListener(new ClickListener(){
			private static final long serialVersionUID = 3555726070782377309L;

			@Override
			public void buttonClick(ClickEvent event) {
				theCanvas.onMoveCCW();
			}
		});
		
		componentsWithResidueSelectionDependency.add(moveCCWButton);
		
		
		toolBar.addComponent(deleteButton);
		toolBar.addComponent(copyButton);
		toolBar.addComponent(pasteButton);
		toolBar.addComponent(orientationButton);
		toolBar.addComponent(selectAllButton);
		toolBar.addComponent(deSelectAllButton);
		toolBar.addComponent(bracketButton);
		toolBar.addComponent(repeatButton);
		toolBar.addComponent(moveCWButton);
		toolBar.addComponent(moveCCWButton);
		
		HorizontalLayout layout=new HorizontalLayout();
		layout.setSizeFull();
		
		toolBar.addComponent(layout);
		toolBar.setExpandRatio(layout, 1);
		
		theToolBarPanel.setContent(toolBar);
	}
	
	public void updateActions(){
		if(theCanvas.hasSelectedResidues()){
			for(Component component:componentsWithResidueSelectionDependency){
				component.setEnabled(true);
			}

			for(MenuItem menuItem:menuItemsWithResidueSelectionDependency){
				menuItem.setEnabled(true);
			}
			
		}else{
			for(MenuItem menuItem:menuItemsWithResidueSelectionDependency){
				menuItem.setEnabled(false);
			}
			
			for(Component component:componentsWithResidueSelectionDependency){
				component.setEnabled(false);
			}
		}
	}
	
	public void automaticHeightAdjust(boolean automaticallyAdjustHeight){
		this.automaticallyAdjustHeight=automaticallyAdjustHeight;
	}
	
	private void updateCanvasHeight(){
		if(automaticallyAdjustHeight){
			int proposedHeight=theCanvas.getHeight();
			if(theCanvas.getWorkspace().getGlycanRenderer().getRenderMode()==GlycanRendererMode.TOOLBAR){
				proposedHeight+=2;
			}else{
				proposedHeight+=100;
			}
			
			int proposedWidth=theCanvas.getWidth();
			
			setMinimumSize(proposedWidth, proposedHeight);
		}
	}
	
	@Override
	public void recieveSelectionUpdate(double x, double y, double width,double height,boolean mouseMoved) {
		final Residue selectedResidue=theCanvas.getCurrentResidue();
		
		theCanvas.selectIntersectingRectangles(x, y, width, height, mouseMoved);
		
		if(theCanvas.getCurrentResidue()!=null && selectedResidue==theCanvas.getCurrentResidue() && selectedResidue.isRepetition()){
			final Window window=new Window("Repeatition options");
			
			VerticalLayout layout=new VerticalLayout();
			
			final TextField minRep=new TextField("Minimum");
			final TextField maxRep=new TextField("Maximum");
			NativeButton okBut=new NativeButton("Ok");
			NativeButton cancelBut=new NativeButton("Cancel");
			
			minRep.setImmediate(true);
			maxRep.setImmediate(true);
			
			minRep.setValue(String.valueOf(selectedResidue.getMinRepetitions()));
			maxRep.setValue(String.valueOf(selectedResidue.getMaxRepetitions()));
		
			okBut.addClickListener(new ClickListener(){
				private static final long serialVersionUID = -408364885359729326L;

				@Override
				public void buttonClick(ClickEvent event) {
					String minRepNum=(String) minRep.getValue();
					String maxRepNum=(String) maxRep.getValue();
					
					boolean valid=true;
					
					try{
						Integer.parseInt(minRepNum);
						Integer.parseInt(maxRepNum);
					}catch(NumberFormatException ex){
						valid=false;
					}
					
					if(valid){
						selectedResidue.setMinRepetitions((String) minRep.getValue());
						selectedResidue.setMaxRepetitions((String) maxRep.getValue());
						
						theCanvas.documentUpdated();
					}
					
					UI.getCurrent().removeWindow(window);
				}
			});
			
			cancelBut.addClickListener(new ClickListener(){
				private static final long serialVersionUID = -657746118918366530L;

				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().removeWindow(window);
				}
			});
			
			layout.addComponent(minRep);
			layout.addComponent(maxRep);
			
			HorizontalLayout buttonLayout=new HorizontalLayout();
			buttonLayout.addComponent(okBut);
			buttonLayout.addComponent(cancelBut);
			
			layout.addComponent(buttonLayout);
			
			window.center();
			window.setContent(layout);
			
			window.getContent().setSizeUndefined();
			window.setSizeUndefined();
			
			UI.getCurrent().addWindow(window);
		}
	}

	@Override
	public synchronized void glycanCanvasUpdated() {
		updateActions();
		updateCanvasHeight();
	}

	@Override
	public void selectionChanged() {
		System.err.println("Updating linkage toolbar");
		updateLinkageToolBar();
	}

	@Override
	public void setLocalResourceWatcher(LocalResourceWatcher watcher) {
		localResourceWatchers.add(watcher);
	}

	@Override
	public void removeLocalResourceWatcher(LocalResourceWatcher watcher) {
		localResourceWatchers.remove(watcher);
	}

	@Override
	public void massOptions(MassOptions massOptions,Collection<Glycan> structures) {
		Collection<Glycan> glycans=theCanvas.getSelectedStructures();
		if(glycans.size()==0){
			glycans=theCanvas.theDoc.getStructures();
		}
		
		theCanvas.theDoc.setMassOptions(glycans, massOptions);
		
		theCanvas.theWorkspace.getDefaultMassOptions().setValues(massOptions);
		
		theCanvas.documentUpdated();
	}

	public synchronized void resizeCanvas() {

	}
	
//	@Override
//	public void paintContent(PaintTarget target) throws PaintException {
//		super.paintContent(target);
//		
//		/**
//		 * - When a page is refreshed we currently don't have a method of reinstalling the client side resize repeat command
//		 * - So this is just a hack to get around this limitation
//		 */
//		updateCanvasHeight();
//	}

	public void setWorkspace(BuilderWorkspace workspace) {
		theCanvas.setWorkspace(workspace);
	}
	
	public void setNotation(String notation){
		theCanvas.setNotation(notation);
	}

	public BuilderWorkspace getWorkspace() {
		return theCanvas.getWorkspace();
	}
	
	private void reportMessage(String message,Exception ex){
		reportMessage(message);
		
		reportException(ex);
	}
	
	private void reportException(Exception ex){
		ex.printStackTrace();    
	}
	
	private void reportMessage(String message){
		MessageDialogBox box=new MessageDialogBox("Notification", message);
		box.setVisible(true);
		box.center();
	}
}
