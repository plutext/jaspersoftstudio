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
package com.jaspersoft.studio.rcp.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.handlers.RestartWorkbenchHandler;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.ISourceProviderService;

import com.jaspersoft.studio.ConfigurationManager;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

/**
 * 
 * Action called when the the user select a new language
 * 
 * @author Orlandin Marco
 * 
 */
@SuppressWarnings("restriction")
public class SwitchLanguageHandler extends AbstractHandler implements
		IElementUpdater {

	private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$

	private static final String PROP_EXIT_DATA = "eclipse.exitdata"; //$NON-NLS-1$

	/**
	 * Execute the command, read the regional code from the parameter passed by
	 * the plugin file and call the method to write the regional code to the
	 * configuration. If the configuration is modified than call a restart
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String locale = event
				.getParameter("com.jaspersoft.studio.switchlanguage.locale"); //$NON-NLS-1$
		boolean needToRestart = changeLocale(locale);
		if (needToRestart) {
			MessageDialog dialog = new MessageDialog(UIUtils.getShell(),
					Messages.SwitchLanguageHandler_restartTitle, null,
					Messages.SwitchLanguageHandler_restartMessage,
					MessageDialog.QUESTION, new String[] { Messages.common_yes,
							Messages.common_no }, 1);
			int selection = dialog.open();
			if (selection == 0) {
				// Some OS (linux\mac) dosen't reload the configuration file
				// after the restart. So when eclipse is
				// re-launched it is done with the -nl parameter to the new
				// locale. Essentially it's like it is launched
				// from command line with the explicit nl parameter
				String command_line = ConfigurationManager
						.buildCommandLineNl(locale);
				System.setProperty(PROP_EXIT_DATA, command_line);
				System.setProperty(PROP_EXIT_CODE,
						IApplication.EXIT_RELAUNCH.toString());
				return new RestartWorkbenchHandler().execute(event);
			} else {
				// Request an update of the locale provider and force the update
				// of the menu item, in this way the language
				// menu is show updated even without a restart
				IWorkbenchWindow window = HandlerUtil
						.getActiveWorkbenchWindow(event);
				ISourceProviderService service = (ISourceProviderService) window
						.getService(ISourceProviderService.class);
				LocaleSourceProvider sessionSourceProvider = (LocaleSourceProvider) service
						.getSourceProvider(LocaleSourceProvider.ACTUAL_LOCALE);
				sessionSourceProvider.forceRefreshLocale();
				ICommandService commandService = (ICommandService) window
						.getService(ICommandService.class);
				commandService.refreshElements(
						"com.jaspersoft.studio.switchlanguage.command", null);
			}
		}
		return null;
	}

	/**
	 * Take the actual language code and if it is the same of the updated
	 * element that the element is marked as checked
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void updateElement(UIElement element, Map parameters) {
		element.setChecked(LocaleSourceProvider.getLocale().equals(
				parameters.get("com.jaspersoft.studio.switchlanguage.locale")));
	}
	
	/**
	 * Convert a list of strings to a single string, each string except the 
	 * last has a new line after it 
	 * 
	 * @param list the list of string
	 * @return a single string with all the string in the list concatenated
	 */
	private static String listToString(List<String> list){
		StringBuilder result = new StringBuilder();
		if (!list.isEmpty()){
			String lastLine = list.get(list.size()-1);
			String separator = System.getProperty("line.separator");
			for (String outLine : list) {
				result.append(outLine);
				if (outLine != lastLine){
					result.append(separator);
				}
			}
		}
		return result.toString();
	}

	/**
	 * Read the configuration file of the application and rewrite it with a new
	 * regional code if the code is changed then it is also requested a platform
	 * restart. The regional code will be set at the place of the old code if
	 * found, otherwise before the first parameter found between -clean, -vm,
	 * -vmargs. If none of this parameters are found then it is set at the end
	 * of the file
	 * 
	 * @param locale
	 * @return
	 */
	private static boolean changeLocale(String locale) {
		boolean fileChanged = false;
		if (ConfigurationManager.isConfigurationAccessibleWithMessage()){
			File configurationFile = ConfigurationManager.getApplicationConfigurationFile();
			BufferedReader in = null; 
			BufferedWriter out = null;
			try {
				in = new BufferedReader(new FileReader(configurationFile));
				String line = in.readLine();
				List<String> configLines = new ArrayList<String>();
				int localePosition = -1;
				int lineNumber = 0;
				while (line != null) {
					if (line.equals("-nl"))localePosition = lineNumber + 1; //$NON-NLS-1$
					else if (localePosition == -1
							&& (line.equals("-vmargs") || line.equals("-clean") || line.equals("-vm"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						configLines.add("-nl"); //$NON-NLS-1$
						configLines.add(""); //$NON-NLS-1$
						localePosition = lineNumber + 1;
					}
					configLines.add(line);
					lineNumber++;
					line = in.readLine();
				}
				if (localePosition != -1) {
					if (configLines.get(localePosition).equals(locale)) {
						FileUtils.closeStream(in);
						FileUtils.closeStream(out);
						// The file has already the right regional code, there
						// is no need to restart eclipse
						return false;
					} else{
						//Change the locale value
						configLines.set(localePosition, locale);
					}
				} else {
					configLines.add("-nl"); //$NON-NLS-1$
					configLines.add(locale);
				}
				fileChanged = ConfigurationManager.writeConfigurationFile(listToString(configLines));
			} catch (Exception ex){
				ex.printStackTrace();
				JaspersoftStudioPlugin.getInstance().logError(ex);
				// Configuration file not found, show an error message
				MessageDialog.openWarning(UIUtils.getShell(),
						Messages.SwitchLanguageHandler_errorTitle,
						MessageFormat.format(
								Messages.SwitchLanguageHandler_errorMessage,
								new Object[] { configurationFile.getAbsolutePath() }));
			} finally {
				FileUtils.closeStream(in);
				FileUtils.closeStream(out);
				
			}
		}
		return fileChanged;
	}
}
