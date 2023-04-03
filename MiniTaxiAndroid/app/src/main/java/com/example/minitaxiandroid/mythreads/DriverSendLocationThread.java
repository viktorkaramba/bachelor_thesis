package com.example.minitaxiandroid.mythreads;

import com.google.android.gms.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.messaging.simp.stomp.StompSession;

import java.nio.charset.StandardCharsets;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverSendLocationThread extends MyThread{

    private StompSession stompSession;
    private LatLng latLng;

    public DriverSendLocationThread(int sleepInterval, String name) {
        super(sleepInterval, name);
    }

    public void sendInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"driverId\" : \"").append("1").append("\",")
                .append("\"latitude\" : \"").append(latLng.latitude).append("\",")
                .append("\"longitude\" : \"").append(latLng.longitude).append("\" }");
        stompSession.send("/app/driver-send-info-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        getRunning().set(true);
        getStopped().set(false);
        while (getRunning().get()) {
            sendInfo();
            try {
                Thread.sleep(getInterval());
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(
                        "Thread was interrupted, Failed to complete operation");
            }
        }
        getStopped().set(true);
    }
}
