package com.weitouch.dm;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.weitouch.dm.pojo.Users;

public class AuthenticationFilter implements Filter {
	private static final Logger log = LoggerFactory
			.getLogger(AuthenticationFilter.class);

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		log.info("================Requested Path : {}", req.getContextPath()
				+ req.getServletPath());		
		

		String servletPath = req.getServletPath();
		if (servletPath.indexOf("/home") >= 0
				|| servletPath.indexOf("/enterLogin") >= 0
				|| servletPath.indexOf("/enterRegister") >= 0
				|| servletPath.indexOf("/enterForget") >= 0
				|| servletPath.indexOf("/check") >= 0
				|| servletPath.indexOf("/register") >= 0
				|| servletPath.indexOf("/forget") >= 0
				|| servletPath.indexOf("/login") >= 0
				|| servletPath.indexOf("/onPaymentReturn.do") >= 0
				|| servletPath.indexOf("/onPaymentNotify.do") >= 0
				|| servletPath.indexOf("/onRefundNotify.do") >= 0
				|| servletPath.indexOf("/whichUser.do") >= 0
				|| servletPath.indexOf("/upload.do") >= 0) {
			chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
			return;
		}

		HttpSession session = req.getSession(false);
		if (session == null
				|| session.getAttribute(Constants.LOGIN_USER) == null) {
			log.debug("Not logging yet. Redirecting to login page...");
			String url = ((HttpServletRequest) request).getRequestURL()
					.toString();
			String queryString = ((HttpServletRequest) request)
					.getQueryString();
			queryString = StringUtils.isEmpty(queryString) ? "" : "?"
					+ queryString;
			// session can be null, so get it again so that a new one can be
			// created when it is null
			session = req.getSession();
			session.setAttribute("openURL", url + queryString);
			resp.sendRedirect(req.getContextPath() + "/enterLogin.do");
			return;
		}
		
		chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
	}

	@Override
	public void destroy() {
		// Do nothing
	}

}
