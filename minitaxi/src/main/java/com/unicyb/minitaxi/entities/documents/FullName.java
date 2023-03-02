package com.unicyb.minitaxi.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullName {
    private int fullNameId;
    private String firstName;
    private String surName;
    private String patronymic;

    public FullName(String firstName, String surName, String patronymic) {
        this.firstName = firstName;
        this.surName = surName;
        this.patronymic = patronymic;
    }
}
