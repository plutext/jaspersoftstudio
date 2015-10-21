/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor.outline.actions;

import java.util.List;

import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.band.MBand;

/*
 * /* The Class CreateBandAction.
 */
public class CreateBandAction extends ACreateAndSelectAction {

	/** The Constant ID. */
	public static final String ID = "create_band"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public CreateBandAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
		setCreationFactory(new JDPaletteCreationFactory(MBand.class));
	}

	@Override
	protected boolean calculateEnabled() {
		if (!checkAllSelectedObjects(MBand.class)) {
			return false;
		} else {
			List<Object> elements = editor.getSelectionCache().getSelectionModelForType(MBand.class);
			for (Object obj : elements) {
				if (!(obj instanceof MBand))
					return false;
				if (((MBand) obj).getValue() != null)
					return false;
				if (((MBand) obj).getBandType() == BandTypeEnum.DETAIL)
					return false;
			}
			command = createCommand();
			return command != null && command.canExecute();
		}
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateBandAction_create_band);
		setToolTipText(Messages.CreateBandAction_create_band_tool_tip);
		setId(CreateBandAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}

	@Override
	public void run() {
		super.run();
		ISelection s = getSelection();
		if (s instanceof StructuredSelection) {
			Object obj = ((StructuredSelection) s).getFirstElement();
			if (obj instanceof EditPart) {
				EditPart editPart = (EditPart) obj;

				StructuredSelection newselection = new StructuredSelection(editPart.getParent());
				setSelection(newselection);
				getWorkbenchPart().getSite().getSelectionProvider().setSelection(newselection);

				setSelection(s);
				getWorkbenchPart().getSite().getSelectionProvider().setSelection(s);
			}
		}
	}
}