package com.eclipseninja.slingshot.application.navigators;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Binary;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;

import org.apache.sling.jcr.api.SlingRepository;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.eclipseninja.slingshot.application.Activator;

public class RepositoryNavigator {

	@Inject
	private SlingRepository repository;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ESelectionService selectionService;

	// @Inject
	// private ResourceResolverFactory resolverFactory;

	// @Inject
	// private MimeTypeService mimeTypeService;

	@PostConstruct
	public void createControls(Composite parent) {
		final TreeViewer viewer = new TreeViewer(parent);
		viewer.setLabelProvider(new ILabelProvider() {

			private List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();

			@Override
			public void removeListener(ILabelProviderListener listener) {
				listeners.remove(listener);
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				listeners.remove(listeners);
			}

			@Override
			public String getText(Object element) {
				try {
					if (element instanceof Node) {
						Node node = (Node) element;
						return node.getName();
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Image getImage(Object element) {
				try {
					if (element instanceof Node) {
						Node node = (Node) element;
						String name = node.getName();
						return Activator.getInstance().getImageRegistry()
								.get(Activator.SAMPLE_ICON);
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		viewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(Object element) {
				try {
					if (element instanceof Node) {
						Node node = (Node) element;
						return node.hasNodes();
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				try {
					if (element instanceof Node) {
						Node node = (Node) element;
						return node.getParent();
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				try {
					if (inputElement instanceof Node) {
						Node node = (Node) inputElement;
						NodeIterator iterator = node.getNodes();
						List<Node> copy = new ArrayList<Node>();
						while (iterator.hasNext()) {
							copy.add(iterator.nextNode());
						}
						return copy.toArray(new Node[0]);
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return getElements(parentElement);
			}
		});

		try {
			// Session session = repository.login(new SimpleCredentials("admin",
			// "admin".toCharArray()));
			Session session = repository.login(new SimpleCredentials(
					"anonymous", "anonymous".toCharArray()));
			Node rootNode = session.getRootNode();
			viewer.setInput(rootNode);

			viewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					selectionService.setSelection(selection.getFirstElement());
				}
			});
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Focus
	private void setFocus() {
		System.out.println("RepositoryNavigator.setFocus()");
	}

	@PreDestroy
	public void dispose() {
		System.out.println("RepositoryNavigator.dispose()");
	}

	@Persist
	public void save() {
		System.out.println("RepositoryNavigator.save()");
	}

	@Inject
	public void setNode(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Node node) {
		if (node != null) {
			try {
				String name = node.getName();
				System.out.println("Name: " + name);
				NodeType[] mixinNodeTypes = node.getMixinNodeTypes();
				if (mixinNodeTypes != null && mixinNodeTypes.length > 0) {
					System.out.println("\tMixin Node Types: ");
					for (NodeType nodeType : mixinNodeTypes) {
						System.out.println("\t\t" + nodeType.getName());
					}
				}
				PropertyIterator propertyIterator = node.getProperties();
				if (propertyIterator != null && propertyIterator.getSize() > 0) {
					System.out.println("\tNode Properties: ");
					while (propertyIterator.hasNext()) {
						Property property = propertyIterator.nextProperty();
						System.out.print("\t\t" + property.getName() + ":" + property.getType() + ":");
						if (property.isMultiple()) {
							Value[] values = property.getValues();
							for (Value value : values) {
								processValue(value);
							}
						} else {
							Value value = property.getValue();
							processValue(value);
						}
					}
				}
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}

	private void processValue(Value value) throws RepositoryException,
			ValueFormatException {
		switch (value.getType()) {
		case PropertyType.BINARY:
			Binary binary = value.getBinary();
			System.out.println("binary");
			break;
		case PropertyType.BOOLEAN:
			boolean b = value.getBoolean();
			System.out.println(b);
			break;
		case PropertyType.DATE:
			Calendar date = value.getDate();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			System.out.println(format.format(date.getTime()));
			break;
		case PropertyType.DECIMAL:
			BigDecimal bigDecimal = value.getDecimal();
			System.out.println("BigDecimal: " + bigDecimal);
			break;
		case PropertyType.DOUBLE:
			double d = value.getDouble();
			System.out.println("double: " + d);
			break;
		case PropertyType.LONG:
			long l = value.getLong();
			System.out.println("long: " + l);
			break;
		case PropertyType.NAME:
			String str = value.getString();
			System.out.println(str);
			break;
		case PropertyType.PATH:
			System.out.println("path");
			break;
		case PropertyType.REFERENCE:
			System.out.println("reference");
			break;
		case PropertyType.STRING:
			String s = value.getString();
			System.out.println(s);
			break;
		case PropertyType.UNDEFINED:
			System.out.println("Undefined");
			break;
		case PropertyType.URI:
			System.out.println("URI");
			break;
		case PropertyType.WEAKREFERENCE:
			System.out.println("Weak Reference");
			break;
		default:
			System.out.println("Default");
			break;
		}
	}

}
