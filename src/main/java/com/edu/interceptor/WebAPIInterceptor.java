package com.edu.interceptor;

import com.edu.dao.CustomerRepository;
import com.edu.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebAPIInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private CustomerRepository repository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        String openId = (String)session.getAttribute(Constant.SESSION_OPENID_KEY);

        if (openId == null /*and is not admin*/) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        if (!repository.isCustomerAlreadyRegistered(openId) /*or is admin*/) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        return true;
    }
}
