package com.adobe.aem.firstaem.core.models;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.firstaem.core.bean.ArticleListBean;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleListTest.class);
	//global desclaration
	List<ArticleListBean> articleListBeans = null;
	
	@Inject
	private String articleRootPath;
	
	@Self
	Resource resource;
	
	public String getArticleRootPath() {
		return articleRootPath;
	}
	

	public List<ArticleListBean> getArticleListBeans(){
		return articleListBeans;
	}
	
	@PostConstruct
	private void init() {
		
		ResourceResolver resourceResolver = resource.getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		
		
		Map<String,String> predicateMap = new HashMap<String, String>();
		predicateMap.put("path", articleRootPath);
		predicateMap.put("type", "cq:Page");
		Query query = null;
		
		try {
			query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
		}catch (Exception e) {
			LOGGER.error("Error in the Query ",e);
		}
		
		
		
		SearchResult searchResult = query.getResult();
		//initialize
		articleListBeans = new ArrayList<ArticleListBean>();
		
		for(Hit hit : searchResult.getHits())
		{
			try {
				
				ArticleListBean articleListBean = new ArticleListBean();
				String path  = hit.getPath();// will return only the path of the Node& Resource
				//Using ResourceResolver at this path , we will get the Resouce ( Node & Resouce 
				// has a DB sesion, so You can perform CRUD operation 
				Resource articleResource =  resourceResolver.getResource(path);
			   // to get the Page, why? i.e jcr:content too/
				//We need to adapt it to Page class to access Jcr:Content
				Page articlePage = articleResource.adaptTo(Page.class);
				
				//Accessing Page properties
				String title= articlePage.getTitle();
				String description = articlePage.getDescription();
				
				//Logging all the three values
				LOGGER.debug("Page Path:{} \nPage Title:{}, \nPage Description:{}", articleRootPath,title,description);
				
				articleListBean.setPath(path);
				articleListBean.setTitle(title);
				articleListBean.setDescription(description);
				
				articleListBeans.add(articleListBean);
				
			}catch (RepositoryException re) {
				LOGGER.error("Repostory Error", re.getMessage());
				re.getStackTrace();
			}
		}

	}
	
}
