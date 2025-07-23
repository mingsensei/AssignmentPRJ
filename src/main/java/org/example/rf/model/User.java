package org.example.rf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN, MENTOR, STUDENT
    }

    // Constructors, getters and setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_name", length = 100)
    private String userName;

    @Setter
    @Getter
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Setter
    @Getter
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Setter
    @Getter
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Setter
    @Getter
    @Column(name = "phone", length = 20)
    private String phone;

    @Setter
    @Getter
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Getter
    @Setter
    @Column(name = "google_id", length = 100, unique = true)
    private String googleId;

    @Getter
    @Setter
    @Column(name = "profile_pic", length = 255)
    private String profilePic;
    

    public User() {}

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", role=" + role + ", phone=" + phone + ", password=" + password + ", googleId=" + googleId + '}';
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
