package com.unicyb.minitaxi.entities.documents;

import com.unicyb.minitaxi.entities.indicators.STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    private int driverId;
    private Timestamp date;
    private int carId;
    private int fullNameId;
    private String telephoneNumber;
    private float experience;
    private float salary;
    private STATUS resumeStatus;
    private String driverUserName;
    public Driver(int driverId,  Timestamp date, int fullNameId, String telephoneNumber, float experience,
                  float salary, STATUS resumeStatus, String driverUserName) {
        this.driverId = driverId;
        this.date = date;
        this.fullNameId = fullNameId;
        this.telephoneNumber = telephoneNumber;
        this.experience = experience;
        this.salary = salary;
        this.resumeStatus = resumeStatus;
        this.driverUserName = driverUserName;
    }
}
