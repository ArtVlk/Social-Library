package ru.gwolk.librarysocial.AppBackend.CommonServices;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.PhoneNumberRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserFromRegistration;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.RegistrationForm;

/**
 * Класс для связывания и валидации формы регистрации пользователя.
 * Обрабатывает привязку полей формы, валидацию пароля и сохранение данных в базу.
 */
public class RegistrationFormBinder {
    private final RegistrationForm registrationForm;

    private final MyUserDetailsService userDetailsService;
    private boolean enablePasswordValidation;

    /**
     * Конструктор для инициализации объекта.
     *
     * @param registrationForm форма регистрации
     * @param userDetailsService сервис для работы с пользователями
     */
    public RegistrationFormBinder(RegistrationForm registrationForm,
                                  MyUserDetailsService userDetailsService) {
        this.registrationForm = registrationForm;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Добавляет привязку и валидацию для всех полей формы регистрации.
     */
    public void addBindingAndValidation() {
        BeanValidationBinder<UserFromRegistration> binder = new BeanValidationBinder<>(UserFromRegistration.class);
        binder.bindInstanceFields(registrationForm);

        binder.forField(registrationForm.getPasswordField())
                .withValidator(this::passwordValidator).bind("password");

        registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
            enablePasswordValidation = true;

            binder.validate();
        });

        binder.setStatusLabel(registrationForm.getErrorMessageField());

        registrationForm.getSubmitButton().addClickListener(event -> {
            try {
                UserFromRegistration userBean = new UserFromRegistration();

                binder.writeBean(userBean);

                boolean isSaved = saveUserAndPhoneNumberToDB(userBean, userDetailsService);
                if (!isSaved)
                    Notification.show("Пользователь с таким именем уже существует!");
                else
                    showSuccess(userBean);
            } catch (ValidationException exception) {
            }
        });
    }

    private boolean saveUserAndPhoneNumberToDB(UserFromRegistration userBean, MyUserDetailsService userDetailsService) {
        User userToDb = convertRegistrationUserToUser(userBean);
        return userDetailsService.addUser(userToDb);
    }

    private User convertRegistrationUserToUser(UserFromRegistration userBean) {
        return new User(
                userBean.getName(),
                userBean.getPassword(),
                userBean.getPhoneNumber(),
                userBean.getCountry(),
                userBean.getGender(),
                userBean.getAddress()
        );
    }

    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {
        if (pass1 == null) {
            return ValidationResult.error("Пароль не должен быть пустым");
        }

        if (!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = registrationForm.getPasswordConfirmField().getValue();

        if (pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Пароли не совпадают");
    }

    private void showSuccess(UserFromRegistration userBean) {
        Notification notification =
                Notification.show("Регистрация успешна!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().navigate("login");
    }
}
