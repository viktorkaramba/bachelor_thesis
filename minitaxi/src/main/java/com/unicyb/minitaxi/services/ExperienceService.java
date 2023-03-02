package com.unicyb.minitaxi.services;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;

public class ExperienceService {
    public static float getUpdateExperience(Timestamp driver, Timestamp date){
        final long MILLIS_PER_DAY = 1000*60*60*24;
        long time_difference = driver.getTime() - date.getTime();
        long days_difference = (time_difference / MILLIS_PER_DAY) % 365;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float result = (float)days_difference * (-100) / 36500;
        return Float.parseFloat(df.format(result).replace(',','.'));
    }
}
