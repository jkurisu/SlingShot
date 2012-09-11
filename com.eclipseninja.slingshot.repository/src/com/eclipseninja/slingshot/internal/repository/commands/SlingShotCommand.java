package com.eclipseninja.slingshot.internal.repository.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipseninja.slingshot.repository.SlingShot;
import com.eclipseninja.slingshot.repository.SlingShotException;

public class SlingShotCommand {
	
	private static final Logger logger = LoggerFactory.getLogger(SlingShotCommand.class);
	
	private SlingShot slingShot;

	public void setSlingShot(SlingShot slingShot) {
		logger.debug("Running SlingShotCommand.setSlingShot(): " + slingShot);
		this.slingShot = slingShot;
	}
	
	public void login(String[] values) {
		logger.debug("login: ");
		for (String value : values) {
			logger.debug("\t" + value);
		}
		try {
			slingShot.login(values[0], values[1]);
		} catch (SlingShotException e) {
			e.printStackTrace();
		}
	}
	
	public void logout(String[] values) {
		logger.debug("logout: ");
		for (String value : values) {
			logger.debug("\t" + value);
		}
	}
}
