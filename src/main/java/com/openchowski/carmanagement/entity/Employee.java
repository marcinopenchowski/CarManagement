package com.openchowski.carmanagement.entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @NotNull(message="First Name is required.")
    @Size(min=1, message="First Name is required.")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message="Last Name is required.")
    @Size(min=1, message="Last Name is required.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message="Email is required.")
    @Email(message = "Email should be valid.")
    @Column(name = "email")
    private String email;

    @NotNull(message="Job Title is required.")
    @Size(min=1, message="Job Title is required.")
    @Column(name = "job_title")
    private String jobTitle;

    @Min(value = 100000000, message = "Phone number should be valid.")
    @Max(value = 999999999, message = "Phone number should be valid.")
    @Column(name = "phone_number")
    private int phoneNumber;


    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, String jobTitle, int phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
