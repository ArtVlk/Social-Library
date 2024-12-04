package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

/**
 * Сущность {@link PhoneNumber} представляет телефонный номер, связанный с пользователем.
 * Этот класс используется для хранения информации о телефонных номерах пользователей в базе данных.
 *
 * Каждый номер телефона может быть привязан к одному пользователю, и может иметь тип (например, мобильный или домашний).
 */
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

    /**
     * Конструктор для создания телефонного номера с указанным номером.
     *
     * @param number Номер телефона.
     */
    public PhoneNumber(String number) {
        this.number = number;
    }


    /**
     * Конструктор по умолчанию для создания пустого телефонного номера.
     */
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
