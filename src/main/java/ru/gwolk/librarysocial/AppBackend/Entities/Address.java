package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность {@link Address} представляет адрес пользователя, включающий город, улицу и номер дома.
 * Этот класс используется для хранения информации о местоположении пользователя в базе данных.
 *
 * Адрес связан с пользователями через отношение "один ко многим", а также связан с страной через отношение "многие к одному".
 */
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String houseNumber;
    /**
     * Конструктор для создания адреса с указанным городом, улицей и номером дома.
     *
     * @param city Город, в котором находится адрес.
     * @param street Улица, на которой находится адрес.
     * @param houseNumber Номер дома по указанной улице.
     */
    public Address(String city, String street, String houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    /**
     * Конструктор по умолчанию для создания пустого адреса.
     */
    public Address() {}


    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
