package com.example.demo;

import java.util.Scanner;

public class Book {
    private int id;
    private String title;
    private String author;
    private double price;

    public Book() {}

    public Book(int id, String title, String author, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPrice(double price) { this.price = price; }

    public void input(Scanner sc) {
        this.id = readInt(sc, "Nhập mã sách: ");

        System.out.print("Nhập tên sách: ");
        this.title = sc.nextLine().trim();

        System.out.print("Nhập tác giả: ");
        this.author = sc.nextLine().trim();

        this.price = readDouble(sc, "Nhập đơn giá: ");
    }

    public void output() {
        String msg = """
                BOOK: id=%d, title="%s", author="%s", price=%.2f
                """.formatted(id, title, author, price);
        System.out.print(msg);
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Sai định dạng số nguyên, nhập lại!");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Sai định dạng số, nhập lại!");
            }
        }
    }
}