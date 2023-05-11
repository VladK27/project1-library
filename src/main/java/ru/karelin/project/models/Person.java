package ru.karelin.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private int id;

    //[A-Z]\w+; 2 <= length >= 20; not null
    private String name;

    //[A-Z]\w+; 2 <= length >= 20; not null
    private String surname;

    //1900 < year < 2023
    private int yearOfBirth;
}
