package com.adobe.aem.firstaem.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@Component(service = {Servlet.class},
property = {
		   "sling.servlet.paths=/bin/example/jcrsql2"
})
public class SQL2Servlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SQL2Servlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
	
		LOGGER.debug("Inside doGet() JCRSQL2 Servlet ");
		
		Session session = request.getResourceResolver().adaptTo(Session.class);
		
		//Query to get all pages ending with "e" letter under /content/we-retail : Result : 20
		String myQuery = "SELECT * FROM [cq:PageContent] AS nodes WHERE ISDESCENDANTNODE ([/content/we-retail])AND nodes.[jcr:title] LIKE \"%_e\"";
		
		//Create Query object
		QueryManager queryManager;
		Query query = null ;
		try {
			queryManager = session.getWorkspace().getQueryManager();
			query = queryManager.createQuery(myQuery,Query.JCR_SQL2);
			LOGGER.debug("Query :  "+query.toString());
		
		} catch (InvalidQueryException  e) {
			LOGGER.debug("Invalid Query Exceeption:"+e.getMessage());
			e.printStackTrace();
		} catch (RepositoryException e) {
			LOGGER.debug("Repository Exceeption:"+e.getMessage());
			e.printStackTrace();
		}
		
		
		
		try {
			//Execute the Query
			QueryResult queryResult = query.execute();
		    // Get the nodes from the query result
			NodeIterator nodeIterator = queryResult.getNodes();
					
			LOGGER.debug("QueryResult.getNodes () sizr: "+nodeIterator.getSize());
			List<String> pagePathList = new ArrayList<String>();
			//Getting pagePath for each Node 
			while(nodeIterator.hasNext())
			{
				Node pageNode = nodeIterator.nextNode();
				pagePathList.add(pageNode.getPath());
			}
			
			//Create a Json file to store the content
			JsonObject jsonResponseObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			
			//Add the paths to Json Object			
			for(String nodepath: pagePathList)
			{
				jsonArray.add(nodepath);
			}
			
			//Add the Json  array to Json Object
			jsonResponseObject.add("query results", jsonArray);
			
			//Set the Json response content type
			response.setContentType("application/json");
			
			PrintWriter printWriter = response.getWriter();
			printWriter.print(jsonResponseObject.toString());
			printWriter.flush();			
			
			
			
			
		} catch (RepositoryException e) {
			LOGGER.debug("Repository Exceptiona at the END",e.getMessage());
			e.printStackTrace();
		}		
    
	}
	
}
