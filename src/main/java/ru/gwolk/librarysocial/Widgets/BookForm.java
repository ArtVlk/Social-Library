package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
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

import java.util.HashSet;
import java.util.Set;

@SpringComponent
@UIScope
public class BookForm extends VerticalLayout {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    private TextField nameField = new TextField("Название книги");
    private TextField authorField = new TextField("Автор");
    private TextField genreField = new TextField("Жанр");
    private TextArea descriptionField = new TextArea("Описание");
    private NumberField ratingField = new NumberField("Оценка");
    private Button saveButton = new Button("Сохранить");

    @Autowired
    public BookForm(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;

        setVisible(false);
        setSizeUndefined();
        add(nameField, authorField, genreField, descriptionField, ratingField, saveButton);

        saveButton.addClickListener(e -> saveBook());
    }

    public void saveBook() {
        if (isValid()) {
            // Получаем или создаем нового автора
            Author author = authorRepository.findByName(authorField.getValue())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorField.getValue());
                        return authorRepository.save(newAuthor);
                    });

            // Получаем или создаем новый жанр
            Genre genre = genreRepository.findByName(genreField.getValue())
                    .orElseGet(() -> {
                        Genre newGenre = new Genre();
                        newGenre.setName(genreField.getValue());
                        return genreRepository.save(newGenre);
                    });

            // Создаем книгу
            Book book = new Book();
            book.setName(nameField.getValue());
            book.setAuthor(author);

            Set<Genre> genres = new HashSet<>();
            genres.add(genre);  // Добавляем жанр в Set
            book.setGenre((Genre) genres);

            book.setDescription(descriptionField.getValue());
            book.setStars(ratingField.getValue().intValue());

            bookRepository.save(book);
            clearForm();
            Notification.show("Книга добавлена!");
        } else {
            Notification.show("Заполните все поля.");
        }
    }

    private boolean isValid() {
        return !nameField.getValue().isEmpty() && !authorField.getValue().isEmpty() &&
                !genreField.getValue().isEmpty() && !descriptionField.getValue().isEmpty() &&
                ratingField.getValue() != null;
    }

    private void clearForm() {
        nameField.clear();
        authorField.clear();
        genreField.clear();
        descriptionField.clear();
        ratingField.clear();
        setVisible(false);
    }

}
