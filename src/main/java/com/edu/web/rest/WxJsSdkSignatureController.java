package com.edu.web.rest;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxJsSdkSignatureController {
    @Autowired
    private WxMpService wxService;

    public static final String PATH = "/api/v1/JsApiSignature";

    @PostMapping(PATH)
    public WxJsapiSignature createSignature(@RequestBody SignatureRequestBody body) throws WxErrorException {
        return wxService.createJsapiSignature(body.getUrl());
    }

    public static class SignatureRequestBody {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
