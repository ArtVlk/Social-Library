package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public User(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
    public User() {};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private PhoneNumber phoneNumber;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;
    private String gender;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;
    private String password;
    private Role role;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public User(String name, String password, String phoneNumber, String country, String gender, String address) {
        this.name = name;
        this.password = password;
        this.phoneNumber = new PhoneNumber(phoneNumber);

        String[] countryAttributes = new String[3];
        String[] countryAttrFromReg = country.split(" ");
        for (int i = 0; i < countryAttrFromReg.length; i++)
            countryAttributes[i] = countryAttrFromReg[i];

        String[] addressAttributes = new String[3];
        String[] addressAttrFromReg = address.split(" ");
        for (int i = 0; i < addressAttrFromReg.length; i++) {
            addressAttributes[i] = addressAttrFromReg[i];
        }

        this.country = new Country(countryAttributes[0], countryAttributes[1]);
        this.gender = gender;
        this.address = new Address(addressAttributes[0], addressAttributes[1], addressAttributes[2]);
        this.role = Role.USER;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

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

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    public String getStringPhoneNumber() {
        return phoneNumber.getNumber();
    }
}
