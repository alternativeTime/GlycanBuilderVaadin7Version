package ac.uk.icl.dell.vaadin.canvas.hezamu.canvas.client.ui;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface CanvasServerRpc extends ServerRpc {
	public void clicked(MouseEventDetails med);

	public void imagesLoaded();
	
	public void mouseDown(int x, int y);
}
