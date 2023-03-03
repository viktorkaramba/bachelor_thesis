package com.example.minitaxiandroid.services;

import com.example.minitaxiandroid.entities.ROLE;
import com.example.minitaxiandroid.entities.User;
import com.example.minitaxiandroid.entities.login.LoginResponseMessage;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.SendOrder;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import org.json.JSONObject;

public class ObjectParserService {
    public static UserSendDate parseUserSendDateFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        return new UserSendDate(obj.getInt("driverId"), obj.getInt("userId"),obj.getString("customerName"),
                                obj.getString("addressCustomer"), obj.getString("addressDelivery"),
                                obj.getString("telephoneNumber"));
    }

    public static SendOrder parseSendOrderFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        return new SendOrder(obj.getInt("driverId"), obj.getInt("userId"), obj.getString("customerName"),
                obj.getString("addressCustomer"), obj.getString("addressDelivery"),
                obj.getString("telephoneNumber"), Float.valueOf(obj.getString("price")),
                Float.valueOf(obj.getString("rating")));
    }

    public static User parseUserFromString(String s) throws Exception{
        final JSONObject obj = new JSONObject(s);
        return new User(obj.getInt("userId"), obj.getString("userName"), obj.getString("password"),
                ROLE.valueOf(obj.getString("role")), obj.getInt("rankId"));
    }

    public static LoginResponseMessage parseLoginMessageFromString(String s) throws Exception{
        final JSONObject obj = new JSONObject(s);
        return new LoginResponseMessage(obj.getString("userId"), ROLE.valueOf(obj.getString("role")));
    }

    public static Message parseMessageFromString(String s) throws Exception{
        final JSONObject obj = new JSONObject(s);
        return new Message(obj.getString("content"));
    }

    public static ResponseMessage parseResponseMessageFromString(String s) throws Exception{
        final JSONObject obj = new JSONObject(s);
        return new ResponseMessage(obj.getString("userId"), obj.getString("content"));
    }
}
