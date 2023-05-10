package com.unicyb.energytaxi.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SalaryService {
    private static final int MIN_EXPERIENCE = 1;
    private static final int MIN_SALARY = 4500;
    private static final float COEFFICIENT = 1.2f;
    public static float getSalary(float experience){
        if(experience > MIN_EXPERIENCE){
            return MIN_SALARY*COEFFICIENT*experience;
        }
        else {
            return MIN_SALARY;
        }
    }

    public static float getNewSalary(Float salary){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return Float.parseFloat(df.format(salary*COEFFICIENT).replace(',','.'));
    }
}
