package ru.gwolk.librarysocial.Utilities;

import ru.gwolk.librarysocial.Entities.Role;

public class RoleUtils {
    public static String[] getRolesAsString(Role role) {
        return new String[] { role.name() };
    }
}
