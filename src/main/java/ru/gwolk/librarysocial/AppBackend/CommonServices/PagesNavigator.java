package ru.gwolk.librarysocial.AppBackend.CommonServices;

import com.vaadin.flow.component.UI;

public class PagesNavigator {
    public static void navigateTo(String page) {
        UI.getCurrent().navigate(page);
    }
}
