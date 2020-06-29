package util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

	/**
	 * get HTTP Request context path. ex. URL="http://xxx:yyy/zzz/.../..." path="zzz/.../..."
	 * @param request
	 * @return
	 */
	public static String getPathFromRequestURL(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
		String[] requestStrings = requestURL.split("/", 4); // URL="http://xxx:yyy/zzz/.../..." 1="http:" 2="" 3="xxx:yyy" 4="zzz/.../..."
		return requestStrings[3];
	}
}
