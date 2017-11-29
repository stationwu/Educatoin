package com.edu.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.edu.utils.Constant;
import com.edu.utils.WxUserOAuthHelper;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private WxUserOAuthHelper oauthHelper;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private CustomerRepository repository;

    private final static String DUMMY_STATE = "1";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
//        Object openIdInSession = request.getSession().getAttribute(
//                Constant.SESSION_OPENID_KEY);
//        String authCode = request.getParameter("code");
//        String requestUrl = request.getRequestURL().toString();
//        if (null == openIdInSession && null == authCode) {
//            String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(
//                    requestUrl, WxConsts.OAUTH2_SCOPE_BASE, DUMMY_STATE);
//            response.sendRedirect(authorizationUrl);
//            return false;
//        } else if (null != authCode) {
//            String openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode,
//                    request.getSession());
//            if (!repository.isCustomerAlreadyRegistered(openId)) {
//                request.getRequestDispatcher(Constant.USER_CENTER_PATH).forward(
//                        request, response);
//                return false;
//            } else {
//                response.sendRedirect(request.getRequestURI());
//                return false;
//            }
//        } else {
//            String openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode,
//                    request.getSession());
//            if (!repository.isCustomerAlreadyRegistered(openId)) {
//                request.getRequestDispatcher(Constant.USER_CENTER_PATH).forward(
//                        request, response);
//                return false;
//            }
//        }

        request.getSession().setAttribute(Constant.SESSION_OPENID_KEY, "oU_hZ0ajDq7jodE3OHnlW0HYrubE");

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
}
