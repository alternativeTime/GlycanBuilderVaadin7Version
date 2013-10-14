package org.vaadin.hezamu.canvas.widgetset.client.ui;

import ac.uk.icl.dell.gwt.canvas.client.ClientSideFontCanvas;

import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Trivial wrapper for GWTCanvas that allows us to register mouse event
 * listeners
 * 
 * @author Henri Muurimaa
 */
public class MouseHandlingCanvas extends ClientSideFontCanvas implements
		HasMouseDownHandlers, HasMouseMoveHandlers,HasMouseUpHandlers {
	
	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return canvas.addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return canvas.addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return canvas.addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public void fireEvent(GwtEvent<?> event){
		// TODO Auto-generated method stub
		
	}
}
