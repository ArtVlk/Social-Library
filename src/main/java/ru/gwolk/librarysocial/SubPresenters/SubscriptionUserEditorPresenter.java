package ru.gwolk.librarysocial.SubPresenters;

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
import ru.gwolk.librarysocial.Presenters.SubscriptionsPresenter;
import ru.gwolk.librarysocial.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.SocialServices.SubscriptionsService;
import ru.gwolk.librarysocial.SocialServices.UserEditorService;

@SpringComponent
@UIScope
public class SubscriptionUserEditorPresenter extends VerticalLayout {
    private SubscriptionsRepository subscriptionsRepository;
    private UserRepository userRepository;
    private User subscribedUser;
    private User me;
    private TextField name;
    private Button lookFavouritesButton;
    private Button unsubscibeButton;
    private Binder<User> binder;
    private CurrentUserService currentUserService;
    private ChangeHandler changeHandler;
    private SubscriptionsService subscriptionsService;


    @Autowired
    public SubscriptionUserEditorPresenter(SubscriptionsService subscriptionsService,
                                           SubscriptionsRepository subscriptionsRepository,
                                           UserRepository userRepository,
                                           CurrentUserService currentUserService) {
        this.subscriptionsService = subscriptionsService;
        this.subscriptionsRepository = subscriptionsRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;

        name = new TextField("Имя");
        name.setReadOnly(true);

        lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
        unsubscibeButton = new Button("Отписаться", VaadinIcon.UNLINK.create());

        binder = new Binder<>(User.class);
        binder.bindInstanceFields(this);

        setHorizontalComponentAlignment(Alignment.CENTER, name, lookFavouritesButton, unsubscibeButton);
        add(name, lookFavouritesButton, unsubscibeButton);

        unsubscibeButton.addClickListener(e -> subscriptionsService.unsubscribe(subscribedUser));

        setSpacing(true);
        setVisible(false);
    }


    public void editSubscription(User user) {
        if (user == null) {
            setVisible(true);
            return;
        }

        if (user.getId() != null) {
            this.subscribedUser = userRepository.findById(user.getId()).orElse(user);
        }
        else {
            this.subscribedUser = user;
        }

        binder.setBean(subscribedUser);
        setVisible(true);
    }
    public void setChangeHandler(ChangeHandler changeHandler) {
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

}
