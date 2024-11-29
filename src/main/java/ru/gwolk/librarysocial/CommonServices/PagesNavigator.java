package ru.gwolk.librarysocial.CommonServices;

import com.vaadin.flow.component.UI;

public class PagesNavigator {
    public static void navigateTo(String page) {
        UI.getCurrent().navigate(page);
    }
}
