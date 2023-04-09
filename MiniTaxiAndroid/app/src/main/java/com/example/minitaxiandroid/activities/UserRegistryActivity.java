package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.ROLE;
import com.example.minitaxiandroid.entities.User;
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

public class UserRegistryActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button registry;
    private StompSession stompSession;
    private  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registry);
        userName = findViewById(R.id.userNameUserRegistryEditText);
        password = findViewById(R.id.passwordUserRegistryEditText);
        registry = findViewById(R.id.registryLoginButton);
        registry.setOnClickListener(view -> registry());
    }

    public void registry(){
        new Thread(() -> {
            try {
                Log.d("MakeOrder", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                subscribeAuthorization(stompSession);
                user = new User(-1, userName.getText().toString(), password.getText().toString(), ROLE.USER, 1);
                sendUserRegistryMessage(stompSession, user);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void subscribeAuthorization(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + user.getUserName() + "/registration", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                String response = new String((byte[]) o);
                Message message = null;
                try {
                    message = parseMessageFromString(response);
                    Log.d("Received ", message.getContent());
                    getResponse(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendUserRegistryMessage(StompSession stompSession, User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"userId\" : ").append(user.getUserId()).append(",")
                .append("\"userName\" : \"").append(user.getUserName()).append("\",")
                .append("\"password\" : \"").append(user.getPassword()).append("\",")
                .append("\"role\" : \"").append(user.getRole()).append("\" }");
        stompSession.send("/app/user-registration", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void getResponse(Message message) {
        if(message.getContent().equals("User with this username already exist")){
            Toast.makeText(UserRegistryActivity.this,
                    "User with this username already exist, please input another one",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                Intent intent = new Intent(UserRegistryActivity.this, UserOrderHistoryActivity.class);
                intent.putExtra("userId", message.getContent());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}