package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.StringRoles;
import ru.gwolk.librarysocial.Entities.User;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {
    private final UserRepository userRepository;
    private User user;
    private TextField name = new TextField("Имя");
    private Button save = new Button("Сохранить", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Отмена");
    private Button delete = new Button("Удалить", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private Binder<User> binder = new Binder<>(User.class);
    private ChangeHandler changeHandler;

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public UserEditor(UserRepository userRepository) {
        this.userRepository = userRepository;

        add(name, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editUser(user));
        setVisible(false);
    }

    @RolesAllowed(StringRoles.ADMIN)
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

    private void delete() {
        userRepository.delete(user);
        changeHandler.onChange();
    }

    private void save() {
        userRepository.save(user);
        changeHandler.onChange();
    }

}
