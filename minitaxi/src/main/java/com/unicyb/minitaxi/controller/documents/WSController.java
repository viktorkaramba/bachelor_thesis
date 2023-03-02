package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.entities.userinterfaceenteties.Message;
import com.unicyb.minitaxi.entities.userinterfaceenteties.UserSendDate;
import com.unicyb.minitaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSController {

    @Autowired
    private WSService wsService;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message){
        wsService.notifyFrontend(message.getContent());
    }

    @PostMapping("/send-driver-message")
    public void sendPrivateMessage(@RequestBody final UserSendDate message){
        wsService.notifyDriver(message);
    }
}
