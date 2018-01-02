package com.edu.interceptor;

import com.edu.dao.CustomerRepository;
import com.edu.utils.Constant;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private CustomerRepository repository;

    private final static String DUMMY_STATE = "1";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Object openIdInSession = request.getSession().getAttribute(
                Constant.SESSION_OPENID_KEY);
        String authCode = request.getParameter("code");
        String requestUrl = request.getRequestURL().toString();
        if (null == openIdInSession && null == authCode) {
            String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(
                    requestUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, DUMMY_STATE);
            response.sendRedirect(authorizationUrl);
            return false;
        } else if (null != authCode) {
            String openId = getOpenIdWhenOAuth2CalledBack(authCode, request.getSession());
            if (!repository.isCustomerAlreadyRegistered(openId)) {
                request.getRequestDispatcher(Constant.USER_CENTER_PATH).forward(
                        request, response);
                return false;
            } else {
                response.sendRedirect(request.getRequestURI());
                return false;
            }
        } else {
            String openId = getOpenIdWhenOAuth2CalledBack(authCode, request.getSession());
            if (!repository.isCustomerAlreadyRegistered(openId)) {
                request.getRequestDispatcher(Constant.USER_CENTER_PATH).forward(
                        request, response);
                return false;
            }
        }

//        request.getSession().setAttribute(Constant.SESSION_OPENID_KEY, "oU_hZ0ajDq7jodE3OHnlW0HYrubE");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
    }

    private String getOpenIdWhenOAuth2CalledBack(String authCode, HttpSession session) throws WxErrorException {
        String openId = null;

        Object openIdInSession = session.getAttribute(Constant.SESSION_OPENID_KEY);
        if (openIdInSession != null) { // If we already have it, we don't need to get it again
            openId = (String)openIdInSession;
        } else {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode); // On the other hand, one authCode can only be used once
            openId = wxMpOAuth2AccessToken.getOpenId();
            session.setAttribute(Constant.SESSION_OPENID_KEY, openId);
        }

        return openId;
    }
}
