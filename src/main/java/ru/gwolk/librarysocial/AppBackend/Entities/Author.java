package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Представляет сущность автора в системе.
 * <p>
 * Автор может быть связан с множеством книг.
 * Сущность отображается в таблицу "authors" базы данных.
 * </p>
 */
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "author")
    private Set<Book> books = new HashSet<>();

    public Author(String name, Book book) {
        this.name = name;
        books.add(book);
    }

    /**
     * Конструктор для создания автора с именем.
     *
     * @param name имя автора
     */
    public Author(String name) {
        this.name = name;
    }

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта автора, например, при десериализации.
     */
    public Author() {

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
