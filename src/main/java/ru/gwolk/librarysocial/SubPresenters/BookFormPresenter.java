package ru.gwolk.librarysocial.SubPresenters;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.AuthorRepository;
import ru.gwolk.librarysocial.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.CRUDRepositories.GenreRepository;
import ru.gwolk.librarysocial.Entities.Author;
import ru.gwolk.librarysocial.Entities.Book;
import ru.gwolk.librarysocial.Entities.Genre;

@SpringComponent
@UIScope
public class BookFormPresenter extends VerticalLayout {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    private TextField nameField = new TextField("Название книги");
    private TextField authorField = new TextField("Автор");
    private TextField genreField = new TextField("Жанр");
    private TextArea descriptionField = new TextArea("Описание");
    private NumberField ratingField = new NumberField("Оценка");
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button("Отмена");

    @Autowired
    public BookFormPresenter(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;

        setVisible(false);
        setSizeUndefined();
        add(nameField, authorField, genreField, descriptionField, ratingField, new HorizontalLayout(saveButton, cancelButton));

        saveButton.addClickListener(e -> saveBook());
        cancelButton.addClickListener(e -> cancelForming());
    }

    private void cancelForming() {
        setVisible(false);
    }

    public void clearForm() {
        nameField.clear();
        authorField.clear();
        genreField.clear();
        descriptionField.clear();
        ratingField.clear();
    }

    public void saveBook() {
        if (isValid()) {
            Author author = getOrCreateAuthor(authorField.getValue());
            Genre genre = getOrCreateGenre(genreField.getValue());
            Book book = createBook(nameField.getValue(), author, genre, descriptionField.getValue(), ratingField.getValue().intValue());

            bookRepository.save(book);

            clearForm();
            Notification.show("Книга добавлена!");
        } else {
            Notification.show("Заполните все поля.");
        }
    }

    private Author getOrCreateAuthor(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = new Author(authorName);
                    return authorRepository.save(newAuthor);
                });
    }

    private Genre getOrCreateGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> {
                    Genre newGenre = new Genre(genreName);
                    return genreRepository.save(newGenre);
                });
    }

    private Book createBook(String name, Author author, Genre genre, String description, Integer stars) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setDescription(description);
        book.setStars(stars);
        return book;
    }


    private boolean isValid() {
        return !nameField.getValue().isEmpty() && !authorField.getValue().isEmpty() &&
                !genreField.getValue().isEmpty() && !descriptionField.getValue().isEmpty() &&
                ratingField.getValue() != null;
    }

}
