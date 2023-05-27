package com.example.energytaxiandroid.services;

import com.example.energytaxiandroid.entities.messages.ResponseMessage;
import com.example.energytaxiandroid.entities.messages.UserSendDate;
import org.json.JSONObject;

public class ObjectParserService {
    public static UserSendDate parseUserSendDateFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        return new UserSendDate(obj.getInt("driverId"), obj.getInt("userId"),obj.getString("customerName"),
                                obj.getString("addressCustomer"), obj.getString("addressDelivery"),
                                obj.getString("telephoneNumber"));
    }

    public static ResponseMessage parseResponseMessageFromString(String s) throws Exception{
        final JSONObject obj = new JSONObject(s);
        return new ResponseMessage(obj.getString("userId"), obj.getString("content"));
    }
}
