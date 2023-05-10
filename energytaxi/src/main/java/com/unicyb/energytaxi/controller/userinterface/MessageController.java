package com.unicyb.energytaxi.controller.userinterface;

import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.UserSendDate;
import com.unicyb.energytaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    private WSService wsService;

    @MessageMapping("/myMessage")
    @SendTo("/topic/messages")
    public MyMessage getMessage(final MyMessage myMessage) {
        return new MyMessage(HtmlUtils.htmlEscape(myMessage.getContent()));
    }


    @MessageMapping("/driver-message")
    public UserSendDate getPrivateMessage(@Payload final UserSendDate message) {
        wsService.notifyDriver(message);
        return message;
    }

    @MessageMapping("/driver-accept-order-message")
    public ResponseMessage getOrderAcceptMessage(@Payload final ResponseMessage responseMessage) {
        System.out.println(responseMessage);
        wsService.notifyOrderAccept(responseMessage);
        return responseMessage;
    }

    @MessageMapping("/order-info-message")
    public UserSendDate getOrderMessage(@Payload final UserSendDate userSendDate) {
        System.out.println(userSendDate);
        wsService.notifyOrderMessage(userSendDate);
        return userSendDate;
    }
}
