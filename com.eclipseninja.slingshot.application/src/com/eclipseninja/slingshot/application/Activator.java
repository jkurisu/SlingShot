package com.eclipseninja.slingshot.application;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String SAVE_EDIT_ICON = "save.edit.icon";

	public static final String SAMPLE_ICON = "sample.icon";

	private static BundleContext context;
	
	private ImageRegistry imageRegistry = null;
	
	private static Activator activator;

	static BundleContext getContext() {
		return context;
	}
	
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		activator = this;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if(imageRegistry != null) {
			imageRegistry.dispose();
            imageRegistry = null;
		}
		activator = null;
		Activator.context = null;
	}

	public ImageRegistry getImageRegistry() {
		if(imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			initializeImageRegistry();
		}
		return imageRegistry;
	}
	
	private void initializeImageRegistry() {
//		FileLocator.findEntries(context.getBundle(), new Path("icons/"));
		imageRegistry.put(SAMPLE_ICON, ImageDescriptor.createFromURL(FileLocator.find(context.getBundle(), new Path("icons/sample.gif"), null)));
		imageRegistry.put(SAVE_EDIT_ICON, ImageDescriptor.createFromURL(FileLocator.find(context.getBundle(), new Path("icons/save_edit.gif"), null)));
		
	}



	public static Activator getInstance() {
		return activator;
	}

}
