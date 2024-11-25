package ru.gwolk.librarysocial.Views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.gwolk.librarysocial.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.Entities.Book;
import ru.gwolk.librarysocial.Entities.Role;
import ru.gwolk.librarysocial.Entities.StringRoles;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Services.CurrentUserService;
import ru.gwolk.librarysocial.Widgets.BookDetail;
import ru.gwolk.librarysocial.Widgets.BookForm;
import ru.gwolk.librarysocial.Widgets.MainLayout;

import java.util.Collection;

@Route(value = "books", layout = MainLayout.class)
@PageTitle("Книги")
@PermitAll
public class BooksListView extends VerticalLayout {
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Grid<Book> grid;
    private final BookDetail bookEditor;
    private final Button addBookButton = new Button("Добавить книгу");
    private final BookForm bookForm;

    @Autowired
    public BooksListView(BookRepository bookRepository, CurrentUserService currentUserService, BookDetail bookEditor, BookForm bookForm){
        this.bookRepository = bookRepository;
        this.bookEditor = bookEditor;
        this.bookForm = bookForm;
        this.currentUserService = currentUserService;

        grid = new Grid<>(Book.class);

        setBooksGrid(grid);
        add(new HorizontalLayout(filter), grid, addBookButton, bookEditor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showBook(e.getValue()));

        bookEditor.setVisible(false);

        showAddBookButton();

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                bookEditor.editBook(event.getValue());
                bookEditor.setVisible(true);
            } else {
                bookEditor.setVisible(false);
            }
        });

        grid.setItems((Collection<Book>) bookRepository.findAll());

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
        bookForm.setVisible(true);
    }

    private void showBook(String name) {
        if (name.isEmpty()) {
            grid.setItems((Collection<Book>) bookRepository.findAll());
        }
        else {
            grid.setItems(bookRepository.findByName(name));
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
