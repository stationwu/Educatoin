package com.edu.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@Value("${app.host.address}")
	private String hostAddress;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(path = "/wechat/redirect", method = RequestMethod.GET)
	public String wxRedirect() {
		String oauthUrl = hostAddress + "/wechat/oauth";
		String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(oauthUrl, WxConsts.OAUTH2_SCOPE_BASE, null);
		redirectUrl = "redirect:" + redirectUrl;
		
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("Redirecting to " + redirectUrl);
		logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		
		return redirectUrl;
	}
	
	@RequestMapping(path = "/wechat/oauth", method = RequestMethod.GET)
	public ModelAndView oauth(HttpServletRequest request, ModelAndView view) {
		String authCode = request.getParameter("code");
		
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("Trying to validate code: " + authCode);
		logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		
		try {
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode);
			
			Boolean valid = wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
			
			String openId = wxMpOAuth2AccessToken.getOpenId();
			
			// You cannot get UserInfo because it's BASE scope
			// WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug(String.format("OpenId: %s, Valid: %s", openId, valid.toString()));
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			view.setViewName("/Student/OpenCode/" + openId);
		} catch (WxErrorException e) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.error(e.toString());
			logger.error("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		
		return view;
	}
	
	@RequestMapping("/wechat/test/redirect")
	public String testRedirect() {
		return "redirect:/wechat/test/static?code=123";
	}
	
	@RequestMapping(path = "/wechat/test/static", method = RequestMethod.GET)
	public String staticContent(@RequestParam(value="code", required=false, defaultValue="UNDEFINED") String code, Model model) {
		model.addAttribute("code", code);
		return "test_view";
	}
	
}
