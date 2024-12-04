package ru.gwolk.librarysocial.AppFrontend.SubPresenters;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.AuthorRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.GenreRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.LibraryServices.BookService;
import ru.gwolk.librarysocial.AppBackend.Entities.Author;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.Genre;

/**
 * Класс для представления редактора книги.
 * Обеспечивает функциональность для редактирования, сохранения, удаления и отмены изменений книги.
 */
@SpringComponent
@UIScope
public class BookEditorPresenter extends VerticalLayout {
    private final BookService bookService;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final UserBookRepository userBookRepository;
    private TextField nameField = new TextField("Название");
    private TextField authorField = new TextField("Автор");
    private TextField genreField = new TextField("Жанр");
    private TextField descriptionField = new TextField("Описание");
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button("Отмена");
    private Button deleteButton = new Button("Удалить");
    private Book currentBook;

    /**
     * Конструктор класса BookEditorPresenter.
     * Инициализирует необходимые репозитории и сервисы, а также настраивает интерфейс.
     *
     * @param bookRepository репозиторий для работы с книгами
     * @param authorRepository репозиторий для работы с авторами
     * @param genreRepository репозиторий для работы с жанрами
     * @param userBookRepository репозиторий для работы с пользовательскими книгами
     * @param bookService сервис для работы с книгами
     */
    @Autowired
    public BookEditorPresenter(BookRepository bookRepository, AuthorRepository authorRepository,
                               GenreRepository genreRepository, UserBookRepository userBookRepository,
                               BookService bookService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userBookRepository = userBookRepository;
        this.bookService = bookService;

        descriptionField.setWidth("60%");
        descriptionField.setHeight("200px");

        HorizontalLayout actionsLayout = new HorizontalLayout(saveButton, cancelButton);

        deleteButton.getStyle()
                .set("background-color", "red").set("color", "white");

        setHorizontalComponentAlignment(Alignment.CENTER, nameField, authorField,
                genreField, descriptionField, actionsLayout, deleteButton);
        add(nameField, authorField, genreField, descriptionField, actionsLayout, deleteButton);

        saveButton.addClickListener(e -> saveBook());
        cancelButton.addClickListener(e -> cancelEditing());
        deleteButton.addClickListener(e -> {
            if (currentBook != null) {
                deleteBook();
            } else {
                Notification.show("Не выбрана книга для удаления");
            }
        });

        setVisible(false);
    }

    /**
     * Редактирует книгу. Заполняет поля формы значениями книги.
     *
     * @param book книга, которую необходимо отредактировать
     */
    public void editBook(Book book) {
        this.currentBook = book;

        if (book == null) {
            setVisible(false);
            return;
        }

        nameField.setValue(book.getName() != null ? book.getName() : "");
        authorField.setValue(book.getAuthor() != null ? book.getAuthor().getName() : "");
        genreField.setValue(book.getGenre() != null ? book.getGenre().getName() : "");
        descriptionField.setValue(book.getDescription() != null ? book.getDescription() : "");
    }

    private void saveBook() {
        if (currentBook == null) return;

        updateBookDetails();
        handleAuthor();
        handleGenre();

        bookRepository.save(currentBook);

        Notification.show("Книга успешно сохранена!");
        setVisible(false);
    }

    private void updateBookDetails() {
        currentBook.setName(nameField.getValue());
        currentBook.setDescription(descriptionField.getValue());
    }

    private void handleAuthor() {
        String authorName = authorField.getValue().trim();
        if (!authorName.isEmpty()) {
            Author author = authorRepository.findByName(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorName);
                        return authorRepository.save(newAuthor);
                    });
            currentBook.setAuthor(author);
        }
    }

    private void handleGenre() {
        String genreName = genreField.getValue().trim();
        if (!genreName.isEmpty()) {
            Genre genre = genreRepository.findByName(genreName)
                    .orElseGet(() -> {
                        Genre newGenre = new Genre();
                        newGenre.setName(genreName);
                        return genreRepository.save(newGenre);
                    });
            currentBook.setGenre(genre);
        }
    }

    private void deleteBook() {
        if (currentBook != null) {
            bookService.deleteBook(currentBook.getId());
            Notification.show("Книга успешно удалена!");
            setVisible(false);
        } else {
            Notification.show("Не выбрана книга для удаления");
        }
    }

    private void cancelEditing() {
        setVisible(false);
    }

}
