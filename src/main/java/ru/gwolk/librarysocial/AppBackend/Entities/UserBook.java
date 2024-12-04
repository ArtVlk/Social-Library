package ru.gwolk.librarysocial.AppBackend.Entities;

import jakarta.persistence.*;

/**
 * Сущность {@link UserBook} представляет связь между пользователем и книгой.
 * <p>
 * Хранит информацию о том, какую книгу пользователь прочитал, его рейтинг и отзыв.
 * Сущность отображается в таблицу "user_books" базы данных.
 * </p>
 */
@Entity
@Table(name = "user_books")
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer userRating;

    private String review;

    /**
     * Конструктор для создания связи между пользователем и книгой с указанием пользовательского рейтинга.
     *
     * @param user       пользователь, связанный с книгой
     * @param book       книга, связанная с пользователем
     * @param userRating пользовательский рейтинг книги
     */
    public UserBook (User user, Book book, Integer userRating) {
        this.user = user;
        this.book = book;
        this.userRating = userRating;
    }

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта связи, например, при десериализации.
     */
    public UserBook() {

    }

    public Integer getUserRating() {
        return userRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
