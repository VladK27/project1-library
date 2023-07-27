package ru.karelin.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotNull(message = "Name can't be empty")
    @NotEmpty(message = "Name can't be empty")
    @Size(min=2, max=200, message = "Title should be from 2 to 200 symbols")
    private String title;

    @Column(name = "author")
    @NotNull(message = "Name can't be empty")
    @NotEmpty(message = "Name can't be empty")
    @Pattern(regexp = "([A-Z][a-z]{2,30} )+[A-Z][a-z]{2,30}|[A-Z][a-z]{2,30}", message = "Name should starts with big letter and contains only letters")
    private String author;

    @Column(name = "year")
    @NotNull(message = "Year can't be empty")
    @Max(value=2023, message = "Year must be under 2023")
    private int year;

    @Column(name = "date_of_issue")
    @Temporal(TemporalType.DATE)
    private Date dateOfIssue;

    @Transient
    private boolean isOverdue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    public static final Set<String> propertiesSet = new HashSet<>(Set.of(
            "id", "title", "author", "year", "owner"
    ));

    public Book(String title, String author, int year){
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public boolean hasOwner(){
        return getOwner() != null;
    }

    public void setOverdue(){
        if(dateOfIssue != null){
            Date currentDate = new Date();
            long millisInDay = 86_400_000;
            long difference = (currentDate.getTime() - dateOfIssue.getTime() ) / millisInDay;
            this.isOverdue = difference > 10;
        }
    }
}
