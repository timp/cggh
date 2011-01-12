package org.cggh.malariagen.merger.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public class LogoutHandler implements
		org.springframework.security.web.authentication.logout.LogoutHandler {

	private Object userAccessCache;

	/**
	 * @return the userAccessCache
	 */
	public Object getUserAccessCache() {
		return userAccessCache;
	}

	/**
	 * @param userAccessCache
	 *            the userAccessCache to set
	 */
	public void setUserAccessCache(Object userAccessCache) {
		this.userAccessCache = userAccessCache;
	}

	@Override
	public void logout(HttpServletRequest req, HttpServletResponse res,
			Authentication arg2) {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		System.out.println("logging out via handler");
		request.getSession().invalidate();
		Cookie[] cookies = request.getCookies();
		int numCookies = cookies.length;
		for (int i = 0; i < numCookies; i++) {
			Cookie myCookie = cookies[i];
			Cookie delCookie = new Cookie(myCookie.getName(),
					myCookie.getValue());
			if (myCookie.getDomain() != null) {
				delCookie.setDomain(myCookie.getDomain());
			}
			if (myCookie.getPath() != null) {
				delCookie.setPath(myCookie.getPath());
			} else {
				delCookie.setPath(request.getContextPath());
			}
			delCookie.setMaxAge(0);
			response.addCookie(delCookie);
		}
	}

}
