package ru.karelin.project.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private int id;

    private String title;

    //[A-Z]\w{2-20} [A-Z]\w{2-20} [A-Z]\w{2-20}; not null
    @NotNull(message = "Name can't be empty")
    @NotEmpty(message = "Name can't be empty")
    @Pattern(regexp = "~[A-Z]\\w{2,20}\\s|[A-Z]\\w{2,20}~gm")
    private String author;

    //not null; year <= 2023
    @NotNull(message = "Year can't be empty")
    @Size(max=2023)
    private int year;

    @Size(min=0)
    private int owner;

    public Book(String title, String author, int year){
        this.title = title;
        this.author = author;
        this.year = year;
    }
}
