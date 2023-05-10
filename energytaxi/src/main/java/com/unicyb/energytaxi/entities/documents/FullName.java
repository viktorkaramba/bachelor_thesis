package com.unicyb.energytaxi.entities.documents;

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
}
