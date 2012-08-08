/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.studio.editor.action.pdf;


import org.eclipse.ui.IWorkbenchPart;


public class PdfActionHeading3 extends PdfActionAbstact {
	
	/** Id of the actions */
	public static final String ID_Heading3_Full = "PdfAction_Heading3_Full"; //$NON-NLS-1$
	public static final String ID_Heading3_Start = "PdfAction_Heading3_Start"; //$NON-NLS-1$
	public static final String ID_Heading3_End = "PdfAction_Heading3_End"; //$NON-NLS-1$
	public static final String ID_Heading3_None = "PdfAction_Heading3_None"; //$NON-NLS-1$
	
	
	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 * @param action_position
	 * 					Identify The position of the label
	 */
	public PdfActionHeading3(IWorkbenchPart part,Position action_position) {
		super(part, action_position, ID_Heading3_Full, ID_Heading3_Start, ID_Heading3_End, ID_Heading3_None);
	}

	/**
	 * method to return the property name 
	 * @return Property for which one the value must be changed
	 */
	protected String GetPropertyName(){
		return "net.sf.jasperreports.export.pdf.tag.h3";
	}

}
