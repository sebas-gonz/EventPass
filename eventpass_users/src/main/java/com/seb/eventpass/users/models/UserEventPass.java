package com.seb.eventpass.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users", schema = "user")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserEventPass extends BaseEntity{
    private @Id Long id;

    private String firstname;
    private String lastname;

    private String role;
    private String email;
    private String password;
}
