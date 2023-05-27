package com.unicyb.energytaxi.services.other;

import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.UserSendDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WSService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyFrontend(final String message){
        MyMessage responseMyMessage = new MyMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", responseMyMessage);
    }

    public void notifyDriver(final UserSendDate message){
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getDriverId()), "/driver", message);
    }

    public void notifyOrderAccept(final ResponseMessage responseMessage){
        simpMessagingTemplate.convertAndSendToUser(responseMessage.getUserId(), "/order-accept", responseMessage);
    }

    public void notifyOrderMessage(final UserSendDate userSendDate){
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(userSendDate.getUserId()), "/order-message", userSendDate);
    }
}
