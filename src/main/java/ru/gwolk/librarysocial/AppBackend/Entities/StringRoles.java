package ru.gwolk.librarysocial.AppBackend.Entities;

/**
 * Класс {@link StringRoles} предоставляет строки, которые представляют различные роли пользователей в системе.
 *
 * Этот класс используется для хранения строковых значений, соответствующих ролям пользователей, таких как
 * "USER" для обычных пользователей и "ADMIN" для администраторов системы.
 * Класс содержит только статические поля, и не предназначен для создания объектов.
 */
public class StringRoles {
    private StringRoles() {
    }

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
}
