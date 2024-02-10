package com.company.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @NotEmpty(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between {min} and {max} characters long")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(min = 1, max = 50 , message = "Last name must be between {min} and {max} characters long")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
