package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Role;
import ru.gwolk.librarysocial.Entities.StringRoles;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.Utilities.RoleUtils;
import ru.gwolk.librarysocial.Widgets.MainLayout;
import ru.gwolk.librarysocial.Widgets.UserEditor;

import java.util.Collection;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Пользователи")
@PermitAll
public class UsersListView extends VerticalLayout {
    private final UserRepository userRepository;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Button subscriptionsButton = new Button("Подписки", VaadinIcon.USER.create());
    private HorizontalLayout toolbar;
    private final UserEditor userEditor;

    private Grid<User> grid;

    @Autowired
    public UsersListView(UserRepository userRepository, UserEditor editor, AuthenticationContext authenticationContext) {
        this.userRepository = userRepository;
        this.userEditor = editor;
        toolbar = new HorizontalLayout();
        grid = new Grid<>(User.class);

        setUsersGrid(grid);

        toolbar.add(filter, subscriptionsButton);

        add(toolbar, grid, userEditor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showUser(e.getValue()));


        grid.asSingleSelect().addValueChangeListener(e -> {
            userEditor.editUser(e.getValue());
        });


        subscriptionsButton.addClickListener(e -> {
            UI.getCurrent().navigate("subscriptions");
        });

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            UsersListView.this.showUser(filter.getValue());
        });

        grid.setItems((Collection<User>) userRepository.findAll());
    }


    private void showUser(String name) {
        if (name.isEmpty()) {
            grid.setItems((Collection<User>) userRepository.findAll());
        }
        else {
            grid.setItems(userRepository.findByName(name));
        }
    }

    private void setUsersGrid(Grid<User> grid) {
        grid.setHeight("300px");
        grid.setWidth("900px");
        grid.setColumns();

        grid.addColumn(User::getName).setHeader("Имя").setWidth("540px");
        grid.addColumn(User::getGender).setHeader("Пол").setWidth("90px");
        grid.addColumn(user -> user.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");

        grid.getElement().getStyle().set("margin-left", "auto");
        grid.getElement().getStyle().set("margin-right", "auto");
    }
}
