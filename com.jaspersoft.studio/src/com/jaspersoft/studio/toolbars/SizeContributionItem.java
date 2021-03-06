/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.toolbars;

import java.util.List;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.size.Size2BorderAction;
import com.jaspersoft.studio.editor.gef.commands.ResizeCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * Toolbar buttons to change the size of an element
 * 
 * @author Orlandin Marco
 *
 */
public class SizeContributionItem extends CommonToolbarHandler{

	private SelectionAdapter pushButtonPressed = new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			List<Object> selection = getSelectionForType(MGraphicElement.class);
			if (selection.isEmpty())
				return;
			
			JSSCompoundCommand compoundCmd =  new JSSCompoundCommand("Resize Operation", null); 
			int alignment = (Integer)e.widget.getData(WIDGET_DATA_KEY);
			for (Object model : selection) {
				compoundCmd.add(new ResizeCommand(alignment, (MGraphicElement)model));
				compoundCmd.setReferenceNodeIfNull(model);
			}
			
			if (compoundCmd != null){
				CommandStack cs = getCommandStack();
				cs.execute(compoundCmd);
			}
		}
	};
	
	@Override
	protected Control createControl(Composite parent) {
		ToolBar buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
		
		ToolItem sizeButton = new ToolItem(buttons, SWT.PUSH);
		sizeButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control_width.gif"));
		sizeButton.setData(WIDGET_DATA_KEY, Size2BorderAction.WIDTH);
		sizeButton.setToolTipText(Messages.Size2BorderAction_fit_width_tool_tip);
		sizeButton.addSelectionListener(pushButtonPressed);
		
		sizeButton = new ToolItem(buttons, SWT.PUSH);
		sizeButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control_height.gif"));
		sizeButton.setData(WIDGET_DATA_KEY, Size2BorderAction.HEIGHT);
		sizeButton.setToolTipText(Messages.Size2BorderAction_fit_height_tool_tip);
		sizeButton.addSelectionListener(pushButtonPressed);
		
		sizeButton = new ToolItem(buttons, SWT.PUSH);
		sizeButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control.gif"));
		sizeButton.setData(WIDGET_DATA_KEY, Size2BorderAction.BOTH);
		sizeButton.setToolTipText(Messages.Size2BorderAction_fit_both_tool_tip);
		sizeButton.addSelectionListener(pushButtonPressed);
		
		return buttons;
	}
	
	@Override
	protected boolean fillWithToolItems(ToolBar parent) {
		ToolItem sizeButtonWidth = new ToolItem(parent, SWT.PUSH);
		sizeButtonWidth.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control_width.gif"));
		sizeButtonWidth.setData(WIDGET_DATA_KEY,Size2BorderAction.WIDTH);
		sizeButtonWidth.setToolTipText(Messages.Size2BorderAction_fit_width_tool_tip);
		sizeButtonWidth.addSelectionListener(pushButtonPressed);
		getToolItems().add(sizeButtonWidth);
		
		ToolItem sizeButtonHeight = new ToolItem(parent, SWT.PUSH);
		sizeButtonHeight.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control_height.gif"));
		sizeButtonHeight.setData(WIDGET_DATA_KEY,Size2BorderAction.HEIGHT);
		sizeButtonHeight.setToolTipText(Messages.Size2BorderAction_fit_height_tool_tip);
		sizeButtonHeight.addSelectionListener(pushButtonPressed);
		getToolItems().add(sizeButtonHeight);
		
		ToolItem sizeButtonBoth = new ToolItem(parent, SWT.PUSH);
		sizeButtonBoth.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseapps/size_to_control.gif"));
		sizeButtonBoth.setData(WIDGET_DATA_KEY,Size2BorderAction.BOTH);
		sizeButtonBoth.setToolTipText(Messages.Size2BorderAction_fit_both_tool_tip);
		sizeButtonBoth.addSelectionListener(pushButtonPressed);
		getToolItems().add(sizeButtonBoth);
		
		return true;
	}
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		List<Object> selection = getSelectionForType(MGraphicElement.class);
		return selection.size() > 0;
	}
}
