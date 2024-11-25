package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;

@SpringComponent
@UIScope
public class SubscriptionUserEditor extends VerticalLayout {
    private SubscriptionsRepository subscriptionsRepository;
    private User subscibedUser;
    @Autowired
    public SubscriptionUserEditor(SubscriptionsRepository subscriptionsRepository) {
        this.subscriptionsRepository = subscriptionsRepository;
    }

}
