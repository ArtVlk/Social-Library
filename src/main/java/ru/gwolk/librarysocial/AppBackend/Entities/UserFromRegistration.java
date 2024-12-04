package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.validation.constraints.NotBlank;

/**
 * Класс {@link UserFromRegistration} используется для представления данных,
 * которые отправляются пользователем при регистрации в систему.
 *
 * Этот класс содержит основные поля, такие как имя, пароль, страну, номер телефона,
 * пол и адрес, которые необходимы для создания нового пользователя.
 * Поля класса имеют аннотацию {@link NotBlank}, которая обеспечивает валидацию на отсутствие пустых значений.
 */
public class UserFromRegistration {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    private String country;
    private String phoneNumber;
    private String gender;
    private String address;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
