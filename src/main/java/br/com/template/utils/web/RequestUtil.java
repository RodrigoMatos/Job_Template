package br.com.template.utils.web;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {

	private RequestUtil() {
	}

	public static String getIpCurrentServer(HttpServletRequest request) {
		return request.getLocalAddr();
	}

	public static Integer getPortCurrentServer(HttpServletRequest request) {
		return request.getLocalPort();
	}

	public static String getIpFromRequest(HttpServletRequest request) {
		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}

	public static String getBrowserDetailsFromRequest(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	public static boolean isInternalAccess(HttpServletRequest request) {
		if (getIpFromRequest(request).startsWith("10.") || getIpFromRequest(request).startsWith("172.")
				|| getIpFromRequest(request).startsWith("192.168.")) {
			return true;
		}
		return false;
	}

	public static String getURIWithQueryParametersAndFacesRedirect(HttpServletRequest request) {
		return getURIWithoutContext(request).concat("?faces-redirect=true")
				.concat(request.getQueryString() != null ? "&".concat(request.getQueryString()) : "");
	}

	public static String getURIWithQueryParameters(HttpServletRequest request) {
		return request.getRequestURI()
				.concat(request.getQueryString() != null ? "?".concat(request.getQueryString()) : "");
	}

	public static String getURIWithoutContext(HttpServletRequest request) {
		return request.getRequestURI().replaceAll(request.getContextPath(), "");
	}

	public static String getURIWithoutContextAndQueryParameters(HttpServletRequest request) {
		String uri = getURIWithoutContext(request);
		if (uri.contains("?")) {
			uri = uri.replaceAll(request.getQueryString(), "");
		}
		return uri;
	}

	public static Object getAttributeFromSessionOrRequest(String attributeName, HttpServletRequest request) {
		Object object = request.getSession().getAttribute(attributeName);
		if (object == null) {
			object = request.getAttribute(attributeName);
		}
		return object;
	}

	public static Integer getSessionTimeoutInMinutes(HttpServletRequest request) {
		return request.getSession().getMaxInactiveInterval() / 60;
	}

	public static String getRequestParameter(HttpServletRequest request, String value) {
		return request.getParameter(value);
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String facesRequest = request.getHeader("Faces-Request");
		return (facesRequest != null && facesRequest.equals("partial/ajax"))
				|| "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

}
