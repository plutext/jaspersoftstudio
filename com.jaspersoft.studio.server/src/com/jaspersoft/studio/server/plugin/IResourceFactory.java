/*******************************************************************************
 * Copyright (C) 2010 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, 
 * the following license terms apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jaspersoft Studio Team - initial API and implementation
 ******************************************************************************/
package com.jaspersoft.studio.server.plugin;

import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.server.model.MResource;

public interface IResourceFactory {
	public MResource getResource(ANode parent, ResourceDescriptor resource,
			int index);

	public IWizardPage getResourcePage(ANode parent, MResource resource);

	public ANode createNewResource(MRoot root, ANode parent);

	public ANode createNewDatasource(MRoot root, ANode parent);
}
