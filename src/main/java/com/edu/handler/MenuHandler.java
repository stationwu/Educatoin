package com.edu.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (WxConsts.BUTTON_CLICK.equals(wxMessage.getEvent())) {
			
			if (wxMessage.getEventKey().equals("MENU_ITEM_2001")) {
				//String redirectUrl = weixinService.oauth2buildAuthorizationUrl(myUrl, WxConsts.OAUTH2_SCOPE_BASE, null);
				return null;
			} else {
				String msg = String.format("type:%s, event:%s, key:%s, fromUser: %s", 
						wxMessage.getMsgType(),
						wxMessage.getEvent(), 
						wxMessage.getEventKey(), 
						wxMessage.getFromUser());
	
				return WxMpXmlOutMessage.TEXT().content(msg).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.build();
			}
		} else {
			return null;
		}

	}

}
