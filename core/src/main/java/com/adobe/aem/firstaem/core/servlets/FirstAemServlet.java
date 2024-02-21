package com.adobe.aem.firstaem.core.servlets;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component(  service =  {Servlet.class})
@SlingServletResourceTypes(
		resourceTypes = "firstaem/components/page",
		selectors = "firstaem",
		extensions = "json",
		methods = HttpConstants.METHOD_GET
		)
public class FirstAemServlet extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = 1L;
	public static final Logger LOGGER = LoggerFactory.getLogger(FirstAemServlet.class) ;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("application/json");
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("FirstName", "Srikanth Reddy");
		jsonObjectBuilder.add("LastName","Shoda");
		
		LOGGER.debug("Welcome to AEM First - Sling Servlet! ");
		response.getWriter().write(jsonObjectBuilder.build().toString());
	}
	
	

}
