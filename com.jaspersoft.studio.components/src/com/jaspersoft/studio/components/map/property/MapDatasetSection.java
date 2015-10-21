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
package com.jaspersoft.studio.components.map.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.StandardItem;
import net.sf.jasperreports.components.items.StandardItemData;
import net.sf.jasperreports.components.items.StandardItemProperty;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.BasicMapInfoData;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.MMap;
import com.jaspersoft.studio.model.util.ItemPropertyUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.ExpressionInterpreter;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.widgets.map.core.LatLng;
import com.jaspersoft.studio.widgets.map.core.MapType;
import com.jaspersoft.studio.widgets.map.core.Marker;
import com.jaspersoft.studio.widgets.map.ui.MarkersPickupDialog;

public class MapDatasetSection extends AbstractSection {

	@Override
	public void createControls(final Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		FormText mapPickSuggestion = new FormText(parent, SWT.NONE);
		mapPickSuggestion.setText(Messages.MarkerPage_0, true, false);
		mapPickSuggestion.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_CENTER));
		mapPickSuggestion.setWhitespaceNormalized(true);
		mapPickSuggestion.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				MMap mmap = (MMap) getElement();
				JasperDesign jd = mmap.getJasperDesign();
				JasperReportsConfiguration jConf = mmap
						.getJasperConfiguration();
				MarkersPickupDialog d = new MarkersPickupDialog(UIUtils
						.getShell()) {
					@Override
					protected void configureShell(Shell newShell) {
						super.configureShell(newShell);
						UIUtils.resizeAndCenterShell(newShell, 800, 600);
					}
				};
				BasicMapInfoData mapInfo = mmap.getBasicMapInformation();
				if (mapInfo.getLatitude() != null
						&& mapInfo.getLongitude() != null)
					d.setMapCenter(new LatLng(mapInfo.getLatitude(), mapInfo
							.getLongitude(), true));
				if (mapInfo.getAddress() != null)
					d.setAddress(mapInfo.getAddress());
				if (mapInfo.getMapType() != null)
					d.setMapType(MapType.fromStringID(mapInfo.getMapType()
							.getName()));
				if (mapInfo.getZoom() != 0)
					d.setZoomLevel(mapInfo.getZoom());

				Map<Marker, StandardItem> map = new HashMap<Marker, StandardItem>();
				List<ItemData> oldMarkers = (List<ItemData>) mmap
						.getPropertyValue(StandardMapComponent.PROPERTY_MARKER_DATA_LIST);
				List<ItemData> newMarkers = new ArrayList<ItemData>();
				if (oldMarkers != null) {
					for (ItemData id : oldMarkers) {
						id = (ItemData) id.clone();
						newMarkers.add(id);

						JRDesignDataset dataset = null;
						if (id != null && id.getDataset() != null)
							dataset = ModelUtils.getDesignDatasetForDatasetRun(
									jd, id.getDataset().getDatasetRun());
						if (dataset == null)
							dataset = ModelUtils.getDataset(mmap);
						if (dataset == null)
							dataset = (JRDesignDataset) jd.getMainDataset();

						ExpressionInterpreter expIntr = ExpressionUtil
								.getCachedInterpreter(dataset, jd, jConf);

						for (Item it : id.getItems()) {
							StandardItemProperty ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_latitude);
							if (ip == null)
								continue;
							Double lat = ItemPropertyUtil
									.getItemPropertyDouble(ip, expIntr);
							if (lat == null)
								continue;

							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_longitude);
							if (ip == null)
								continue;
							Double lon = ItemPropertyUtil
									.getItemPropertyDouble(ip, expIntr);
							if (lon == null)
								continue;
							Marker m = new Marker(new LatLng(lat, lon));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_flat);
							if (ip != null)
								m.setFlat(ItemPropertyUtil
										.getItemPropertyBoolean(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_clickable);
							if (ip != null)
								m.setClickable(ItemPropertyUtil
										.getItemPropertyBoolean(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_draggable);
							if (ip != null)
								m.setDraggable(ItemPropertyUtil
										.getItemPropertyBoolean(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil
									.getProperty(it.getProperties(),
											MapComponent.ITEM_PROPERTY_visible);
							if (ip != null)
								m.setVisible(ItemPropertyUtil
										.getItemPropertyBoolean(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_optimized);
							if (ip != null)
								m.getOptions().setOptimized(
										ItemPropertyUtil
												.getItemPropertyBoolean(ip,
														expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_raiseOnDrag);
							if (ip != null)
								m.getOptions().setRaiseOnDrag(
										ItemPropertyUtil
												.getItemPropertyBoolean(ip,
														expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_cursor);
							if (ip != null)
								m.setCursor(ItemPropertyUtil
										.getItemPropertyString(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_title);
							if (ip != null)
								m.setTitle(ItemPropertyUtil
										.getItemPropertyString(ip, expIntr));
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									it.getProperties(),
									MapComponent.ITEM_PROPERTY_MARKER_zIndex);
							if (ip != null)
								m.setZIndex(ItemPropertyUtil
										.getItemPropertyInteger(ip, expIntr));

							map.put(m, (StandardItem) it);
							d.getMarkersList().add(m);
						}
					}
				}
				if (d.open() == Window.OK) {
					List<Marker> markersList = d.getMarkersList();
					StandardItemData sid = null;
					for (Marker m : markersList) {
						StandardItem si = map.get(m);
						if (si != null) {
							StandardItemProperty ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									si.getProperties(),
									MapComponent.ITEM_PROPERTY_latitude);
							if (ip.getValueExpression() != null)
								ip.setValueExpression(new JRDesignExpression(m
										.getPosition().getLat().toString()));
							else
								ip.setValue(m.getPosition().getLat().toString());
							ip = (StandardItemProperty) ItemPropertyUtil.getProperty(
									si.getProperties(),
									MapComponent.ITEM_PROPERTY_longitude);
							if (ip.getValueExpression() != null)
								ip.setValueExpression(new JRDesignExpression(m
										.getPosition().getLng().toString()));
							else
								ip.setValue(m.getPosition().getLng().toString());
						} else {
							// will add it to the last itemdata, so we append
							// markers
							if (sid == null) {
								if (newMarkers.isEmpty()) {
									sid = new StandardItemData();
									newMarkers.add(sid);
								} else
									sid = (StandardItemData) newMarkers
											.get(newMarkers.size() - 1);
							}
							si = new StandardItem();
							si.addItemProperty(new StandardItemProperty(
									MapComponent.ITEM_PROPERTY_latitude, m
											.getPosition().getLat()
											.floatValue()
											+ "f", null)); //$NON-NLS-1$ //$NON-NLS-2$
							si.addItemProperty(new StandardItemProperty(
									MapComponent.ITEM_PROPERTY_longitude, m
											.getPosition().getLng()
											.floatValue()
											+ "f", null)); //$NON-NLS-1$ //$NON-NLS-2$
							sid.addItem(si);
						}
					}
					changeProperty(
							StandardMapComponent.PROPERTY_MARKER_DATA_LIST,
							newMarkers);
				}
			}
		});

		createWidget4Property(parent,
				StandardMapComponent.PROPERTY_MARKER_DATA_LIST, false);
	}
}