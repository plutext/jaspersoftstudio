/*
 * JasperReports - Free Java Reporting Library. Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of JasperReports.
 * 
 * JasperReports is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * JasperReports is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with JasperReports. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.studio.property.section;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

import com.jaspersoft.studio.editor.report.EditorContributor;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.SetValueCommand;

/*
 * Abstract class for a section in a tab in the properties view.
 */
public abstract class AbstractSection extends AbstractPropertySection implements PropertyChangeListener {

	private List<APropertyNode> elements;
	private APropertyNode element;
	private EditDomain editDomain;

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#setInput(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		setInputC(part, selection);
	}

	protected void setInputC(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			element = null;
			elements = new ArrayList<APropertyNode>();
			List<?> selected = ((IStructuredSelection) selection).toList();
			for (Object item : selected) {
				if (item instanceof EditPart) {
					APropertyNode model = getModelFromEditPart(item);
					if (model != null) {
						if (element == null) {
							EditorContributor provider = (EditorContributor) part.getAdapter(EditorContributor.class);
							if (provider != null)
								setEditDomain(provider.getEditDomain());
							if (getElement() != model) {
								if (getElement() != null)
									getElement().getPropertyChangeSupport().removePropertyChangeListener(this);
								setElement((APropertyNode) model);
								getElement().getPropertyChangeSupport().addPropertyChangeListener(this);
							}
						}

						elements.add((APropertyNode) model);
					}
				}
			}
		}
	}

	protected APropertyNode getModelFromEditPart(Object item) {
		Object model = ((EditPart) item).getModel();
		if (model instanceof APropertyNode)
			return (APropertyNode) model;
		return null;
	}

	public void setElement(APropertyNode element) {
		this.element = element;
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.view.ITabbedPropertySection#aboutToBeShown()
	 */
	public void aboutToBeShown() {
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.view.ITabbedPropertySection#aboutToBeHidden()
	 */
	public void aboutToBeHidden() {
	}

	@Override
	public void dispose() {
		if (getElement() != null)
			getElement().getPropertyChangeSupport().removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * Get the element.
	 * 
	 * @return the element.
	 */
	public APropertyNode getElement() {
		return element;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (!isDisposed()) {
			isRefreshing = true;
			refresh();
			isRefreshing = false;
		}
	}

	protected boolean isRefreshing = false;

	public void changeProperty(String property, Object newValue) {
		if (!isRefreshing && elements != null && !elements.isEmpty() && getEditDomain() != null) {
			CommandStack cs = getEditDomain().getCommandStack();
			CompoundCommand cc = new CompoundCommand("Set " + property);
			for (APropertyNode n : elements) {
				Command c = changeProperty(property, newValue, n);
				if (c != null)
					cc.add(c);
			}
			cs.execute(cc);
		}
	}

	protected Command changeProperty(String property, Object newValue, APropertyNode n) {
		Object oldValue = n.getPropertyValue(property);
		if (((oldValue == null && newValue != null) || (oldValue != null && newValue == null) || (newValue != null && !newValue
				.equals(oldValue))) && getEditDomain() != null) {
			SetValueCommand setCommand = new SetValueCommand(n.getDisplayText());
			setCommand.setTarget(n);
			setCommand.setPropertyId(property);
			setCommand.setPropertyValue(newValue);
			return setCommand;
		}
		return null;
	}

	public List<APropertyNode> getElements() {
		return elements;
	}

	public abstract boolean isDisposed();
}
