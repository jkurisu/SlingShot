package com.eclipseninja.slingshot.internal.repository.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipseninja.slingshot.repository.SlingShot;
import com.eclipseninja.slingshot.repository.SlingShotException;

public class SlingShotCommand {
	
	private static final Logger logger = LoggerFactory.getLogger(SlingShotCommand.class);
	
	private SlingShot slingShot;

	public void setSlingShot(SlingShot slingShot) {
		logger.info("Running SlingShotCommand.setSlingShot(): " + slingShot);
		this.slingShot = slingShot;
	}
	
	public void login(String[] values) {
		logger.info("login: ");
		for (String value : values) {
			logger.info("\t" + value);
		}
		try {
			slingShot.login(values[0], values[1]);
		} catch (SlingShotException e) {
			e.printStackTrace();
		}
	}
	
	public void logout(String[] values) {
		logger.info("logout: ");
		for (String value : values) {
			logger.info("\t" + value);
		}
	}
	
	public void timeout(long value) {
		logger.info("timeout: " + value);
		try {
			slingShot.timeout(value);
		} catch (SlingShotException e) {
			e.printStackTrace();
		}
	}
}
