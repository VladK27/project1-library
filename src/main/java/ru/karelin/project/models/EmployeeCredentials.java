package ru.karelin.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee_credentials")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EmployeeCredentials {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @Size(min = 2, max = 100, message = "Username must has length from 2 to 100")
    private String username;

    @Column(name = "password")
    @Size(min = 8, max = 100, message = "Password must has length from 8 to 100")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    private Role role;

    @JoinColumn(name = "personal_info_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Employee employee;

    public EmployeeCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    public EmployeeCredentials(String username, String password, Role role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}