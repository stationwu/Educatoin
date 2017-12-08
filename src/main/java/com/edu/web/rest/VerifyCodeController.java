package com.edu.web.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    static final String PATH = "/verify";
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
    
    /**
     * 根据Id查询验证码，如果没有找到，返回code: -1, mobile: 0
     * @param id
     * @return
     */
    @GetMapping(path= PATH + "/{id}")
    public VerifyCode findOne(@PathVariable("id") Long id) {
        VerifyCode code = repo.findOneVerifyCodeById(id);
        if(null == code ) {
            return new VerifyCode("-1", "0");
        } 
        return code;
    }
    /**
     * 通过短信网关发送验证码，实体对象包含手机号和类型，
     *  R: 注册类型信息
     *  P：支付类型信息
     * @param smsContainer
     * @return
     */
    @PostMapping(path = PATH)
    public Long create(@RequestBody @Valid SmsContainer smsContainer) {
        String code = String.valueOf(System.nanoTime()).substring(7, 13);
        VerifyCode verifyCode = new VerifyCode(code, smsContainer.getMobile());
        VerifyCode savedVerifyCode = repo.save(verifyCode);

        if (savedVerifyCode != null) {
            String content = "";
            switch (smsContainer.getType()) {
                case "R"://register
                    content = "欢迎注册大树夏微信公众号，您的验证码为" + code + "，请不要告诉别人哦【大树夏】";
                    break;
                case "P"://payment
                    content = "您的支付验证码为" + code + "，请不要告诉别人哦【大树夏】";
                    break;
            }
            
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
