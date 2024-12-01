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
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.UserEditorPresenter;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.MainLayout;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.PagesNavigator;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.UserEditorService;
import ru.gwolk.librarysocial.AppBackend.SocialServices.UsersFilterService;

import java.util.Collection;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Пользователи")
@PermitAll
public class UsersListPresenter extends VerticalLayout {
    private final String SUBSCRIPTIONS_PAGE = "subscriptions";
    private final UserRepository userRepository;
    private final TextField filter = new TextField("", "Нажмите на фильтр");
    private final Button subscriptionsButton = new Button("Подписки", VaadinIcon.USER.create());
    private HorizontalLayout toolbar;

    private Grid<User> usersGrid;

    @Autowired
    public UsersListPresenter(UserRepository userRepository,
                              UserEditorPresenter userEditorPresenter,
                              UserEditorService userEditorService) {
        this.userRepository = userRepository;
        toolbar = new HorizontalLayout();
        usersGrid = userEditorService.getUsersGrid();

        UsersFilterService usersFilterService = new UsersFilterService(usersGrid, userRepository);

        toolbar.add(filter, subscriptionsButton);

        add(toolbar, usersGrid, userEditorPresenter);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> usersFilterService.showUser(e.getValue()));


        usersGrid.asSingleSelect().addValueChangeListener(e -> {
            userEditorPresenter.setEditingUser(e.getValue());
        });


        subscriptionsButton.addClickListener(e -> PagesNavigator.navigateTo(SUBSCRIPTIONS_PAGE));

        usersGrid.setItems((Collection<User>) userRepository.findAll());
    }

}
