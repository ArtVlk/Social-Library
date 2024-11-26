package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.constraints.NotBlank;

import java.util.stream.Stream;

public class RegistrationForm extends FormLayout {

    @NotBlank
    private TextField name;

    private PasswordField password;
    private PasswordField passwordConfirm;
    private TextField phoneNumber;
    private TextField gender;
    private TextField country;
    private TextField address;

    private Span errorMessageField;

    private Button submitButton;


    public RegistrationForm() {
        name = new TextField("Имя");
        password = new PasswordField("Пароль");
        passwordConfirm = new PasswordField("Повторный ввод пароля");
        phoneNumber = new TextField("Номер телефона");
        gender = new TextField("Пол (M или W)");
        country = new TextField("Страна (Название, код)");
        address = new TextField("Адрес (Город, улица, номер дома)");


        setRequiredIndicatorVisible(name,  password, passwordConfirm, phoneNumber);

        errorMessageField = new Span();

        submitButton = new Button("Регистрация");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        phoneNumber.setPlaceholder("+7");

        add(name, password, passwordConfirm, phoneNumber, gender, country, address, errorMessageField, submitButton);

        // Max width of the Form
        setMaxWidth("500px");

        // Allow the form layout to be responsive.
        // On device widths 0-490px we have one column.
        // Otherwise, we have two columns.
        /*setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

        // These components always take full width
        setColspan(title, 2);
        setColspan(email, 2);
        setColspan(errorMessageField, 2);
        setColspan(submitButton, 2);*/
    }

    public PasswordField getPasswordField() { return password; }

    public PasswordField getPasswordConfirmField() { return passwordConfirm; }

    public Span getErrorMessageField() { return errorMessageField; }

    public Button getSubmitButton() { return submitButton; }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }
}
