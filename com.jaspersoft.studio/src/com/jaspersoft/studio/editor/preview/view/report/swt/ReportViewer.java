/*******************************************************************************
 * Copyright (C) 2010 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.studio.editor.preview.view.report.swt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.view.JRHyperlinkListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jasperassistant.designer.viewer.ReportViewerEvent;
import com.jaspersoft.studio.plugin.IEditorContributor;

public class ReportViewer implements IReportViewer {

	private EventListenerList listenerList = new EventListenerList();

	private JasperPrint document;

	private double zoom = 1.0f;

	private int pageIndex;

	private int style;

	private ViewerCanvas viewerComposite;

	private List<JRHyperlinkListener> hyperlinkListeners;
	private JasperReportsContext jContext;

	/**
	 * Default constructor. The default style will be used for the SWT control associated to the viewer.
	 */
	public ReportViewer(JasperReportsContext jContext) {
		this(SWT.NONE, jContext);
	}

	/**
	 * Constructor that allows to specify a SWT control style. For possible styles see the
	 * {@link org.eclipse.swt.widgets.Canvas} class. Most frequently you will wont to specify the
	 * <code>SWT.NONE<code> style.
	 * 
	 * @param style
	 *          the style
	 */
	public ReportViewer(int style, JasperReportsContext jContext) {
		this.style = style;
		this.jContext = jContext;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#setDocument(net.sf.jasperreports.engine.JasperPrint)
	 */
	public void setDocument(JasperPrint document) {
		try {
			//		Assert.isNotNull(document, "ReportViewer.documentNotNull"); //$NON-NLS-1$
			//		Assert.isNotNull(document.getPages(), "ReportViewer.documentNotEmpty"); //$NON-NLS-1$
			//		Assert.isTrue(!document.getPages().isEmpty(), "ReportViewer.documentNotEmpty"); //$NON-NLS-1$

			this.document = document;
			this.pageIndex = Math.min(Math.max(0, pageIndex), getPageCount() - 1);
			setZoomInternal(computeZoom());
			fireViewerModelChanged();
		} catch (OutOfMemoryError e) {
			this.document = null;
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#hasDocument()
	 */
	public boolean hasDocument() {
		return document != null;
	}

	public JasperPrint getDocument() {
		return document;
	}

	public void setZoom(double zoom) {
		if (!canChangeZoom())
			return;

		if (Math.abs(zoom - getZoom()) > 0.00001) {
			setZoomInternal(zoom);
			fireViewerModelChanged();
		}
	}

	public boolean canChangeZoom() {
		return hasDocument();
	}

	private void setZoomInternal(double zoom) {
		this.zoom = zoom;
	}

	private int zoomMode = ZOOM_MODE_NONE;

	public int getZoomMode() {
		return zoomMode;
	}

	public void setZoomMode(int zoomMode) {
		if (!canChangeZoom())
			return;

		if (zoomMode != getZoomMode()) {
			this.zoomMode = zoomMode;
			setZoomInternal(computeZoom());
			fireViewerModelChanged();
		}
	}

	public double[] getZoomLevels() {
		return zoomLevels;
	}

	public void setZoomLevels(double[] levels) {
		Assert.isNotNull(levels);
		Assert.isTrue(levels.length > 0);
		this.zoomLevels = levels;
	}

	public void zoomIn() {
		if (canZoomIn())
			setZoom(getNextZoom());
	}

	public boolean canZoomIn() {
		return hasDocument() && getZoom() < getMaxZoom();
	}

	public void zoomOut() {
		if (canZoomOut())
			setZoom(getPreviousZoom());
	}

	private static final double[] DEFAULT_ZOOM_LEVELS = new double[] { 0.5f, 0.75f, 1.0f, 1.25f, 1.50f, 1.75f, 2.0f };
	private double[] zoomLevels = DEFAULT_ZOOM_LEVELS;

	private double getMinZoom() {
		return zoomLevels[0];
	}

	private double getMaxZoom() {
		return zoomLevels[zoomLevels.length - 1];
	}

	private double getNextZoom() {
		for (int i = 0; i < zoomLevels.length; i++) {
			if (zoom < zoomLevels[i])
				return zoomLevels[i];
		}

		return getMaxZoom();
	}

	private double getPreviousZoom() {
		for (int i = zoomLevels.length - 1; i >= 0; i--) {
			if (zoom > zoomLevels[i])
				return zoomLevels[i];
		}

		return getMinZoom();
	}

	public boolean canZoomOut() {
		return hasDocument() && getZoom() > getMinZoom();
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#getZoom()
	 */
	public double getZoom() {
		return zoom;
	}

	private double computeZoom() {
		switch (zoomMode) {
		case ZOOM_MODE_ACTUAL_SIZE:
			return 1.0;
		case ZOOM_MODE_FIT_WIDTH: {
			double ratio = ratio(viewerComposite.getFitSize().x, document.getPageWidth());
			return ratio(
					viewerComposite.getFitSize((int) (document.getPageWidth() * ratio), (int) (document.getPageHeight() * ratio)).x,
					document.getPageWidth());
		}
		case ZOOM_MODE_FIT_HEIGHT: {
			double ratio = ratio(viewerComposite.getFitSize().y, document.getPageHeight());
			return ratio(
					viewerComposite.getFitSize((int) (document.getPageWidth() * ratio), (int) (document.getPageHeight() * ratio)).y,
					document.getPageHeight());
		}
		case ZOOM_MODE_FIT_PAGE:
			Point fitSize = viewerComposite.getFitSize();
			return Math.min(ratio(fitSize.x, document.getPageWidth()), ratio(fitSize.y, document.getPageHeight()));
		}

		return zoom;
	}

	private double ratio(int a, int b) {
		return (a * 100 / b) / 100.0;
	}

	private int getPageCount() {
		return document == null ? 0 : document.getPages().size();
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#getPageIndex()
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#setPageIndex(int)
	 */
	public void setPageIndex(int pageIndex) {
		if (pageIndex != getPageIndex()) {
			this.pageIndex = Math.min(Math.max(0, pageIndex), getPageCount() - 1);
			fireViewerModelChanged();
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#canGotoFirstPage()
	 */
	public boolean canGotoFirstPage() {
		return hasDocument() && pageIndex > 0;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#gotoFirstPage()
	 */
	public void gotoFirstPage() {
		if (canGotoFirstPage()) {
			setPageIndex(0);
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#canGotoLastPage()
	 */
	public boolean canGotoLastPage() {
		return hasDocument() && pageIndex < getPageCount() - 1;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#gotoLastPage()
	 */
	public void gotoLastPage() {
		if (canGotoLastPage()) {
			setPageIndex(getPageCount() - 1);
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#canGotoNextPage()
	 */
	public boolean canGotoNextPage() {
		return hasDocument() && pageIndex < getPageCount() - 1;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#gotoNextPage()
	 */
	public void gotoNextPage() {
		if (canGotoNextPage()) {
			setPageIndex(pageIndex + 1);
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#canGotoPreviousPage()
	 */
	public boolean canGotoPreviousPage() {
		return hasDocument() && pageIndex > 0;
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#gotoPreviousPage()
	 */
	public void gotoPreviousPage() {
		if (canGotoPreviousPage()) {
			setPageIndex(pageIndex - 1);
		}
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#addReportViewerListener(com.jasperassistant.designer.viewer.IReportViewerListener)
	 */
	public void addReportViewerListener(IReportViewerListener listener) {
		listenerList.add(IReportViewerListener.class, listener);
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#removeReportViewerListener(com.jasperassistant.designer.viewer.IReportViewerListener)
	 */
	public void removeReportViewerListener(IReportViewerListener listener) {
		listenerList.remove(IReportViewerListener.class, listener);
	}

	private void fireViewerModelChanged() {
		Object[] listeners = listenerList.getListenerList();
		ReportViewerEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IReportViewerListener.class) {
				if (e == null) {
					e = new ReportViewerEvent(this);
				}
				((IReportViewerListener) listeners[i + 1]).viewerStateChanged(e);
			}
		}
	}

	/**
	 * Creates the SWT control for the report viewer. Later calls to this method will return the same instance of the
	 * control.
	 * 
	 * @param parent
	 *          the parent
	 * @return the created control
	 */
	public Control createControl(Composite parent) {
		if (viewerComposite == null) {
			viewerComposite = new ViewerCanvas(parent, style, jContext) {
				/**
				 * @see com.jasperassistant.designer.viewer.ViewerCanvas#resize()
				 */
				protected void resize() {
					setZoom(computeZoom());
					super.resize();
				}
			};
			viewerComposite.setReportViewer(this);
		}

		return viewerComposite;
	}
	
	/**
	 * Save to the specified location an image of the current page
	 * 
	 * @param file the absolute path of the image file to create
	 * 
	 * @param width the width of the image to create, if the width is less or equal than 0 than 
	 * the real width of the image will be used. Otherwise the image will be resized
	 * 
	 * @param height the height of the image to create, if the height is less or equal than 0 than 
	 * the real height of the image will be used. Otherwise the image will be resized
	 */
  public void exportImage(String file, int width, int height) {
  	 ImageLoader loader = new ImageLoader();
  	 Image actualImage = viewerComposite.getActualImage();
  	 int resizeWidth = width > 0 ? width : actualImage.getBounds().width;
  	 int resizeHeight = height>0 ? height : actualImage.getBounds().height;
     loader.data = new ImageData[] { actualImage.getImageData().scaledTo(resizeWidth, resizeHeight) };
     loader.save(file, SWT.IMAGE_PNG);
  }
  
  /**
   * Return a string that represent the location of the jrxml file
   * 
   * @return an absolute path to the showed jrxml file
   */
  public String getReportPath(){
  	IFile reportFile = (IFile)jContext.getValue(IEditorContributor.KEY_FILE);
		String fileName = reportFile.getName();
		String path = reportFile.getLocation().toPortableString();
		path = path.substring(0, path.lastIndexOf(fileName));
		return path;
  }
  
  /**
   * The name of the report shown
   * 
   * @return The name of the report file without the extension
   */
	public String getReportName(){
		IFile reportFile = (IFile)jContext.getValue(IEditorContributor.KEY_FILE);
		String fileName = reportFile.getName();
		String extension = reportFile.getFileExtension();
		String path = reportFile.getLocation().toPortableString();
		path = path.substring(0, path.lastIndexOf(fileName));
		fileName = fileName.substring(0, fileName.lastIndexOf(extension)-1);
		return fileName;
	}
	

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#addHyperlinkListener(net.sf.jasperreports.view.JRHyperlinkListener)
	 */
	public void addHyperlinkListener(JRHyperlinkListener listener) {
		if (hyperlinkListeners == null) {
			hyperlinkListeners = new ArrayList<JRHyperlinkListener>();
		} else {
			hyperlinkListeners.remove(listener); // add once
		}

		hyperlinkListeners.add(listener);
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#removeHyperlinkListener(net.sf.jasperreports.view.JRHyperlinkListener)
	 */
	public void removeHyperlinkListener(JRHyperlinkListener listener) {
		if (hyperlinkListeners != null)
			hyperlinkListeners.remove(listener);
	}

	/**
	 * @see com.jasperassistant.designer.viewer.IReportViewer#getHyperlinkListeners()
	 */
	public JRHyperlinkListener[] getHyperlinkListeners() {
		return hyperlinkListeners == null ? new JRHyperlinkListener[0] : (JRHyperlinkListener[]) hyperlinkListeners
				.toArray(new JRHyperlinkListener[hyperlinkListeners.size()]);
	}
}
