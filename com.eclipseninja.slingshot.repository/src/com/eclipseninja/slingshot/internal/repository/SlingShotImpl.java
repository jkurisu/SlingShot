package com.eclipseninja.slingshot.internal.repository;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipseninja.slingshot.repository.SlingShot;
import com.eclipseninja.slingshot.repository.SlingShotException;

public class SlingShotImpl implements SlingShot {
	
	private static final Logger logger = LoggerFactory.getLogger(SlingShotImpl.class);

	private SlingRepository repository;
	private EventAdmin eventAdmin;
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

	@Override
	public Session login(String username, String password) throws SlingShotException {
		logger.debug("Logging in " + username);
		try {
			return repository.login(new SimpleCredentials(username, password.toCharArray()));
		} catch (LoginException e) {
			throw new SlingShotException(e);
		} catch (RepositoryException e) {
			throw new SlingShotException(e);
		}
	}
	
	
	
	

}
