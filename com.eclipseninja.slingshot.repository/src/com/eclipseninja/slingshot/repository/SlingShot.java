package com.eclipseninja.slingshot.repository;

import javax.jcr.Session;

public interface SlingShot {
	
	public Session login(String username, String password) throws SlingShotException;
	
}
