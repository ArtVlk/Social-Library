package ru.gwolk.librarysocial.AppBackend.LibraryServices;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервис для работы с книгами.
 * <p>
 * Обеспечивает операции для управления книгами, обновления интерфейса и удаления связанных данных.
 * </p>
 */
@Service
@UIScope
public class BookService {
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final CurrentUserService currentUserService;
    private Grid<Book> booksGrid;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param bookRepository      репозиторий для работы с книгами
     * @param userBookRepository  репозиторий для работы с пользовательскими книгами
     * @param currentUserService  сервис для получения информации о текущем пользователе
     */
    @Autowired
    public BookService(BookRepository bookRepository, UserBookRepository userBookRepository,
                       CurrentUserService currentUserService) {
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Обновляет данные в предоставленной таблице книг.
     *
     * @param booksGrid таблица книг для обновления
     */
    public void updateGrid(Grid<Book> booksGrid) {
        booksGrid.setItems((Collection<Book>) bookRepository.findAll());
    }

    /**
     * Удаляет книгу и все связанные с ней записи из базы данных.
     *
     * @param bookId идентификатор книги, которую нужно удалить
     */
    @Transactional
    public void deleteBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            System.out.println("Ошибка: Книга с ID " + bookId + " не найдена");
            return;
        }
        Book book = bookOptional.get();

        System.out.println("Удаляем книгу: " + book.getName());

        userBookRepository.deleteByBookId(book.getId());
        System.out.println("Удалены все связанные записи из UserBook для книги с ID " + book.getId());

        bookRepository.delete(book);
        System.out.println("Книга с ID " + book.getId() + " успешно удалена");
    }
}
