package com.unicyb.minitaxi;

import com.unicyb.minitaxi.distancematrixapi.DistanceMatrixAPi;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MiniTaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniTaxiApplication.class, args);
    }

}
