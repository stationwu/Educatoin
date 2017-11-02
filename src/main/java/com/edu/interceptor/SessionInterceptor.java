package com.edu.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.edu.dao.StudentRepository;
import com.edu.domain.Student;
import com.edu.utils.Constant;
import com.edu.utils.WxUserOAuthHelper;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

public class SessionInterceptor extends HandlerInterceptorAdapter{
	private static final Logger logger = LoggerFactory
			.getLogger(SessionInterceptor.class);
	
	@Autowired
    private WxUserOAuthHelper oauthHelper;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private StudentRepository repository;
	
	private final static String DUMMY_STATE = "1";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Object openIdInSession = request.getSession().getAttribute(Constant.SESSION_OPENID_KEY);
		String authCode = request.getParameter("code");
		String requestUrl = request.getRequestURL().toString();
		if (null == openIdInSession && null == authCode) {  
			String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(requestUrl, WxConsts.OAUTH2_SCOPE_BASE, DUMMY_STATE);
			response.sendRedirect(authorizationUrl); 
		}else if (null != authCode) {
			String openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, request.getSession());
			if(!repository.isStudentAlreadyRegistered(openId)){
				response.sendRedirect(Constant.USER_SIGNUP_PATH); 
			}else{
				response.sendRedirect(request.getRequestURI());
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
