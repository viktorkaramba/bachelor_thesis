package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.UserSendDate;
import com.unicyb.energytaxi.services.other.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSController {

    @Autowired
    private WSService wsService;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final MyMessage myMessage){
        wsService.notifyFrontend(myMessage.getContent());
    }

    @PostMapping("/send-driver-message")
    public void sendPrivateMessage(@RequestBody final UserSendDate message){
        wsService.notifyDriver(message);
    }
}
