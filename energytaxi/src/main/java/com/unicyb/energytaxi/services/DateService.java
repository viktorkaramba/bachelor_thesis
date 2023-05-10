package com.unicyb.energytaxi.services;

import com.unicyb.energytaxi.entities.documents.PeriodsOfDay;

import java.time.LocalTime;

public class DateService {
    public static String recognize(LocalTime date){
        int hour = date.getHour();
        if (hour>0 && hour<6) {
            return PeriodsOfDay.NIGHT.name();
        }
        else if(hour>6 && hour<12) {
            return PeriodsOfDay.MORNING.name();
        }
        else if(hour>12 && hour<18) {
            return PeriodsOfDay.DAY.name();
        }
        else {
            return PeriodsOfDay.EVENING.name();
        }
    }
}
