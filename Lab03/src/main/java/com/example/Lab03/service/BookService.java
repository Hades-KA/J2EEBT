package com.example.lab03.service;

import com.example.lab03.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();
    private long nextId = 1;

    public BookService() {
        // dữ liệu mẫu (giống slide minh hoạ)
        addBook(new Book(null, "Spring boot", "Huy Cuong"));
        addBook(new Book(null, "Spring Boot V2", "Anh"));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public void addBook(Book book) {
        book.setId(nextId++);
        books.add(book);
    }

    public void updateBook(Book updatedBook) {
        getBookById(updatedBook.getId()).ifPresent(b -> {
            b.setTitle(updatedBook.getTitle());
            b.setAuthor(updatedBook.getAuthor());
        });
    }

    public void deleteBook(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}