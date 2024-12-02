package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "phoneNumbers")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String number;
    private String type;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    public PhoneNumber(String number) {
        this.number = number;
    }

    public PhoneNumber() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
