package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.ContactRepository;
import ru.gwolk.librarysocial.Entities.Contact;
import ru.gwolk.librarysocial.Entities.PhoneNumber;

import java.util.stream.Collectors;

@Route(value = "favourites", layout = MainLayout.class)
@PageTitle("Подписки")
public class FavouritesView extends VerticalLayout {
    private Grid<Contact> favouritesGrid;
    private ContactRepository contactRepository;
    private H1 favouritesTitle;
    @Autowired
    public FavouritesView(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.favouritesGrid = new Grid<>(Contact.class);
        this.favouritesTitle = new H1("Ваши подписки");

        favouritesGrid.setHeight("300px");
        favouritesGrid.setWidth("900px");
        favouritesGrid.setColumns();

        favouritesGrid.addColumn(Contact::getName).setHeader("Имя").setWidth("540px");
        favouritesGrid.addColumn(Contact::getGender).setHeader("Пол").setWidth("90px");
        favouritesGrid.addColumn(contact -> contact.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");

        add(favouritesTitle, favouritesGrid);

        favouritesGrid.setItems(contactRepository.findByGender("M"));

    }
}
