package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DriverResume;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseMessageFromString;

public class DriverRegistrationActivity extends AppCompatActivity {
    private EditText driverUserNameEditText;
    private EditText driverPasswordEditText;
    private EditText driverFirstNameEditText;
    private EditText driverSurNameEditText;
    private EditText driverPatronymicEditText;
    private EditText driverTelephoneNumberEditText;
    private EditText driverExperienceEditText;
    private Button sendButton;
    private Button cancelButton;
    private StompSession stompSession;
    private DriverResume driverResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        initializeComponents();
    }

    private void initializeComponents(){
        driverUserNameEditText = findViewById(R.id.driverUserNameEditText);
        driverPasswordEditText = findViewById(R.id.driverPasswordDriverEditText);
        driverFirstNameEditText = findViewById(R.id.driverFirstName);
        driverSurNameEditText = findViewById(R.id.driverSurName);
        driverPatronymicEditText= findViewById(R.id.driverPatronymic);
        driverTelephoneNumberEditText = findViewById(R.id.driverTelephoneNumber);
        driverExperienceEditText = findViewById(R.id.driverExperience);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> sendResume());
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> goMain());
    }

    private void goMain() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }

    public void sendResume(){
        String driverUserName = String.valueOf(this.driverUserNameEditText.getText());
        String driverPassword = String.valueOf(this.driverPasswordEditText.getText());
        String driverFirstName = String.valueOf(this.driverFirstNameEditText.getText());
        String driverSurName = String.valueOf(this.driverSurNameEditText.getText());
        String driverPatronymic = String.valueOf(this.driverPatronymicEditText.getText());
        String driverTelephone = String.valueOf(this.driverTelephoneNumberEditText.getText());
        float driverExperience = Float.parseFloat(String.valueOf(this.driverExperienceEditText.getText()));
        new Thread(() -> {
            try {
                Log.d("Diver Registration", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                driverResume = new DriverResume(driverUserName, driverPassword, driverFirstName, driverSurName,
                        driverPatronymic, driverTelephone, driverExperience);
                subscribeAuthorization(stompSession);
                sendDriverRegistryMessage(stompSession, driverResume);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void subscribeAuthorization(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + driverResume.getDriverUserName() + "/driver-registration", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                String response = new String((byte[]) o);
                Message message = new Message();
                try {
                    message.setContent(parseMessageFromString(response).getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("Diver Registration Received(/user/driver-registration)", message.getContent());
                getResponse(message);
            }
        });
    }

    public void sendDriverRegistryMessage(StompSession stompSession, DriverResume driverResume) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"driverUserName\" : \"").append(driverResume.getDriverUserName()).append("\",")
                .append("\"driverPassword\" : \"").append(driverResume.getDriverPassword()).append("\",")
                .append("\"driverFirstName\" : \"").append(driverResume.getDriverFirstName()).append("\",")
                .append("\"driverSurName\" : \"").append(driverResume.getDriverSurName()).append("\",")
                .append("\"driverPatronymic\" : \"").append(driverResume.getDriverPatronymic()).append("\",")
                .append("\"driverTelephoneNumber\" : \"").append(driverResume.getDriverTelephoneNumber()).append("\",")
                .append("\"driverExperience\" : \"").append(driverResume.getDriverExperience()).append("\" }");
        stompSession.send("/app/driver-registration-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void getResponse(Message message) {
        if(message.getContent().equals("Sorry, there are no car for your car experience")){
            Toast.makeText(DriverRegistrationActivity.this,
                    "Sorry, there are no car for your work experience, so your resume is rejected",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                Intent intent = new Intent(DriverRegistrationActivity.this, DriverMenuActivity.class);
                intent.putExtra("userId", message.getContent());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}