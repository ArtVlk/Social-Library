package ru.gwolk.librarysocial.AppBackend.Entities;

/**
 * Перечисление {@link Role} представляет различные роли пользователей в системе.
 *
 * В данной системе предусмотрены две основные роли:
 * - {@link Role#ADMIN}: Администратор системы, обладающий расширенными правами.
 * - {@link Role#USER}: Обычный пользователь с ограниченными правами.
 */
public enum Role {
    ADMIN,
    USER
}
