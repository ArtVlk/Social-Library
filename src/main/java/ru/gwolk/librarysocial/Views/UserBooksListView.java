package ru.gwolk.librarysocial.Views;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.Entities.UserBook;
import ru.gwolk.librarysocial.Services.CurrentUserService;
import ru.gwolk.librarysocial.Widgets.MainLayout;

import java.util.Collection;

@Route(value = "user-books", layout = MainLayout.class)
@PageTitle("Мои книги")
@PermitAll
public class UserBooksListView extends VerticalLayout {
    private final UserBookRepository userBookRepository;
    private final CurrentUserService currentUserService;
    private final Grid<UserBook> grid;

    @Autowired
    public UserBooksListView(UserBookRepository userBookRepository, CurrentUserService currentUserService) {
        this.userBookRepository = userBookRepository;
        this.currentUserService = currentUserService;

        grid = new Grid<>(UserBook.class);
        setUserBooksGrid(grid);

        add(grid);

        loadUserBooks();
    }


    private void setUserBooksGrid(Grid<UserBook> grid) {
        grid.setHeight("600px");
        grid.setWidth("1800px");
        grid.setColumns();

        grid.addColumn(userBook -> userBook.getBook().getName()).setHeader("Название").setWidth("450px");
        grid.addColumn(userBook -> userBook.getBook().getAuthor().getName()).setHeader("Автор").setWidth("450px");
        grid.addColumn(userBook -> userBook.getBook().getGenre().getName()).setHeader("Жанр").setWidth("450px");
        grid.addColumn(UserBook::getUserRating).setHeader("Оценка").setWidth("450px");

        grid.getElement().getStyle().set("margin-left", "auto");
        grid.getElement().getStyle().set("margin-right", "auto");
    }

    private void loadUserBooks() {
        Collection<UserBook> userBooks = userBookRepository.findByUser(currentUserService.getCurrentUser());
        grid.setItems(userBooks);
    }
}
