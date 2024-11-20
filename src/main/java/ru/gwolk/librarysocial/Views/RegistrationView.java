package ru.gwolk.librarysocial.Views;

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
    @Autowired
    public RegistrationView(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        RegistrationForm registrationForm = new RegistrationForm();
        // Center the RegistrationForm
        setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

        add(registrationForm);

        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm, userDetailsService);
        registrationFormBinder.addBindingAndValidation();
    }

}
