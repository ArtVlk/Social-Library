package ru.gwolk.librarysocial.Presenters;

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
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.CommonServices.PagesNavigator;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.SocialServices.UsersFilterService;
import ru.gwolk.librarysocial.AppLayouts.MainLayout;
import ru.gwolk.librarysocial.SubPresenters.UserEditorPresenter;

import java.util.Collection;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Пользователи")
@PermitAll
public class UsersListPresenter extends VerticalLayout {
    private final String SUBSCRIPTIONS_PAGE = "subscriptions";
    private final UserRepository userRepository;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Button subscriptionsButton = new Button("Подписки", VaadinIcon.USER.create());
    private HorizontalLayout toolbar;
    private final UserEditorPresenter userEditorPresenter;

    private Grid<User> grid;

    @Autowired
    public UsersListPresenter(UserRepository userRepository, UserEditorPresenter userEditorPresenter) {
        this.userEditorPresenter = userEditorPresenter;
        this.userRepository = userRepository;
        toolbar = new HorizontalLayout();
        grid = new Grid<>(User.class);

        setUsersGrid(grid);
        UsersFilterService usersFilterService = new UsersFilterService(grid, userRepository);

        toolbar.add(filter, subscriptionsButton);

        add(toolbar, grid, userEditorPresenter);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> usersFilterService.showUser(e.getValue()));


        grid.asSingleSelect().addValueChangeListener(e -> {
            userEditorPresenter.setEditingUser(e.getValue());
        });


        subscriptionsButton.addClickListener(e -> PagesNavigator.navigateTo(SUBSCRIPTIONS_PAGE));

        /*editor.setChangeHandler(() -> {
            editor.setVisible(false);
            usersFilterService.showUser(filter.getValue());
        });*/

        grid.setItems((Collection<User>) userRepository.findAll());
    }


    private void setUsersGrid(Grid<User> grid) {
        grid.setHeight("400px");
        grid.setWidth("900px");
        grid.setColumns();

        grid.addColumn(User::getName).setHeader("Имя").setWidth("540px");
        grid.addColumn(User::getGender).setHeader("Пол").setWidth("90px");
        grid.addColumn(user -> user.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номер телефона").setWidth("270px");

        grid.getElement().getStyle().set("margin-left", "auto");
        grid.getElement().getStyle().set("margin-right", "auto");
    }
}
