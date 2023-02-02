package com.bajaks.RisWebshopApi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Enumerated
    private Role role;

    public String getRoleName(){
        if (role == Role.ADMINISTRATOR) {
            return "ADMINISTRATOR";
        }
        if (role == Role.EMPLOYEE) {
            return "EMPLOYEE";
        }
        return "USER";
    }
}
