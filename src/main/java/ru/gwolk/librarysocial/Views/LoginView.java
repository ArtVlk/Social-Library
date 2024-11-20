package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private Button regButton;
    public LoginView() {
        loginForm = new LoginForm();
        regButton = new Button("Зарегистрироваться", VaadinIcon.ENTER.create());
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");

        regButton.addClickListener(e -> UI.getCurrent().navigate("registration"));

        add(new H1("Войти в Social Library"),
                new Div("Если вы не зарегистрированы - нажмите Зарегистриоваться"),
                loginForm, regButton);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
