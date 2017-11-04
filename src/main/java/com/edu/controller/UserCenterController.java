package com.edu.controller;

import com.edu.dao.CustomerRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Student;
import com.edu.utils.WxUserOAuthHelper;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserCenterController {

	@Autowired
	private WxMpService wxMpService;

    @Autowired
    private WxUserOAuthHelper oauthHelper;
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	private final String DUMMY_STATE = "123";

	public final static String USER_CENTER_PATH = "/user/center";

	public final static String USER_CENTER_CALLBACK_PATH = "/user/center/cb";

	public final static String USER_SIGNUP_PATH = "/user/signup";

	public final static String SESSION_OPENID_KEY = "openCode";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(USER_CENTER_PATH)
	public String userCenter(HttpServletRequest request, HttpSession session, Model model) {
        logger.debug(">>> Got request to access the User Center");

        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, USER_CENTER_PATH, USER_CENTER_CALLBACK_PATH);
        } else {
            return gotoUserHomeOrSignUp((String) openIdInSession, model);
        }
	}
	
	@GetMapping(USER_CENTER_CALLBACK_PATH)
	public String authorize(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        logger.debug(">>> Redirected back to validate authorization code");
        logger.debug(">>> The code is: " + authCode);

        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return gotoUserHomeOrSignUp(openId, model);
	}

	private String gotoUserHomeOrSignUp(String openId, Model model) {
	    if (openId == null) {
	        return "error_500";
        }

        logger.debug(">>> Your OPENID is: " + openId);

	    String view = null;

        if(repository.isCustomerAlreadyRegistered(openId)) {
            logger.debug(">>> You've already registered");
            logger.debug(">>> Redirecting to user's home page");

            Customer customer = repository.findOneByOpenCode(openId);
            model.addAttribute("customer", customer);
            view = "user_info";
        } else {
            logger.debug(">>> You're not registered");
            logger.debug(">>> Redirecting to the signup page");

            Customer customer = new Customer();
            customer.setOpenCode(openId);

            model.addAttribute("customer", customer);
            view = "user_signup";
        }

        return view;
    }
	
	@PostMapping(USER_SIGNUP_PATH)
	public String signup(@ModelAttribute @Valid Customer customer, BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			return "user_signup";
		}

        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);
        if (openIdInSession == null) {
            return "error_500"; // It's a must to have the openId in session!
        }

        String openId = (String)openIdInSession;
        customer.setOpenCode(openId);
		
		logger.debug(">>> Signing up: " + customer.toString());

        customer = repository.save(customer);
		
		logger.debug(">>> Signed up. Id: " + customer.getId());
		
		return "user_info";
	}
	
	@PostMapping("user/search")
	@ResponseBody
	public List<Student> searchStudent(@RequestParam(value="keyword") String keyword, HttpSession session) {
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);
		List<Student> students = studentRepository.search(keyword);
		return students;
	}
	
	@GetMapping("user/search")
	public String getSearchPage(HttpSession session) {
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);
		return "user_search";
	}
	
	@GetMapping("user/session")
	@ResponseBody
	public String createSession(HttpSession session) {
		String openId = "123456";
        session.setAttribute(SESSION_OPENID_KEY, openId);
		
		return "true";
	}
	
}