package com.unicyb.minitaxi.controller.userinterface;

import com.unicyb.minitaxi.entities.userinterfaceenteties.DriverResume;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Message;
import com.unicyb.minitaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class DriverResumeController {

    @Autowired
    private WSService wsService;

    @MessageMapping("/driver-registration-message")
    public Message getMessage(@Payload final DriverResume driverResume) {
        return wsService.notifyDriverRegistryMessage(driverResume);
    }
}
