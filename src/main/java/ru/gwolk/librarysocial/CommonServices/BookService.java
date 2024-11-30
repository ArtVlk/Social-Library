package ru.gwolk.librarysocial.CommonServices;

import com.vaadin.flow.component.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gwolk.librarysocial.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.Entities.Book;
import ru.gwolk.librarysocial.SocialServices.CurrentUserService;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final CurrentUserService currentUserService;
    private Grid<Book> booksGrid;

    @Autowired
    public BookService(BookRepository bookRepository, UserBookRepository userBookRepository,
                       CurrentUserService currentUserService) {
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.currentUserService = currentUserService;
    }


    public void updateGrid(Grid<Book> booksGrid) {
        booksGrid.setItems((Collection<Book>) bookRepository.findAll());
    }


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
