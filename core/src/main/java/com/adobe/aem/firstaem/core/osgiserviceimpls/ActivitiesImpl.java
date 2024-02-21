package com.adobe.aem.firstaem.core.osgiserviceimpls;

import java.util.Random;

import org.osgi.service.component.annotations.Component;

import com.adobe.aem.firstaem.core.osgiservices.Activities;


@Component( 
		service = {Activities.class}
		)
public class ActivitiesImpl implements Activities {
	
	private static final String[] ACTIVITIES = new String[] {"Camping","Skiing","Stakeboarding"};
	
	
	private final Random random = new Random();
	

	@Override
	public String getRandomActivity() {
		int radomIndex = random.nextInt(ACTIVITIES.length);
		
		return ACTIVITIES[radomIndex];
	}

}
