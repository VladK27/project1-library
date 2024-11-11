package ru.karelin.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee_info")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @Size(min=2, max=100, message = "Name must has from 2 to 100 length")
    private String name;

    @Column(name = "surname")
    @Size(min=2, max=100, message = "Surname must has from 2 to 100 length")
    private String surname;

    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Year of birth can't be under 1900")
    @Max(value = 2006, message = "Year of birth can't be after 2006")
    private int yearOfBirth;

    @Column(name = "phone_number")
    @Size(min = 8, max = 20, message = "Phone number must has from 8 to 20 length")
    private String phoneNumber;

    @Column(name = "email")
    //@Pattern(regexp = "\"^[a-zA-Z0-9.!#$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$\"", message = "Email wrong pattern!")
    private String email;

    public String getFullName(){
        return name + " " + surname;
    }

    @OneToOne(mappedBy = "employee")
    private EmployeeCredentials credentials;
}
