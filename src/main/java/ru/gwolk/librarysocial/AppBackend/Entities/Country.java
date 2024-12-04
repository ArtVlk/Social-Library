package ru.gwolk.librarysocial.AppBackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность {@link Country} представляет страну, которая может быть связана с пользователями и адресами.
 * Этот класс используется для хранения информации о странах в базе данных.
 *
 * Страна может быть связана с множеством пользователей и адресов, и каждый пользователь или адрес
 * может иметь одну страну, к которой он относится.
 */
@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String code;


    /**
     * Конструктор для создания страны с именем и кодом.
     *
     * @param name Название страны.
     * @param code Код страны.
     */
    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Конструктор по умолчанию для создания пустой страны.
     */
    public Country() {}

    /**
     * Конструктор для создания страны только с именем.
     *
     * @param name Название страны.
     */
    public Country(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "country", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "country")
    private Set<Address> addresses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }
}
