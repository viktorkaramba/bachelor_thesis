package com.example.minitaxiandroid.services;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.activities.MainActivity;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.example.minitaxiandroid.mythreads.DriverSendLocationThread;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.google.android.gms.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseDriverInfoFromString;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfoService extends AppCompatActivity {

    private static List<DriverInfo> driverInfoList;
    private static StompSession stompSession;
    private String messageInfo;
    private Context context;
    private LatLng latLng;
    private static DriverSendLocationThread driverSendLocationThread = new DriverSendLocationThread(2000,
            "driverThread");

    public DriverInfoService(Context context, LatLng latLng){
        this.context = context;
        this.latLng = latLng;
        driverSendLocationThread.setLatLng(latLng);
    }

    public void getDriverInfo(){
        new Thread(() -> {
            try {
                Log.d("GetPriceByClass", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                sendUserIdMessage();
                subscribeUser();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void driverWaitRequest(){
        new Thread(() -> {
            try {
                Log.d("GetPriceByClass", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                driverSendLocationThread.setStompSession(stompSession);
                subscribeDriver();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendUserIdMessage(){
        stompSession.send("/app/users-request-drivers-info-message",
                ("{ \"userId\" : \"" + UserLoginInfoService.getProperty("userId") + "\" }").getBytes(StandardCharsets.UTF_8));
    }

    public void sendDriversInfoMessage(String userId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"userId\" : \"").append(userId).append("\",")
                .append("\"driverId\" : \"").append(UserLoginInfoService.getProperty("driverId")).append("\",")
                .append("\"latitude\" : \"").append(UserLoginInfoService.getProperty("driverId")).append("\",")
                .append("\"longitude\" : \"").append(UserLoginInfoService.getProperty("driverId")).append("\" }");
        stompSession.send("/app/driver-send-info-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void subscribeUser() {
        stompSession.subscribe("/topic/users-request-drivers-info", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                System.out.println("message 2:" + response);
                try {
                    add(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Subscribe");
    }

    public void add(String response) throws Exception {
        DriverInfo driverInfo = parseDriverInfoFromString(response);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.showInfo(driverInfo);
    }

    public void disconnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{ \"userId\" : \"").append(UserLoginInfoService.getProperty("userId")).append("\" }");
                stompSession.send("/app/disconnect-user", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            }
        }).start();
     }

    public void subscribeDriver() {
        stompSession.subscribe("/topic/users-request-drivers", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                System.out.println("message " + response);
                try {
                    driverSendInfo(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void driverSendInfo(String message) throws Exception {

        if(message.contains("userId")){
            driverSendLocationThread.start();
        }
        else{
            DriverInfo driverInfo = parseDriverInfoFromString(message);
            if(driverInfo.getDriverId() == 0){
                driverSendLocationThread.stop();
            }
        }
    }
}
