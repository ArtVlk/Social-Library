package ru.gwolk.librarysocial.AppBackend.Utilities;

import ru.gwolk.librarysocial.AppBackend.Entities.Role;

public class RoleUtils {
    public static String[] getRolesAsString(Role role) {
        return new String[] { role.name() };
    }
}
