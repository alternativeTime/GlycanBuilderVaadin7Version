package ac.uk.icl.dell.vaadin.navigator7.pages.buildingblocks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ac.uk.icl.dell.vaadin.LocalResourceWatcher;
import ac.uk.icl.dell.vaadin.MessageDialogBox;
import ac.uk.icl.dell.vaadin.glycanbuilder.GlycanBuilderWindow;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;

public abstract class NavigatorFileUpload {
	File file;
	
	List<LocalResourceWatcher> theWatchers;
	
	Refresher refresher;
	Upload upload;
	
	private volatile SucceededEvent succeededEvent;
	private volatile FailedEvent failedEvent;

	private Message msg;
	
	public NavigatorFileUpload(String buttonCaption){
		initialise(buttonCaption);
	}
	
	public NavigatorFileUpload(String buttonCaption,Upload.Receiver receiver){
		initialise(buttonCaption, receiver);
	}
	
	protected void initialise(String buttonCaption){
		initialise(buttonCaption,new Upload.Receiver(){
			private static final long serialVersionUID=-4952934177751492620L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType){
				FileOutputStream fos = null;
		        try {
		        	file = File.createTempFile("import",".seq");
		        	
		        	for(LocalResourceWatcher watcher:theWatchers){
		        		watcher.addLocalResource(file);
		        	}
		        	
		            fos = new FileOutputStream(file);
		        } catch (IOException e) {
		        	msg=new Message("IO error reading uploaded file", e);
		        	
		        	if(file.exists()){
						file.delete();
					}
		        	
		        	return null;
		        } 
		        
		        return fos;
			}
		});
	}
	
	protected void initialise(String buttonCaption,Upload.Receiver receiver){
		upload=new Upload(buttonCaption,receiver);
		
		upload.addSucceededListener(new Upload.SucceededListener(){
			private static final long serialVersionUID=-2110272574466389181L;

			@Override
			public void uploadSucceeded(SucceededEvent event){
				succeededEvent=event;
			}
		});
		
		upload.addFailedListener(new Upload.FailedListener(){
			private static final long serialVersionUID=6938546297736635515L;

			@Override
			public void uploadFailed(FailedEvent event){
				failedEvent=event;
				
				if(file != null && file.exists()){
					file.delete();
				}
			}
		});
		
		upload.addStartedListener(new StartedListener(){
			private static final long serialVersionUID=7860277448399016656L;

			@Override
			public void uploadStarted(StartedEvent event){
				Component parent=upload.getParent();
				if(parent instanceof ComponentContainer){
						refresher=new Refresher();
						
						refresher.addListener(new RefreshListener(){
							private static final long serialVersionUID=-8257231567232966938L;

							@Override
							public void refresh(Refresher source){
								
								//((ComponentContainer) refresher.getParent()).removeComponent(refresher);
								refresher.getParent().removeExtension(refresher);
								
								if(failedEvent!=null){
									uploadFailed(failedEvent);
								}else if(succeededEvent!=null){
									uploadSucceeded(succeededEvent);
								}else if(msg!=null){
									uploadFailed(msg);									
								}
								
								failedEvent=null;
								succeededEvent=null;
								msg=null;
							}
						});
										
					//((ComponentContainer)parent).addComponent(refresher);
					((GlycanBuilderWindow)UI.getCurrent()).addExtension(refresher);

				}else{
					new MessageDialogBox("Error", "Parent of upload component must be an instanceof ComponentContainer");
				}
			}
		});
	}

	public abstract void uploadFailed(FailedEvent failedEvent);
	public abstract void uploadFailed(Message msg);
	public abstract void uploadSucceeded(SucceededEvent succeededEvent);

	public void setLocalResourceWatchers(List<LocalResourceWatcher> theWatchers){
		this.theWatchers=theWatchers;
	}
	
	public Upload getUploadComponent(){
		return upload;
	}
	
	public File getUploadedFile(){
		return file;
	}
	
	protected class Message{
		String msg;
		Exception ex;
		
		public Message(String msg){
			this.msg=msg;
		}
		
		public Message(String msg, Exception ex){
			this.msg=msg;
			this.ex=ex;
		}

		public String getMessage(){
			return this.msg;
		}
	}
}