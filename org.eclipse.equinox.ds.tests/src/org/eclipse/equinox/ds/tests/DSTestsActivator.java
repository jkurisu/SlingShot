/*******************************************************************************
 * Copyright (c) 1997-2009 by ProSyst Software GmbH
 * http://www.prosyst.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    ProSyst Software GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.ds.tests;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class DSTestsActivator implements BundleActivator {

	private static DSTestsActivator instance;
	private BundleContext context;
	
	public DSTestsActivator() {
		instance = this;
	}
	public void start(BundleContext context) throws Exception {
		this.context = context;
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
	}

	public static BundleContext getContext() {
		return instance != null ? instance.context : null;
	}
	
	public static void activateSCR() {
		activateBundle("org.eclipse.equinox.ds");
		activateBundle("org.eclipse.equinox.cm");
		activateBundle("org.eclipse.equinox.log");
		activateBundle("org.eclipse.equinox.util");
	}
	
	private static void activateBundle(String symbolicName) {
		if (instance != null) {
			Bundle[] bundles = instance.context.getBundles();
			for (int i = 0; i < bundles.length; i++) {
				if (symbolicName.equals(bundles[i].getSymbolicName())) {
					if (bundles[i].getState() != Bundle.ACTIVE)
						try {
							bundles[i].start(Bundle.START_TRANSIENT);
						} catch (BundleException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}
}
