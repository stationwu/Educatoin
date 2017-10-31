package com.edu.utils;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class WxUserOAuthHelper {
    private final static String SESSION_OPENID_KEY = "openCode";
    private final static String DUMMY_STATE = "1";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxMpService wxMpService;

    public String buildOAuth2RedirectURL(HttpServletRequest request, String controllerPath, String callbackPath) {
        String requestUrl = request.getRequestURL().toString();
        String hostAddress = URLUtil.getServiceURLBeforePath(requestUrl, controllerPath);
        String redirectUrl = hostAddress + callbackPath;
        String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_BASE, DUMMY_STATE);

        logger.debug(">>> Redirecting to " + authorizationUrl);

        return "redirect:" + authorizationUrl;
    }

    public String getOpenIdWhenOAuth2CalledBack(String authCode, HttpSession session) throws WxErrorException {
        String openId = null;

        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);
        if (openIdInSession != null) { // If we already have it, we don't need to get it again
            openId = (String)openIdInSession;
        } else {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode); // On the other hand, one authCode can only be used once
            openId = wxMpOAuth2AccessToken.getOpenId();
            session.setAttribute(SESSION_OPENID_KEY, openId);
        }

        return openId;
    }
}
