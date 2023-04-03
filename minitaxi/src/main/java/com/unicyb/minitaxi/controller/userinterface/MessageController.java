package com.unicyb.minitaxi.controller.userinterface;

import com.unicyb.minitaxi.entities.userinterfaceenteties.Message;
import com.unicyb.minitaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.minitaxi.entities.userinterfaceenteties.UserAddressesMessage;
import com.unicyb.minitaxi.entities.userinterfaceenteties.UserSendDate;
import com.unicyb.minitaxi.entities.usersinfo.DriverInfo;
import com.unicyb.minitaxi.entities.usersinfo.DriverSendInfoMessage;
import com.unicyb.minitaxi.services.WSService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

@Controller
public class MessageController {
    @Autowired
    private WSService wsService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;
    private int userCount = 0;

    public int getNumberOfSessions() {
        return simpUserRegistry.getUserCount();
    }

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

    @MessageMapping("/user-addresses-message")
    public UserAddressesMessage getUserAddressesMessage(@Payload final UserAddressesMessage userAddressesMessage)
            throws IOException, ParseException, InterruptedException {
        wsService.notifyUserAddressesMessage(userAddressesMessage);
        return userAddressesMessage;
    }

    @MessageMapping("/users-request-drivers-info-message")
    @SendTo("/topic/users-request-drivers")
    public String getUserAddressesMessage(@Payload final String userId) {
        System.out.println("userIdServer: " + userId);
        userCount++;
        System.out.println("userCount: " + userCount);
        return userId;
    }

    @MessageMapping("/disconnect-user")
    public String getDisconnectUserMessage(@Payload final String userId) {
        userCount--;
        return userId;
    }

    @MessageMapping("/driver-send-info-message")
//    @SendTo("/topic/users-request-drivers-info")
    public DriverInfo getDriverSendInfoMessage(@Payload final DriverSendInfoMessage driverSendInfoMessage) {
        System.out.println("driverIdServer: " + driverSendInfoMessage.getDriverId());
        System.out.println("userCount: " + userCount);
        return wsService.notifyDriverSendInfoMessage(driverSendInfoMessage, userCount);
    }
}
