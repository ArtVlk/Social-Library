package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
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

import java.util.List;

@SpringComponent
@UIScope
public class SubscriptionUserEditor extends VerticalLayout {
    private SubscriptionsRepository subscriptionsRepository;
    private UserRepository userRepository;
    private User subscibedUser;
    private User me;
    private TextField name;
    private Button lookFavouritesButton;
    private Button unsubscibeButton;
    private Binder<User> binder;
    private CurrentUserService currentUserService;
    private ChangeHandler changeHandler;


    @Autowired
    public SubscriptionUserEditor(SubscriptionsRepository subscriptionsRepository, UserRepository userRepository,
                                  CurrentUserService currentUserService) {
        this.subscriptionsRepository = subscriptionsRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        name = new TextField("Имя");
        name.setReadOnly(true);
        lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
        unsubscibeButton = new Button("Отписаться", VaadinIcon.UNLINK.create());
        me = currentUserService.getCurrentUser();

        binder = new Binder<>(User.class);
        binder.bindInstanceFields(this);

        setHorizontalComponentAlignment(Alignment.CENTER, name, lookFavouritesButton, unsubscibeButton);
        add(name, lookFavouritesButton, unsubscibeButton);

        unsubscibeButton.addClickListener(e -> unsubscribe());

        setSpacing(true);
        setVisible(false);
    }

    private void unsubscribe() {
        Subscription subscription = subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(me, subscibedUser);
        subscriptionsRepository.delete(subscription);
        setVisible(false);
        changeHandler.onChange();
    }

    public void editSubscription(User user) {
        if (user == null) {
            setVisible(true);
            return;
        }

        if (user.getId() != null) {
            this.subscibedUser = userRepository.findById(user.getId()).orElse(user);
        }
        else {
            this.subscibedUser = user;
        }

        binder.setBean(subscibedUser);
        setVisible(true);
    }
    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public interface ChangeHandler {
        void onChange();
    }

}
