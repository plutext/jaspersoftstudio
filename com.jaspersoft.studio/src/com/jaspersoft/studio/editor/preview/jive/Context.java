/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.editor.preview.jive;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private static final Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();

	public static Map<String, Object> getContext(String key) {
		if (key != null)
			return map.get(key);
		return null;
	}

	public static void putContext(String key, Map<String, Object> value) {
		map.put(key, value);
	}

	public static void unsetContext(String key) {
		if (key != null)
			map.remove(key);
	}
}
