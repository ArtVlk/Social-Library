package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

/**
 * Сущность {@link Book} представляет сущность книги в системе.
 * <p>
 * Каждая книга имеет такие атрибуты, как название, описание, рейтинг (в звездах),
 * жанр, автор и статистическую информацию о пользовательских оценках.
 * Сущность отображается в таблицу "books" базы данных.
 * </p>
 */
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Integer stars;
    private Integer sumStars;
    private Integer estimationCount;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    /**
     * Конструктор инициализации
     * <p>
     * Если создаём книгу со всеми полями
     * </p>
     */
    public Book(String name, Author author, Genre genre, String description, int stars) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.stars = stars;
    }

    /**
     * Конструктор по умолчанию
     * <p>
     * Создаём пустую книгу
     * </p>
     */
    public Book() {}

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Integer getSumStars() {
        return sumStars;
    }

    public void setSumStars(Integer sumStars) {
        this.sumStars = sumStars;
    }

    public Integer getEstimationCount() {
        return estimationCount;
    }

    public void setEstimationCount(Integer estimationCount) {
        this.estimationCount = estimationCount;
    }
}
