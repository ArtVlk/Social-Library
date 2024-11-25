package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Services.CurrentUserService;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {
    private final UserRepository userRepository;
    private User user;
    private TextField name = new TextField("Имя");
    private Button subscribeButton = new Button("Подписаться", VaadinIcon.PLUS.create());
    private TextField gender = new TextField("Пол");
    private Button lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
    private Binder<User> binder = new Binder<>(User.class);
    private ChangeHandler changeHandler;
    private CurrentUserService currentUserService;
    private SubscriptionsRepository subscriptionsRepository;


    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public UserEditor(UserRepository userRepository,SubscriptionsRepository subscriptionsRepository,
                      CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.currentUserService = currentUserService;
        name.setReadOnly(true);
        gender.setReadOnly(true);

        setHorizontalComponentAlignment(Alignment.CENTER, name, gender, subscribeButton, lookFavouritesButton);

        add(name, gender, subscribeButton, lookFavouritesButton);

        binder.bindInstanceFields(this);

        setSpacing(true);

        subscribeButton.addClickListener(e -> subscribe());
        setVisible(false);
    }

    private void subscribe() {
        User iUser = currentUserService.getCurrentUser();
        Subscription subscription = new Subscription(iUser, user);
        subscriptionsRepository.save(subscription);
        changeHandler.onChange();
    }

    public void editUser(User newUser) {
        if (newUser == null) {
            setVisible(true);
            return;
        }

        if (newUser.getId() != null) {
            this.user = userRepository.findById(newUser.getId()).orElse(newUser);
        }
        else {
            this.user = newUser;
        }

        binder.setBean(user);
        setVisible(true);
    }

}
