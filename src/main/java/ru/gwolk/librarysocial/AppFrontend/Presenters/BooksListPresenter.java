package ru.gwolk.librarysocial.AppFrontend.Presenters;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.AppBackend.LibraryServices.BookService;
import ru.gwolk.librarysocial.AppBackend.CommonServices.PagesNavigator;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.Role;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookDetailPresenter;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookEditorPresenter;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookFormPresenter;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.MainLayout;

import java.util.Collection;

/**
 * Презентер для отображения списка книг в библиотеке.
 * Этот класс управляет отображением и взаимодействием с компонентами UI, такими как таблица книг, фильтр и кнопки сортировки.
 * Презентер поддерживает отображение книг с возможностью фильтрации, сортировки, добавления и просмотра детальной информации о книге.
 */
@Route(value = "books", layout = MainLayout.class)
@PageTitle("Книги")
@PermitAll
public class BooksListPresenter extends VerticalLayout {
    private final String LOCAL_BOOKS_PAGE = "user-local-books";
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Grid<Book> grid;
    private final Button addBookButton = new Button("Добавить книгу");
    private final Button localBooksButton = new Button("Мои книги", VaadinIcon.FOLDER.create());
    private final BookDetailPresenter bookDetailPresenter;
    private final BookFormPresenter bookFormPresenter;
    private final BookEditorPresenter bookEditorPresenter;
    private final BookService bookService;

    private final Button sortButton = new Button("Сортировать по", VaadinIcon.SORT.create());
    private int sortState = 0; // 0 - по названию, 1 - по автору, 2 - по жанру, 3 - по оценке

    /**
     * Конструктор для инициализации всех необходимых зависимостей.
     *
     * @param bookRepository репозиторий для работы с книгами.
     * @param currentUserService сервис для получения информации о текущем пользователе.
     * @param bookDetailPresenter презентер для отображения деталей книги.
     * @param bookFormPresenter презентер для отображения формы добавления книги.
     * @param bookEditorPresenter презентер для редактирования книги.
     * @param bookService сервис для работы с книгами.
     */
    @Autowired
    public BooksListPresenter(BookRepository bookRepository, CurrentUserService currentUserService,
                              BookDetailPresenter bookDetailPresenter, BookFormPresenter bookFormPresenter,
                              BookEditorPresenter bookEditorPresenter, BookService bookService){
        this.bookRepository = bookRepository;
        this.bookDetailPresenter = bookDetailPresenter;
        this.bookFormPresenter = bookFormPresenter;
        this.bookEditorPresenter = bookEditorPresenter;
        this.currentUserService = currentUserService;
        this.bookService = bookService;

        grid = new Grid<>(Book.class);
        setBooksGrid(grid);

        add(new HorizontalLayout(filter, sortButton, localBooksButton), grid, addBookButton, bookDetailPresenter, bookFormPresenter, bookEditorPresenter);


        setupFilter();
        setupGridSelection();
        setupSortButton();
        setupLocalBooksButton();
        setupAddBookButton();
        showAddBookButton();
        showBook("");
    }

    private void setupFilter() {
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showBook(e.getValue()));
    }

    private void setupGridSelection() {
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                if (currentUserService.getCurrentUser().getRole() == Role.ADMIN) {
                    bookEditorPresenter.editBook(event.getValue());
                    bookEditorPresenter.setVisible(true);
                    bookDetailPresenter.setVisible(false);
                } else {
                    bookDetailPresenter.editBook(event.getValue());
                    bookDetailPresenter.setVisible(true);
                    bookEditorPresenter.setVisible(false);
                }
                bookFormPresenter.setVisible(false);
            } else {
                bookDetailPresenter.setVisible(false);
                bookEditorPresenter.setVisible(false);
            }
        });
    }

    private void setupSortButton() {
        sortButton.addClickListener(e -> sortGrid());
    }

    private void setupLocalBooksButton() {
        localBooksButton.addClickListener(e -> PagesNavigator.navigateTo(LOCAL_BOOKS_PAGE));
    }

    private void showAddBookButton() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            addBookButton.setVisible(true);
        } else {
            addBookButton.setVisible(false);
        }
    }

    private void openBookForm() {
        bookDetailPresenter.setVisible(false);
        bookEditorPresenter.setVisible(false);
        bookFormPresenter.setVisible(true);
    }

    private void showBook(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            grid.setItems((Collection<Book>) bookRepository.findAll());
        } else {
            grid.setItems(bookRepository.findByNameContaining(filterText));
        }
    }

    private void setupAddBookButton() {
        addBookButton.addClickListener(e -> openBookForm());
    }

    private void setBooksGrid(Grid<Book> grid) {
        grid.setHeight("600px");
        grid.setWidth("1800px");
        grid.setColumns();

        grid.addColumn(Book::getName).setHeader("Название").setWidth("450px");
        grid.addColumn(book -> book.getAuthor().getName()).setHeader("Автор").setWidth("450px");
        grid.addColumn(book -> book.getGenre().getName()).setHeader("Жанр").setWidth("450px");
        grid.addColumn(Book::getStars).setHeader("Оценка").setWidth("450px");

        grid.getElement().getStyle().set("margin-left", "auto");
        grid.getElement().getStyle().set("margin-right", "auto");
    }

    private void sortGrid() {
        switch (sortState) {
            case 0:
                grid.setItems(bookRepository.findAllByOrderByNameAsc());
                sortButton.setText("Сортировать по: Название");
                break;
            case 1:
                grid.setItems(bookRepository.findAllByOrderByAuthor_NameAsc());
                sortButton.setText("Сортировать по: Автор");
                break;
            case 2:
                grid.setItems(bookRepository.findAllByOrderByGenre_NameAsc());
                sortButton.setText("Сортировать по: Жанр");
                break;
            case 3:
                grid.setItems(bookRepository.findAllByOrderByStarsDesc());
                sortButton.setText("Сортировать по: Оценка");
                break;
        }
        sortState = (sortState + 1) % 4;
    }

}
