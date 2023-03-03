package com.unicyb.minitaxi.controller.userinterface;

import com.unicyb.minitaxi.entities.userinterfaceenteties.*;
import com.unicyb.minitaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {
    @Autowired
    private WSService wsService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message getMessage(final Message message) {
        return new Message(HtmlUtils.htmlEscape(message.getContent()));
    }


    @MessageMapping("/driver-message")
    public UserSendDate getPrivateMessage(@Payload final UserSendDate message) {
        wsService.notifyDriver(message);
        return message;
    }

    @MessageMapping("/order-accept-message")
    public ResponseMessage getOrderAcceptMessage(@Payload final ResponseMessage responseMessage) {
        wsService.notifyOrderAccept(responseMessage);
        return responseMessage;
    }

    @MessageMapping("/order-info-message")
    public UserSendDate getOrderMessage(@Payload final UserSendDate userSendDate) {
        wsService.notifyOrderMessage(userSendDate);
        return userSendDate;
    }


}
