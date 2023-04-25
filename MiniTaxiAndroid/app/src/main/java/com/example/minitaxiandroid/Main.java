package com.example.minitaxiandroid;

import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        StompSession stompSession;
        WebSocketClient userClient = new WebSocketClient();
        ListenableFuture<StompSession> f = userClient.connect();
        stompSession = f.get();
        subscribeUser(stompSession, 1);
        while(true){
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String userName = myObj.nextLine();
            System.out.println(userName);
            if(Objects.equals(userName, "1")){
                System.out.println("send");
                send(stompSession);
            }
            if(Objects.equals(userName, "2")){
                System.out.println("disconnect");
                stompSession.disconnect();
                System.out.println(stompSession.isConnected());
            }
        }
    }

    public static void subscribeUser(StompSession stompSession, int userID) {

        stompSession.subscribe("/user/" + userID + "/driver", new StompFrameHandler() {
            @NotNull
            @Override
            public Type getPayloadType(@NotNull StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(@NotNull StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                System.out.println(response);
            }
        });
    }

    private static void send(StompSession stompSession) {
        UserSendDate userSendDate = new UserSendDate(1,
                2, "Nazareeer",
                "addressCustomer", "addressDelivery", "+380961533467");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"driverId\" : ").append(userSendDate.getDriverId()).append(",")
                .append("\"userId\" : \"").append(userSendDate.getUserId()).append("\",")
                .append("\"customerName\" : \"").append(userSendDate.getCustomerName()).append("\",")
                .append("\"addressCustomer\" : \"").append(userSendDate.getAddressCustomer()).append("\",")
                .append("\"addressDelivery\" : \"").append(userSendDate.getAddressDelivery()).append("\",")
                .append("\"telephoneNumber\" : \"").append(userSendDate.getTelephoneNumber()).append("\" }");
        stompSession.send("/app/driver-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
