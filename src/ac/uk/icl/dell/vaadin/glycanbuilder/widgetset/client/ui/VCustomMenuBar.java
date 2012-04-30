package ac.uk.icl.dell.vaadin.glycanbuilder.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.ui.VMenuBar;


/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VCustomMenuBar extends VMenuBar {
	public VCustomMenuBar() {
		super();
	}
	
	public VCustomMenuBar(boolean subMenu, VMenuBar parentMenu) {
		super(subMenu,parentMenu);
	}
}
