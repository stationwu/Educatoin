package com.edu.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Controller
public class WxRedirectController {
	
	@Autowired
	private WxMpService wxMpService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String oauthUrl = "http://101.132.129.129/Education-0.0.1-SNAPSHOT/wechat/oauth";

	@RequestMapping(path = "/wechat/redirect", method = RequestMethod.GET)
	public String wxRedirect() {
		String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(oauthUrl, WxConsts.OAUTH2_SCOPE_BASE, null);
		redirectUrl = "redirect:" + redirectUrl;
		
		logger.debug(redirectUrl);
		
		return redirectUrl;
	}
	
	@RequestMapping(path = "/wechat/oauth", method = RequestMethod.GET)
	public ModelAndView oauth(HttpServletRequest request, ModelAndView view) {
		String authCode = request.getParameter("code");
		
		try {
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode);
			
			Boolean valid = wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
			
			String openId = wxMpOAuth2AccessToken.getOpenId();
			
			// You cannot get UserInfo because it's BASE scope
			// WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			
			String msg = String.format("OpenId: %s, Valid: %s", openId, valid.toString());
			logger.debug(msg);
			
			view.setViewName("/Student/OpenCode/" + openId);
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
		}
		
		return view;
	}
	
}
