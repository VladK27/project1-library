package ru.karelin.project.models;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "~[A-Z]\\w{2,20}\\s|[A-Z]\\w{2,20}~gm", message = "Name should starts with big letter and contains only letters")
    private String author;

    //not null; year <= 2023
    @NotNull(message = "Year can't be empty")
    @Max(value=2023, message = "Year must be under 2023")
    private int year;

    @Min(0)
    private int owner;

    public Book(String title, String author, int year){
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public boolean hasOwner(){
        return this.owner != 0;
    }
}
