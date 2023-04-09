package com.unicyb.minitaxi.distancematrixapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class DistanceMatrixAPi {
    private static final String API_KEY = "AIzaSyA-OPsE3b6OZgPme5XDIzl_o1xV64aRWaw";
    private String source;
    private String destination;

    public DistanceMatrixAPi(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public float getDistance() throws IOException, InterruptedException, ParseException {
        StringBuilder url = new StringBuilder()
                .append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=")
                .append(URLEncoder.encode(source, StandardCharsets.UTF_8))
                .append("&destinations=")
                .append(URLEncoder.encode(destination, StandardCharsets.UTF_8))
                .append("&key=")
                .append(API_KEY);
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create(url.toString())).build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        String httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        return Float.parseFloat(convertJson(httpResponse))/1000;
    }

    private String convertJson(String response) throws ParseException {
        System.out.println(response);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("rows");
        jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray = (JSONArray) jsonObject.get("elements");
        jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject result = (JSONObject) jsonObject.get("distance");
        return result.get("value").toString();
    }

}
