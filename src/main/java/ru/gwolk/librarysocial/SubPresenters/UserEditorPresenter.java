package ru.gwolk.librarysocial.SubPresenters;

import com.vaadin.flow.component.KeyNotifier;
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
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.SocialServices.UserEditorService;

@SpringComponent
@UIScope
public class UserEditorPresenter extends VerticalLayout implements KeyNotifier {
    private final UserRepository userRepository;
    private User editingUser;
    private TextField name = new TextField("Имя");
    private Button subscribeButton = new Button("Подписаться", VaadinIcon.PLUS.create());
    private TextField gender = new TextField("Пол");
    public Button lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
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
    public UserEditorPresenter(UserRepository userRepository, CurrentUserService currentUserService,
                               SubscriptionsRepository subscriptionsRepository) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;

        UserEditorService userEditorService = new UserEditorService(currentUserService, subscriptionsRepository);

        name.setReadOnly(true);
        gender.setReadOnly(true);

        setHorizontalComponentAlignment(Alignment.CENTER, name, gender, subscribeButton, lookFavouritesButton);

        add(name, gender, subscribeButton, lookFavouritesButton);

        binder.bindInstanceFields(this);

        setSpacing(true);

        subscribeButton.addClickListener(e -> userEditorService.subscribe(editingUser));
        setVisible(false);
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

        binder.setBean(editingUser);
        setVisible(true);
    }

}
