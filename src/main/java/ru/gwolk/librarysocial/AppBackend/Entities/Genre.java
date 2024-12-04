package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность {@link Genre} представляет сущность жанра в системе.
 * <p>
 * Жанр может быть связан с множеством книг.
 * Сущность отображается в таблицу "genres" базы данных.
 * </p>
 */
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "genre")
    private Set<Book> books = new HashSet<>();

    public Genre(String name, Book book) {
        this.name = name;
        books.add(book);
    }

    /**
     * Конструктор для создания жанра с именем.
     *
     * @param name имя жанра
     */
    public Genre(String name) {
        this.name = name;
    }


    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта жанра, например, при десериализации.
     */
    public Genre() {

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
}
