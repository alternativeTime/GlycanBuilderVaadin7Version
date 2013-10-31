package com.example.whatever;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme("whatever")
public class WhateverUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = WhateverUI.class, widgetset = "com.example.whatever.widgetset.WhateverWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	
	
	@Override
	protected void init(VaadinRequest request) {
		
	}

}