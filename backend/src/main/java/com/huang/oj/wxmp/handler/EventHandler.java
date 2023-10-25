package com.huang.oj.wxmp.handler;

import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 事件处理器
 *
 
 
 **/
@Component
public class EventHandler/* implements WxMpMessageHandler*/ {

    /*@Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService,
            WxSessionManager wxSessionManager) throws WxErrorException {
        final String content = "您点击了菜单";
        // 调用接口，返回验证码
        return WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }*/
}
