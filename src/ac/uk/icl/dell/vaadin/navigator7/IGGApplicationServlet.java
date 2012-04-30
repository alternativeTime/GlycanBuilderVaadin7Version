package ac.uk.icl.dell.vaadin.navigator7;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.vaadin.navigator7.NavigableApplicationServlet;


public class IGGApplicationServlet extends NavigableApplicationServlet{
	private static final long serialVersionUID=5352670715311623077L;

	@Override
	protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title,
			String themeUri, HttpServletRequest request) throws IOException {
		super.writeAjaxPageHtmlHeader(page, title, themeUri, request);
		
//		System.err.println("<script type=\"text/javascript\" src=\""+ themeUri + "/js/selectivizr-min.js\"></script>");
//		
//		page.write("<script type=\"text/javascript\" src=\""+ themeUri + "/js/selectivizr-min.js\"></script>");
//		page.write("<script type=\"text/javascript\" src=\""+ themeUri + "/js/flexie.js\"></script>");
	}
}
