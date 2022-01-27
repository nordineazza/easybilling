package fr.easybilling.web.rest.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserForm {

    @Size(max = 50)
    @JsonProperty("firstname")
    private String firstName;

    @Size(max = 50)
    @JsonProperty("lastname")
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private String password;

    public UserForm() {
        // Empty constructor needed for Jackson.
    }

    public UserForm(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
