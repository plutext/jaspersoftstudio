/*
 * generated by Xtext
 */
package com.jaspersoft.studio.editor.jrexpressions;

import com.jaspersoft.studio.editor.jrexpressions.scoping.JRImportedNamespaceAwareLocalScopeProvider;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class JavaJRExpressionRuntimeModule extends com.jaspersoft.studio.editor.jrexpressions.AbstractJavaJRExpressionRuntimeModule {

	@Override
	public void configureIScopeProviderDelegate(com.google.inject.Binder binder) {
		binder.bind(org.eclipse.xtext.scoping.IScopeProvider.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(JRImportedNamespaceAwareLocalScopeProvider.class);
	}
	
}
