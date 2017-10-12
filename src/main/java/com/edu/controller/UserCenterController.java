package com.edu.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import com.edu.dao.StudentRepository;
import com.edu.domain.Student;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

@Controller
public class UserCenterController {

	@Autowired
	private WxMpService wxMpService;
	
	@Value("${app.host.address}")
	private String hostAddress;
	
	@Autowired
	private StudentRepository repository;
	
	private final String DUMMY_STATE = "123";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/user/center")
	public String userCenter() {
		String redirectUrl = hostAddress + "/user/oauth";
		String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_BASE, DUMMY_STATE);
		
		logger.debug(">>> Got request to access the User Center");
		logger.debug(">>> Redirecting to " + authorizationUrl);
		
		return "redirect:" + authorizationUrl;
	}
	
	@RequestMapping("/user/oauth")
	public String authorize(@RequestParam(value="code") String authCode, Model model) {
		String view = "error404";
		logger.debug(">>> Redirected back to validate authorization code");
		logger.debug(">>> The code is: " + authCode);
		
		try {
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode);
			
//			if(wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken)) {
//				logger.debug(">>> The code is valid");
			String openId = wxMpOAuth2AccessToken.getOpenId();
			
			logger.debug(">>> Your OPENID is: " + openId);
			if(repository.isStudentAlreadyRegistered(openId)) {
				logger.debug(">>> You've already registered");
				logger.debug(">>> Redirecting to user's home page");
				
				Student student = repository.findOneByOpenCode(openId);
				model.addAttribute("student", student);
				view = "user_info";
			} else {
				logger.debug(">>> You're not registered");
				logger.debug(">>> Redirecting to the signup page");
				
				Student student = new Student();
				student.setOpenCode(openId);
				
				model.addAttribute("student", student);
				view = "user_signup";
			}
//			} else {
//				logger.debug(">>> The code is invalid");
//				view = "error_500";
//			}
		} catch (WxErrorException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error(sw.toString());
			view = "error_500";
		}
		
		return view;
	}
}
