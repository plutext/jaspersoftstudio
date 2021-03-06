/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.editor.preview.jive.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRLoader;

public class SResourceServlet extends HttpServlet {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String resource = "webapp" + request.getServletPath() + request.getPathInfo();
		try {
			byte[] bytes = JRLoader.loadBytesFromResource(resource);

			response.setContentType(getServletContext().getMimeType(resource));

			// Set to expire far in the past.
			// response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
			// // Set standard HTTP/1.1 no-cache headers.
			// response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			// // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
			// response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			// // Set standard HTTP/1.0 no-cache header.
			// response.setHeader("Pragma", "no-cache");

			response.getOutputStream().write(bytes);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
