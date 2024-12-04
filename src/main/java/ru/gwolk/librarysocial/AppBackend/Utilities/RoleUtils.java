package ru.gwolk.librarysocial.AppBackend.Utilities;

import ru.gwolk.librarysocial.AppBackend.Entities.Role;

/**
 * Утилитный класс для работы с ролями.
 * Предоставляет методы для преобразования роли в строковый массив.
 */
public class RoleUtils {
    /**
     * Преобразует роль в массив строк, содержащий название роли.
     *
     * @param role роль, которую необходимо преобразовать
     * @return массив строк, содержащий название роли
     */
    public static String[] getRolesAsString(Role role) {
        return new String[] { role.name() };
    }
}
