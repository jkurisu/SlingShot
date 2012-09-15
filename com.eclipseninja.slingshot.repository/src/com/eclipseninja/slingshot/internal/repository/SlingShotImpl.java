package com.eclipseninja.slingshot.internal.repository;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipseninja.slingshot.repository.SlingShot;
import com.eclipseninja.slingshot.repository.SlingShotException;

public class SlingShotImpl implements SlingShot {
	
	private static final Logger logger = LoggerFactory.getLogger(SlingShotImpl.class);

	private SlingRepository repository;
	private EventAdmin eventAdmin;
	private ConfigurationAdmin configurationAdmin;
	
//	private ResourceResolverFactory resolverFactory;
	

	public void setSlingRepository(SlingRepository repository) {
		this.repository = repository;
	}

	public void unsetSlingRepository(SlingRepository repository) {
		this.repository = null;
	}

//	public void setResourceResolverFactory(
//			ResourceResolverFactory resolverFactory) {
//		this.resolverFactory = resolverFactory;
//	}

//	public void unsetResourceResolverFactory(
//			ResourceResolverFactory resolverFactory) {
//		this.resolverFactory = null;
//	}

	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	public void unsetEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = null;
	}
	
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
	}

	public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = null;
	}

	
//	@Override
//	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
//		logger.debug("updating");
//	}
	
	
	protected void activate(ComponentContext context) {
		logger.info("activating");
	}
	
	protected void deactivate(ComponentContext context) {
		logger.info("deactivating");
	}
	
	protected void updated(ComponentContext context) {
		logger.info("updating");
	}

	@Override
	public void logout(String username) throws SlingShotException {
		logger.info("Logging out: " + username);
	}
	
	private static final String SERVICE_PID = "SlingShot";

	@Override
	public void timeout(long value) throws SlingShotException {
		logger.info("Setting timeout: " + value);
		try {
			Configuration configuration = configurationAdmin.getConfiguration(SERVICE_PID, null);
			Dictionary<String, Object> properties = configuration.getProperties();
			if(properties == null) {
				properties = new Hashtable<String, Object>();
				properties.put("service.pid", SERVICE_PID);
			}
			properties.put("slingshot.login.timeout", value);
			configuration.update(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Session login(String username, String password) throws SlingShotException {
		logger.info("Logging in " + username);
		try {
			return repository.login(new SimpleCredentials(username, password.toCharArray()));
		} catch (LoginException e) {
			throw new SlingShotException(e);
		} catch (RepositoryException e) {
			throw new SlingShotException(e);
		}
	}


}
