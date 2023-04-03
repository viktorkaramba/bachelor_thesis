package com.example.minitaxiandroid.services;

import com.example.minitaxiandroid.entities.ROLE;
import com.example.minitaxiandroid.entities.User;
import com.example.minitaxiandroid.entities.login.LoginResponseMessage;
import com.example.minitaxiandroid.entities.messages.*;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObjectParserService {
    public static UserSendDate parseUserSendDateFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        return new UserSendDate(obj.getInt("driverId"), obj.getInt("userId"),obj.getString("customerName"),
                                obj.getString("addressCustomer"), obj.getString("addressDelivery"),
                                obj.getString("telephoneNumber"));
    }

    public static DriverInfo parseDriverInfoFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        return new DriverInfo(obj.getInt("driverId"), obj.getString("driverFirstName"),
                obj.getString("driverSurName"), obj.getString("carProducer"),
                obj.getString("carBrand"), obj.getString("inOrder"),
                obj.getString("latitude"), obj.getString("longitude"));
    }

    public static PriceByClass parsePriceByClassFromString(String s) throws Exception {
        final JSONObject obj = new JSONObject(s);
        List<PriceByClass> priceByClassList = new ArrayList<>();
        PriceByClass priceByClass = new PriceByClass();
        priceByClass.setDistance(Float.parseFloat(obj.getString("distance")));
        priceByClass.setClassName(obj.getString("className"));
        String str = obj.getString("priceByClass");
        str = str.replace("[","");
        str = str.replace("]","");
        String[] s1 = str.split(",");
        List<Float> priceList = new ArrayList<>();
        for (int i = 0; i< s1.length; i++){
            priceList.add(Float.valueOf(s1[i]));
        }
        priceByClass.setPriceByClass(priceList);
        return priceByClass;
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
