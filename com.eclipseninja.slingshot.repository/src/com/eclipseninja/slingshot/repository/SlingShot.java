package com.eclipseninja.slingshot.repository;

import javax.jcr.Session;

public interface SlingShot {
	
	public Session login(String username, String password) throws SlingShotException;
	
	public void logout(String username) throws SlingShotException;
	
	public void timeout(long value) throws SlingShotException;
	
}
