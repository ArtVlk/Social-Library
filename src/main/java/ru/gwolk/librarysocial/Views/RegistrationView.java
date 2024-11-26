package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Services.MyUserDetailsService;
import ru.gwolk.librarysocial.Widgets.RegistrationForm;
import ru.gwolk.librarysocial.Widgets.RegistrationFormBinder;

@Route("registration")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {
    private final MyUserDetailsService userDetailsService;
    private H1 registrationTitle;
    @Autowired
    public RegistrationView(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        registrationTitle = new H1("Регистрация");
        RegistrationForm registrationForm = new RegistrationForm();

        setHorizontalComponentAlignment(Alignment.CENTER, registrationTitle, registrationForm);

        add(registrationTitle, registrationForm);

        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm, userDetailsService);
        registrationFormBinder.addBindingAndValidation();
    }

}
