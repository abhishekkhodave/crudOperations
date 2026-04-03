package com.api.book.bootrestbook.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.book.bootrestbook.dao.BookRepository;
import com.api.book.bootrestbook.entities.Book;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor injection (safer, test-friendly)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // returns List<Book> when using JpaRepository
    }

    // Get book by id
    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id); // Optional<Book>
    }

    // Add book
    public Book addBook(Book b) {
        return bookRepository.save(b);
    }

    // Delete book
    public void deleteBook(int bid) {
        bookRepository.deleteById(bid);
    }

    // Update the book (upsert behavior)
    public Book updateBook(Book book, int bookId) {
        book.setId(bookId);
        return bookRepository.save(book);
    }
}
