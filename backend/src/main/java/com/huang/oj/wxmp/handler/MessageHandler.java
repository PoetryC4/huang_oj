package com.huang.oj.wxmp.handler;

import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 消息处理器
 *
 
 
 **/
@Component
public class MessageHandler /*implements WxMpMessageHandler*/ {

    /*@Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
            WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String content = "我是复读机：" + wxMpXmlMessage.getContent();
        return WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }*/
}
