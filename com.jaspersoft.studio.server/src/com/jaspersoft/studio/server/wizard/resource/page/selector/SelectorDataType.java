/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.server.wizard.resource.page.selector;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.server.model.MDataType;
import com.jaspersoft.studio.server.model.AMResource;

public class SelectorDataType extends ASelector {

	@Override
	protected ResourceDescriptor createLocal(AMResource res) {
		ResourceDescriptor rd = MDataType.createDescriptor(res);
		rd.setName("DataType");
		rd.setLabel(rd.getName());
		return rd;
	}

	@Override
	protected boolean isResCompatible(AMResource r) {
		return r instanceof MDataType;
	}

	@Override
	protected boolean isResCompatible(ResourceDescriptor r) {
		return r.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE);
	}

	protected ResourceDescriptor getResourceDescriptor(ResourceDescriptor ru) {
		for (Object obj : ru.getChildren()) {
			ResourceDescriptor r = (ResourceDescriptor) obj;
			if (r.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE))
				return r;
		}
		return null;
	}

	@Override
	protected String[] getIncludeTypes() {
		return new String[] { ResourceMediaType.DATA_TYPE_CLIENT_TYPE };
	}

	@Override
	protected String[] getExcludeTypes() {
		return null;
	}

}
