package com.unicyb.minitaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResume {
    private String driverUserName;
    private String driverPassword;
    private String driverFirstName;
    private String driverSurName;
    private String driverPatronymic;
    private String driverTelephoneNumber;
    private float driverExperience;
}
