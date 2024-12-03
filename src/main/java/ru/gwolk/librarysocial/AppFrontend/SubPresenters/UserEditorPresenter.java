package ru.gwolk.librarysocial.AppFrontend.SubPresenters;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.CustomNotification;
import ru.gwolk.librarysocial.AppBackend.Entities.StringRoles;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.UserEditorService;

@SpringComponent
@UIScope
public class UserEditorPresenter extends VerticalLayout implements KeyNotifier {
    private final UserRepository userRepository;
    private User editingUser;
    private TextField name = new TextField("Имя");
    private Button subscribeButton = new Button("Подписаться", VaadinIcon.PLUS.create());
    private TextField number = new TextField("Номер телефона");
    private TextField gender = new TextField("Пол");
    private Button lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
    private Button banUserButton = new Button("Забанить", VaadinIcon.BAN.create());
    private Binder<User> binder = new Binder<>(User.class);




    @Autowired
    public UserEditorPresenter(UserRepository userRepository,
                               AuthenticationContext authenticationContext,
                               UserEditorService userEditorService) {
        this.userRepository = userRepository;

        name.setReadOnly(true);
        number.setReadOnly(true);
        gender.setReadOnly(true);
        banUserButton.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.ERROR);

        setHorizontalComponentAlignment(Alignment.CENTER, name, number, gender, subscribeButton, lookFavouritesButton,
                banUserButton);

        addComponentsByRoles(authenticationContext);

        binder.bindInstanceFields(this);

        setSpacing(true);

        subscribeButton.addClickListener(e -> {
            if (userEditorService.subscribe(editingUser)) {
                CustomNotification.showNotification("✓", NotificationVariant.LUMO_SUCCESS);
            }
            else {
                CustomNotification.showNotification("Вы уже подписаны на этого пользователя",
                        NotificationVariant.LUMO_WARNING);
            }
        });
        banUserButton.addClickListener(e -> {
            userEditorService.ban(editingUser);
            CustomNotification.showNotification("Забанен! \uD83D\uDE08", NotificationVariant.LUMO_ERROR);
        });
        setVisible(false);
    }

    private void addComponentsByRoles(AuthenticationContext authenticationContext) {
        if (authenticationContext.hasRole(StringRoles.ADMIN)) {
            add(name, number, gender, subscribeButton, lookFavouritesButton, banUserButton);
        }
        else {
            add(name, number, gender, subscribeButton, lookFavouritesButton);
        }
    }


    public void setEditingUser(User newUser) {
        if (newUser == null) {
            setVisible(true);
            return;
        }

        if (newUser.getId() != null) {
            this.editingUser = userRepository.findById(newUser.getId()).orElse(newUser);
        }
        else {
            this.editingUser = newUser;
        }

        number.setValue(editingUser.getPhoneNumber() == null ? "" : editingUser.getPhoneNumber().getNumber());

        binder.setBean(editingUser);
        setVisible(true);
    }



}
