package com.adobe.aem.firstaem.core.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.firstaem.core.bean.ArticleListBean;
import com.adobe.aem.firstaem.core.models.ArticleListTest;
import com.adobe.aem.firstaem.core.osgiservices.Activities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
		methods = {HttpConstants.METHOD_POST},
        resourceTypes = "sling/servlet/default",
		extensions = {"json","html"},
		selectors = {"getArticleList1"}
		)
public class Myservlet extends  SlingAllMethodsServlet {
	public static final Logger LOGGER = LoggerFactory.getLogger(Myservlet.class);
	private static final String RESOURCE_COMPONENT_PATH="/content/firstaem/us/en/myservlettest/jcr:content/root/container/articlelist";
	
	//@Reference(target=("country=in)")
	@Reference
	public Activities activities;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		LOGGER.debug("Inside my Custom Headless Sling Servlet");
		LOGGER.debug("OSGi service is been used in the servlet -Randon Number: "+ activities.getRandomActivity());
		
		//Fetching ResourceResolver Object from SlingFrame through Request Object
		//As Sling Framewo
		ResourceResolver resourceResolver = request.getResourceResolver();
		Resource resource = resourceResolver.getResource(RESOURCE_COMPONENT_PATH);
		
		ArticleListTest articleListBean = resource.adaptTo(ArticleListTest.class);
		
		List<ArticleListBean> articleListBeanList = articleListBean.getArticleListBeans();
		
		//Building Json response with the given Bean list 
		ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonArticleListBeanList = objectWriter.writeValueAsString(articleListBeanList);
		
		response.setContentType("application/json");
		response.getWriter().write(jsonArticleListBeanList);
		
	}
	/*
	@Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("This is Default Servlet GET method ");
    }*/
	

}
