package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Войти - SocialLibrary")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginForm loginForm;
    public LoginView() {
        loginForm = new LoginForm();
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");

        add(new H1("Войти в Social Library"),
                new Div("Если вы не зарегистрированы - нажмите Зарегистриоваться"),
                loginForm);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
