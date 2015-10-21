/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.itemproperty.desc;

import java.util.List;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.StandardItemProperty;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.util.ItemPropertyUtil;
import com.jaspersoft.studio.utils.Misc;

/**
 * @author Veaceslav Chicu (schicu@users.sourceforge.net)
 * 
 */
public abstract class ADescriptor {
	protected boolean showAllProperties = false;
	protected ItemPropertyDescription<?>[] itemProperties;

	public ADescriptor() {
	}

	public boolean isShowAllProperties() {
		return showAllProperties;
	}

	/**
	 * @return the itemProperties
	 */
	public ItemPropertyDescription<?>[] getItemPropertyDescriptors() {
		if (itemProperties == null)
			initItemPropertyDescriptors();
		return itemProperties;
	}

	public ItemPropertyDescription<?> getDescription(String id) {
		for (ItemPropertyDescription<?> ip : getItemPropertyDescriptors())
			if (ip.getName().equals(id))
				return ip;
		return null;
	}

	public void setupDefaultValue(Item selected, StandardItemProperty newitem) {
	}

	public String getDisplayName() {
		return Messages.ADescriptor_0;
	}

	protected abstract void initItemPropertyDescriptors();

	protected List<ItemData> itemDatas;
	protected ItemData itemData;
	protected Item item;
	protected ItemProperty oldItemProperty;
	protected APropertyNode pnode;

	public Image getIcon(Object element) {
		if (element instanceof ItemData)
			return JaspersoftStudioPlugin.getInstance().getImage("icons/resources/datasets-16.png"); //$NON-NLS-1$
		return null;
	}

	public void setOldItemProperty(ItemProperty oldItemProperty) {
		this.oldItemProperty = oldItemProperty;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}

	public ItemData getItemData() {
		return itemData;
	}

	public void setItemDatas(List<ItemData> itemDatas, APropertyNode pnode) {
		this.itemDatas = itemDatas;
		this.pnode = pnode;
	}

	public void validateItem(ItemProperty itemProperty) throws Exception {
		if (itemProperty != null) {
			if (Misc.isNullOrEmpty(itemProperty.getName()))
				throw new Exception(Messages.ADescriptor_2);
			for (ItemProperty ip : item.getProperties()) {
				if (oldItemProperty == ip)
					continue;
				if (ip.getName().equals(itemProperty.getName()))
					throw new Exception(Messages.ADescriptor_3);
			}
		} else
			for (ItemData id : itemDatas) {
				if (id.getItems() == null)
					continue;
				for (Item it : id.getItems()) {
					if (it.getProperties() == null)
						continue;
					for (ItemPropertyDescription<?> ipd : getItemPropertyDescriptors()) {
						if (ipd.isMandatory()) {
							ItemProperty p = ItemPropertyUtil.getProperty(it.getProperties(), ipd.getName());
							if (p == null
									|| ((p.getValueExpression() == null || Misc.isNullOrEmpty(p.getValueExpression().getText())) && Misc
											.isNullOrEmpty(p.getValue())))
								throw new Exception(ipd.getLabel() + " is mandatory property.");
						}
					}
				}
			}
	}

}