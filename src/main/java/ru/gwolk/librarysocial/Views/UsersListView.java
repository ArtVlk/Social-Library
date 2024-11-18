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
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.ContactRepository;
import ru.gwolk.librarysocial.Entities.Contact;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.Widgets.ContactEditor;

import java.util.Collection;
import java.util.stream.Collectors;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Пользователи")
public class UsersListView extends VerticalLayout {
    private final ContactRepository contactRepository;
    private final TextField filter = new TextField("", "Нажмите на фильтр");

    private final Button addNewBtn = new Button("Добавить", VaadinIcon.PLUS.create());
    private final Button favouritesButton = new Button("Подписки", VaadinIcon.USER.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn, favouritesButton);
    private final ContactEditor contactEditor;

    private Grid<Contact> grid = new Grid<>(Contact.class);

    @Autowired
    public UsersListView(ContactRepository contactRepository, ContactEditor editor) {
        this.contactRepository = contactRepository;
        this.contactEditor = editor;

        grid.setHeight("300px");
        grid.setWidth("900px");
        grid.setColumns();

        grid.addColumn(Contact::getName).setHeader("Имя").setWidth("540px");
        grid.addColumn(Contact::getGender).setHeader("Пол").setWidth("90px");
        grid.addColumn(contact -> contact.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");

        grid.getElement().getStyle().set("margin-left", "auto");
        grid.getElement().getStyle().set("margin-right", "auto");

        add(toolbar, grid, contactEditor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showContact(e.getValue()));


        grid.asSingleSelect().addValueChangeListener(e -> {
            contactEditor.editContact(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editContact(new Contact()));
        favouritesButton.addClickListener(e -> {
            UI.getCurrent().navigate("favourites");
        });

        editor.setChangeHandler(new ContactEditor.ChangeHandler() {
            @Override
            public void onChange() {
                editor.setVisible(false);
                UsersListView.this.showContact(filter.getValue());
            }
        });

        grid.setItems((Collection<Contact>) contactRepository.findAll());
    }
    private void showContact(String name) {
        if (name.isEmpty()) {
            grid.setItems((Collection<Contact>) contactRepository.findAll());
        }
        else {
            grid.setItems(contactRepository.findByName(name));
        }
    }
}
