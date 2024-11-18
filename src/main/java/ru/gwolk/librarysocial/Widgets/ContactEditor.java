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
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.ContactRepository;
import ru.gwolk.librarysocial.Entities.Contact;

@SpringComponent
@UIScope
public class ContactEditor extends VerticalLayout implements KeyNotifier {
    private final ContactRepository contactRepository;
    private Contact contact;
    private TextField name = new TextField("Имя");
    private Button save = new Button("Сохранить", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Отмена");
    private Button delete = new Button("Удалить", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private Binder<Contact> binder = new Binder<>(Contact.class);
    private ChangeHandler changeHandler;

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public ContactEditor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;

        add(name, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editContact(contact));
        setVisible(false);
    }

    public void editContact(Contact newContact) {
        if (newContact == null) {
            setVisible(true);
            return;
        }

        if (newContact.getId() != null) {
            this.contact = contactRepository.findById(newContact.getId()).orElse(newContact);
        }
        else {
            this.contact = newContact;
        }

        binder.setBean(contact);

        setVisible(true);
    }

    private void delete() {
        contactRepository.delete(contact);
        changeHandler.onChange();
    }

    private void save() {
        contactRepository.save(contact);
        changeHandler.onChange();
    }

}
