package ru.gwolk.librarysocial.AppFrontend.Presenters;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CommonServices.MyUserDetailsService;
import ru.gwolk.librarysocial.AppBackend.CommonServices.RegistrationForm;
import ru.gwolk.librarysocial.AppBackend.CommonServices.RegistrationFormBinder;

@Route("registration")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationPresenter extends VerticalLayout {
    private final MyUserDetailsService userDetailsService;
    private H1 registrationTitle;
    @Autowired
    public RegistrationPresenter(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        registrationTitle = new H1("Регистрация");
        RegistrationForm registrationForm = new RegistrationForm();

        setHorizontalComponentAlignment(Alignment.CENTER, registrationTitle, registrationForm);

        add(registrationTitle, registrationForm);

        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm, userDetailsService);
        registrationFormBinder.addBindingAndValidation();
    }

}
