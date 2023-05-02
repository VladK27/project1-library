package ru.karelin.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String title;

    //[A-Z]\w{2-20} [A-Z]\w{2-20} [A-Z]\w{2-20}; not null
    private String author;

    //not null; year <= 2023
    private int year;
}
