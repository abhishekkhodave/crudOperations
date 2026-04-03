package com.api.book.bootrestbook.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.book.bootrestbook.entities.Book;
import com.api.book.bootrestbook.services.BookService;

@RestController
@RequestMapping("/books") // base path for all book endpoints
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ---------------------------------------------------------------------
    // GET /books  -> list all
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> list = bookService.getAllBooks();
        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    // ---------------------------------------------------------------------
    // GET /books/{id}  -> get one by id
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok) // 200 with body
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404
    }

    // ---------------------------------------------------------------------
    // POST /books  -> create new
    // Returns 201 Created + Location header pointing to /books/{newId}
    // ---------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // You can add @Valid and a DTO if you need validation
        Book created = bookService.addBook(book);
        // Build a Location header like /books/{id}
        URI location = URI.create("/books/" + created.getId());
        return ResponseEntity.created(location).body(created); // 201
    }

    // ---------------------------------------------------------------------
    // PUT /books/{id}  -> full update (replace/upsert by id)
    // Returns 200 OK with updated entity (or 404 if you want strict update)
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {
        // If you want strict update (only when resource exists), check first:
        return bookService.getBookById(id)
                .map(existing -> {
                    Book updated = bookService.updateBook(book, id);
                    return ResponseEntity.ok(updated); // 200
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404
    }

    // ---------------------------------------------------------------------
    // PATCH /books/{id}  -> partial update (optional)
    // Example merges provided fields; implement merging logic as needed.
    // ---------------------------------------------------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable int id, @RequestBody Book partial) {
        return bookService.getBookById(id)
                .map(existing -> {
                    // Example merge (only set fields that are non-null in `partial`)
                    if (partial.getTitle() != null) existing.setTitle(partial.getTitle());
                    if (partial.getAuthor() != null) existing.setAuthor(partial.getAuthor());
                    // ... add more fields as needed
                    Book saved = bookService.updateBook(existing, id);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ---------------------------------------------------------------------
    // DELETE /books/{id}  -> delete by id
    // Returns 204 No Content (idempotent; ok if it didn't exist)
    // If you want 404 when not found, check existence first.
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        // Option A (idempotent, always 204):
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();

        // Option B (strict, return 404 if not found):
        // return bookService.getBookById(id)
        //         .map(b -> {
        //             bookService.deleteBook(id);
        //             return ResponseEntity.noContent().build();
        //         })
        //         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
