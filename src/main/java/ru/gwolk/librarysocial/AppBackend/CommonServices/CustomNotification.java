package ru.gwolk.librarysocial.AppBackend.CommonServices;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class CustomNotification {
    public static void showNotification(String message, NotificationVariant variant) {
        Notification.show(message).addThemeVariants(variant);
    }
}
