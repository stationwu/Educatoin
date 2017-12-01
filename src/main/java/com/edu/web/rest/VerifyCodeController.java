package com.edu.web.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.edu.dao.VerifyCodeRepository;
import com.edu.domain.VerifyCode;
import com.edu.domain.dto.SmsContainer;
import com.edu.domain.dto.SmsReturnValue;

@RestController
public class VerifyCodeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String PATH = "/verify";
    static final String SMS_URL = "https://dx.ipyy.net/smsJson.aspx?";
    @Autowired  
    private RestTemplate restTemplate;
    private VerifyCodeRepository repo;
    
    @Bean
    public RestTemplate buildRestTemplate(){
        return new RestTemplate();
    }
    @Autowired
    public VerifyCodeController(VerifyCodeRepository verifyCodeRepository) {
        this.repo = verifyCodeRepository;
    }
    
    @PostMapping(path = PATH)
    public Long create(@RequestBody @Valid SmsContainer smsContainer) {
        String code = String.valueOf(System.nanoTime()).substring(7, 13);
        VerifyCode verifyCode = new VerifyCode(code, smsContainer.getMobile());
        VerifyCode savedVerifyCode = repo.save(verifyCode);

        if (savedVerifyCode != null) {
            String content = "欢迎注册大树夏微信公众号，您的验证码为" + code + "，【大树夏】";
            String sUrl = SMS_URL
                    + "action=send&userid=&account=AG00126&password=AG0012689&mobile="
                    + smsContainer.getMobile() + "&content=" + content
                    + "&sendTime=&extno=";
            ResponseEntity<SmsReturnValue> responseEntity = restTemplate
                    .postForEntity(sUrl, null, SmsReturnValue.class);
            SmsReturnValue returnValue = responseEntity.getBody();
            logger.info("send msg return value {}", returnValue);
        }

        return savedVerifyCode.getId();
    }
}
