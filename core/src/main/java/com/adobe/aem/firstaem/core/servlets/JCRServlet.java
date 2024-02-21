package com.adobe.aem.firstaem.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = {Servlet.class},
           property = {
        		   "sling.servlet.paths=/bin/example/demo"
           })
public class JCRServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException {
		
		 //Getting data  from a Form 
		 
		 String firstName 	= "Srikanth" ;/*request.getParameter("fName");*/
		 String lastName 	= "Shoda" ;//request.getParameter("lName");
		 String age 		= "40";//request.getParameter("age");
		 
		 String nodeName="";
		 //Values to set at the Node
		 Map<String,Object> nodePropertyMap = new HashMap<String, Object>();
		 nodePropertyMap.put("firstName", firstName);
		 nodePropertyMap.put("lastName", lastName);
		 nodePropertyMap.put("age", age);
		 
		 
		 ResourceResolver resourceResolver = request.getResourceResolver();
		 Resource nodeResource = resourceResolver.getResource("/content/firstaem/us/en/hellocomp/jcr:content") ;
		 		 
		 try {
			Resource newResource = resourceResolver.create(nodeResource, "newNode", nodePropertyMap);
			resourceResolver.commit();
			
			nodeName = newResource.getName();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
		 //resourceResolver.close();
		 
		 try {
			 
			response.getWriter().write("New Node created : Name : "+ nodeName);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		 
		 
	}

}
