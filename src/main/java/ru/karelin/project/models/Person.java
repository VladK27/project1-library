package ru.karelin.project.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private int id;

    //[A-Z]\w+; 2 <= length >= 20; not null
    @NotNull(message = "Name can't be empty")
    @Size(min=1, max=30, message = "Name should be from 1 to 30 symbols")
    @Pattern(regexp = "[A-Z]\\w+", message = "Name should starts with big letter and contains only letters")
    private String name;

    //[A-Z]\w+; 2 <= length >= 20; not null@Size(min=1, max=30, message = "Name should be from 1 to 30 symbols")
    @NotNull(message = "Surname can't be empty")
    @Size(min=1, max=30, message = "Surname should be from 1 to 30 symbols")
    @Pattern(regexp = "[A-Z]\\w+", message = "Surname should starts with big letter and contains only letters")
    private String surname;

    //1900 < year < 2023
    @NotNull(message = "Year can't be empty")
    @Min(value=1900, message = "Reader is too old for library")
    @Max(value=2023, message = "Reader is too young for library")
    private int yearOfBirth;

    public String getFullName(){
        return name + " " + surname;
    }
}
