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
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.PersonalBookDetailPresenter;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.MainLayout;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;

import java.util.Collection;

@Route(value = "user-local-books", layout = MainLayout.class)
@PageTitle("Мои книги")
@PermitAll
public class UserBooksListPresenter extends VerticalLayout {
    private final UserBookRepository userBookRepository;
    private final CurrentUserService currentUserService;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Grid<UserBook> grid;

    private final Button sortButton = new Button("Сортировать по", VaadinIcon.SORT.create());
    private int sortState = 0; // 0 - по названию, 1 - по автору, 2 - по жанру

    @Autowired
    public UserBooksListPresenter(UserBookRepository userBookRepository, CurrentUserService currentUserService,
                                  PersonalBookDetailPresenter personalBookDetailPresenter) {
        this.userBookRepository = userBookRepository;
        this.currentUserService = currentUserService;

        grid = new Grid<>(UserBook.class);
        setUserBooksGrid(grid);

        add(new HorizontalLayout(filter, sortButton), grid, personalBookDetailPresenter);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showBook(e.getValue()));

        personalBookDetailPresenter.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            UserBook selectedUserBook = event.getValue();
            if (selectedUserBook != null){
                personalBookDetailPresenter.editBook(selectedUserBook);
                personalBookDetailPresenter.setVisible(true);
            } else {
                personalBookDetailPresenter.setVisible(false);}
        });

        loadUserBooks();
        sortButton.addClickListener(e -> sortGrid());
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

    private void showBook(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            loadUserBooks();
        } else {
            Collection<UserBook> filteredBooks = userBookRepository.findByUserAndBookNameContaining(currentUserService.getCurrentUser(), filterText);
            grid.setItems(filteredBooks);
        }
    }

    private void sortGrid() {
        Collection<UserBook> sortedBooks;
        switch (sortState) {
            case 0:
                sortedBooks = userBookRepository.findByUserOrderByBook_NameAsc(currentUserService.getCurrentUser());
                sortButton.setText("Сортировать по: Название");
                break;
            case 1:
                sortedBooks = userBookRepository.findByUserOrderByBook_Author_NameAsc(currentUserService.getCurrentUser());
                sortButton.setText("Сортировать по: Автор");
                break;
            case 2:
                sortedBooks = userBookRepository.findByUserOrderByBook_Genre_NameAsc(currentUserService.getCurrentUser());
                sortButton.setText("Сортировать по: Жанр");
                break;
            case 3:
                sortedBooks = userBookRepository.findByUserOrderByUserRatingDesc(currentUserService.getCurrentUser());
                sortButton.setText("Сортировать по: Оценка");
                break;
            default:
                sortedBooks = userBookRepository.findByUser(currentUserService.getCurrentUser());
        }
        grid.setItems(sortedBooks);
        sortState = (sortState + 1) % 4;
    }
}
