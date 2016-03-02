/*
 * Copyright (c) 2014. Matthew Campbell <matthew.campbell@mq.edu.au>, David R. Damerell <david@nixbioinf.org>.
 *
 * This file is part of GlycanBuilder Vaadin Release and its affliated projects EUROCarbDB, UniCarb-DB and UniCarbKB.
 *
 * This program is free software free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GlycanBuilder Vaadin Release is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License (LICENSE.txt) for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GlycanBuilder Vaadin Release.  If not, see <http ://www.gnu.org/licenses/>.
 */
package ac.uk.icl.dell.vaadin.glycanbuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.GlycanParserFactory;
import org.eurocarbdb.application.glycanbuilder.GlycanRendererAWT;
import org.eurocarbdb.application.glycanbuilder.LogUtils;
import org.eurocarbdb.application.glycanbuilder.LoggerStorage;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageImpl;
import org.eurocarbdb.application.glycanbuilder.LoggerStorageIndex;
import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Extension;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("ucdb_2011theme")
public class GlycanBuilderWindow extends UI{

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = GlycanBuilderWindow.class, widgetset = "ac.uk.icl.dell.vaadin.glycanbuilder.widgetset.GlycanbuilderWidgetset")
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = 4956233270191237091L;
        /**
         * Override to handle the CORS requests.
         */
        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException,
                IOException {

            // Origin is needed for all CORS requests
            String origin = request.getHeader("Origin");
            if (origin != null && isAllowedRequestOrigin(origin)) {

                // Handle a preflight "option" requests
                if ("options".equalsIgnoreCase(request.getMethod())) {
                    response.addHeader("Access-Control-Allow-Origin", origin);
                    response.setHeader("Allow",
                            "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
 
                    // allow the requested method
                    String method = request
                            .getHeader("Access-Control-Request-Method");
                    response.addHeader("Access-Control-Allow-Methods", method);

                    // allow the requested headers
                    String headers = request
                            .getHeader("Access-Control-Request-Headers");
                    response.addHeader("Access-Control-Allow-Headers", headers);

                    response.addHeader("Access-Control-Allow-Credentials",
                            "true");
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().flush();
                    return;
                } // Handle UIDL post requests
                else if ("post".equalsIgnoreCase(request.getMethod())) {
                    response.addHeader("Access-Control-Allow-Origin", origin);
                    response.addHeader("Access-Control-Allow-Credentials",
                            "true");
                    super.service(request, response);
                    return;
                }
            }

            // All the other requests nothing to do with CORS
            super.service(request, response);

        }

        /**
         * Check that the page Origin header is allowed.
         */
        private boolean isAllowedRequestOrigin(String origin) {
            // TODO: Remember to limit the origins.
            return origin.startsWith("*.expasy.org") || origin.startsWith("*.isb-sib.ch");
        }
	}
	private GlycanBuilder theBuilder; 
	private static final long serialVersionUID=-4407090778568443024L;
	private static LoggerStorage loggerStorage = new LoggerStorageImpl();
	private static LoggerStorageIndex logger = new LoggerStorageIndex() {
		
		@Override
		public LoggerStorage getLogger() {
			return loggerStorage;
		}
	};


	@Override
	public void init(VaadinRequest request){
		JavaScript.getCurrent().addFunction("exportCanvas", new JavaScriptFunction() {
            public void call(JSONArray arguments) throws JSONException {
                //Notification.show(arguments.getString(1));
            	//0 - format
            	//1 - callback
            	String type= arguments.getString(0);
				String sequence=null;
				String callback =  arguments.getString(1);
				if(type.equals("glycoct_condensed")){
					sequence=theBuilder.theCanvas.theCanvas.theDoc.toGlycoCTCondensed();
				}else if(type.equals("glycoct")){
					sequence=theBuilder.theCanvas.theCanvas.theDoc.toGlycoCT();
				}else{
					try {
						sequence=theBuilder.theCanvas.theCanvas.theDoc.toString(GlycanParserFactory.getParser(type));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if(sequence == null) sequence = "sequence is null";
				try {
					sequence = URLEncoder.encode(sequence, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.err.println("Executing :" +callback+".run('"+sequence+"')");
				JavaScript.eval(callback+".run(unescape('"+sequence+"'))");
				
            }
        });
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		theBuilder = new GlycanBuilder();
		layout.addComponentAsFirst(theBuilder);
		theBuilder.setSizeFull();
		setContent(layout);
	}
	
	public void respondToExport(String callback, String response) {
		response = response.replaceAll("\\n", "");
		
		System.err.println("Executing :" +callback+".run('"+response+"')");
		JavaScript.eval(callback+".run('"+response+"')");
	}
	public void addExtension (Extension extension)
	{
		super.addExtension(extension);
	}
	
	public static void initialiseStaticResources(){
		/**
		 * There are some static dictionary type resources that must be initialised for glycan parsing etc..
		 * 
		 * BuilderWorkspace takes care to initialise these resources if it's own static field "loaded" is false
		 * during initialisation.
		 * 
		 * Not all uses of these static resources are bounded by BuilderWorkspace instances, so we create a new
		 * instance here so that they are loaded.
		 * 
		 * BuilderWorkspace and all other GlycanBuilder and GlycoWorkbench code wasn't meant to be run within the
		 * context of a web server.  There are therefore potential issues anywhere a static field is not final and
		 * initialised on declaration - loaded is one such example (it needs synchronised access).
		 */
		@SuppressWarnings("unused")
		BuilderWorkspace workspace=new BuilderWorkspace(new GlycanRendererAWT());
		
		LogUtils.setLookupLogger(logger);
	}
	
}
