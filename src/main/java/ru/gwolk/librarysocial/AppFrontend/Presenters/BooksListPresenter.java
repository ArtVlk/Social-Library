package ru.gwolk.librarysocial.AppFrontend.Presenters;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.BookService;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.Role;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookDetailPresenter;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookEditorPresenter;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.BookFormPresenter;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.MainLayout;

import java.util.Collection;

@Route(value = "books", layout = MainLayout.class)
@PageTitle("Книги")
@PermitAll
public class BooksListPresenter extends VerticalLayout {
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Grid<Book> grid;
    private final Button addBookButton = new Button("Добавить книгу");
    private final BookDetailPresenter bookDetailPresenter;
    private final BookFormPresenter bookFormPresenter;
    private final BookEditorPresenter bookEditorPresenter;
    private final BookService bookService;


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

        add(new HorizontalLayout(filter), grid, addBookButton, bookDetailPresenter, bookFormPresenter, bookEditorPresenter);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showBook(e.getValue()));

        bookDetailPresenter.setVisible(false);
        bookFormPresenter.setVisible(false);
        bookEditorPresenter.setVisible(false);

        showAddBookButton();

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


        bookService.updateGrid(grid);

        addBookButton.addClickListener(e -> openBookForm());

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
        bookFormPresenter.setVisible(true);
        bookDetailPresenter.setVisible(false);
    }

    public void updateGrid() {
        grid.setItems((Collection<Book>) bookRepository.findAll());
    }

    private void showBook(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            grid.setItems((Collection<Book>) bookRepository.findAll());
        } else {
            grid.setItems(bookRepository.findByNameContaining(filterText));
        }
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

}
