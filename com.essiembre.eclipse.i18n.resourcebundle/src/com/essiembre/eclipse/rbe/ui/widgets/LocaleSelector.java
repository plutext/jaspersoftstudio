/*
 * Copyright (C) 2003, 2004  Pascal Essiembre, Essiembre Consultant Inc.
 * 
 * This file is part of Essiembre ResourceBundle Editor.
 * 
 * Essiembre ResourceBundle Editor is free software; you can redistribute it 
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * Essiembre ResourceBundle Editor is distributed in the hope that it will be 
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Essiembre ResourceBundle Editor; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */
package com.essiembre.eclipse.rbe.ui.widgets;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.essiembre.eclipse.rbe.RBEPlugin;
import com.essiembre.eclipse.rbe.ui.UIUtils;

/**
 * Composite for dynamically selecting a locale from a list of available
 * locales.
 * @author Pascal Essiembre (essiembre@users.sourceforge.net)
 * @version $Author: essiembre $ $Revision: 1.4 $ $Date: 2005/12/12 04:11:19 $
 */
public class LocaleSelector extends Composite {

    private static final String DEFAULT_LOCALE = "[" //$NON-NLS-1$
            + RBEPlugin.getString("editor.default") //$NON-NLS-1$ 
            + "]"; //$NON-NLS-1$
    
    protected Locale[] availableLocales;
    protected Combo localesCombo;
    protected Text langText;
    protected Text countryText;
    protected Text variantText;

    protected Group selectionGroup;
    
    /**
     * Constructor.
     * @param parent parent composite
     */
    public LocaleSelector(Composite parent) {
        super(parent, SWT.NONE);

        // Init available locales
        availableLocales = Locale.getAvailableLocales();
        Arrays.sort(availableLocales, new Comparator<Locale>() {
            
        	@Override
        	public int compare(Locale locale1, Locale locale2) {
                return Collator.getInstance().compare(locale1.getDisplayName(), locale2.getDisplayName());
            }
        });
        
        // This layout
        GridLayout layout = new GridLayout();
        setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 20;
        
        // Group settings
        selectionGroup = new Group(this, SWT.NULL);
        layout = new GridLayout(3, false);
        selectionGroup.setLayout(layout);
        selectionGroup.setText(RBEPlugin.getString(
                "selector.title")); //$NON-NLS-1$
        // Set locales drop-down
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        localesCombo = new Combo(selectionGroup, SWT.READ_ONLY);
        localesCombo.setLayoutData(gd);
        localesCombo.add(DEFAULT_LOCALE);
        for (int i = 0; i < availableLocales.length; i++) {
            localesCombo.add(availableLocales[i].getDisplayName());
        }
        localesCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int index = localesCombo.getSelectionIndex();
                if (index == 0) { // default
                    langText.setText(""); //$NON-NLS-1$
                    countryText.setText(""); //$NON-NLS-1$
                } else {
                    Locale locale = availableLocales[index -1];
                    langText.setText(locale.getLanguage());
                    countryText.setText(locale.getCountry());
                }
                variantText.setText(""); //$NON-NLS-1$
            }
        });

        // Language field
        langText = new Text(selectionGroup, SWT.BORDER);
        langText.setTextLimit(3);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = UIUtils.getWidthInChars(langText, 4);
        langText.setLayoutData(gd);
        langText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                langText.setText(langText.getText().toLowerCase());
                setLocaleOnlocalesCombo();
            }
        });

        // Country field
        countryText = new Text(selectionGroup, SWT.BORDER);
        countryText.setTextLimit(2);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = UIUtils.getWidthInChars(countryText, 4);
        countryText.setLayoutData(gd);
        countryText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                countryText.setText(
                        countryText.getText().toUpperCase());
                setLocaleOnlocalesCombo();
            }
        });

        // Variant field
        variantText = new Text(selectionGroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = UIUtils.getWidthInChars(variantText, 4);
        variantText.setLayoutData(gd);
        variantText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                setLocaleOnlocalesCombo();
            }
        });
        
        // Labels
        Label lblLang = new Label(selectionGroup, SWT.NULL);
        lblLang.setText(RBEPlugin.getString("selector.language")); //$NON-NLS-1$
        lblLang.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));

        Label lblCountry = new Label(selectionGroup, SWT.NULL);
        lblCountry.setText(RBEPlugin.getString(
                "selector.country")); //$NON-NLS-1$
        lblCountry.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));

        Label lblVariant = new Label(selectionGroup, SWT.NULL);
        lblVariant.setText(RBEPlugin.getString(
                "selector.variant")); //$NON-NLS-1$
        lblVariant.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
    }

    /**
     * Gets the selected locale.  Default locale is represented by a 
     * <code>null</code> value.
     * @return selected locale
     */
    public Locale getSelectedLocale() {
        String lang = langText.getText().trim();
        String country = countryText.getText().trim();
        String variant = variantText.getText().trim();
        
        if (lang.length() > 0 && country.length() > 0 && variant.length() > 0) {
            return new Locale(lang, country, variant);
        } else if (lang.length() > 0 && country.length() > 0) {
            return new Locale(lang, country);
        } else if (lang.length() > 0) {
            return new Locale(lang);
        } else {
            return null;
        }
    }
    
    private int getLocaleIndex(Locale locale){
    	for (int i=0; i<availableLocales.length; i++){
    		if (availableLocales[i].equals(locale)){
    	        return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * Set the selected locale in the combo
     * 
     * @param locale locale to select
     */
    public void selectLocale(Locale locale){
    	int localeIndex = getLocaleIndex(locale);
    	//If the locale is not found i do a less restrictive research based only on the language
    	if (localeIndex == -1) localeIndex = getLocaleIndex(new Locale(locale.getLanguage()));
    	if (localeIndex != -1) {
    		localesCombo.select(localeIndex+1);
    		Locale foundLocale = availableLocales[localeIndex];
            langText.setText(foundLocale.getLanguage());
            countryText.setText(foundLocale.getCountry());
    	}
    }

    /**
     * Sets an available locale on the available locales combo box.
     */
    /*default*/ void setLocaleOnlocalesCombo() {
        Locale locale = new Locale(
                langText.getText(),
                countryText.getText(),
                variantText.getText());
        int index = -1;
        for (int i = 0; i < availableLocales.length; i++) {
            Locale availLocale = availableLocales[i];
            if (availLocale.equals(locale)) {
                index = i + 1;
            }
        }
        if (index >= 1) {
            localesCombo.select(index);
        } else {
            localesCombo.clearSelection();
        }
    }
    
    /**
     * Adds a modify listener.
     * @param listener modify listener
     */
    public void addModifyListener(final ModifyListener listener) {
        langText.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                listener.modifyText(e);
            }
        });
        countryText.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                listener.modifyText(e);
            }
        });
        variantText.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                listener.modifyText(e);
            }
        });
    }
}
