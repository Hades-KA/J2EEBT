package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            List<Book> listBook = new ArrayList<>();

            // Fix tiếng Việt khi nhập (UTF-8)
            Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

            String menu = """
                    ===== QUẢN LÝ SÁCH =====
                    1. Thêm 1 cuốn sách
                    2. Xóa 1 cuốn sách
                    3. Thay đổi cuốn sách
                    4. Xuất thông tin tất cả sách
                    5. Tìm sách có tựa chứa "Lập trình" (không phân biệt hoa thường)
                    6. Nhập K và P: lấy tối đa K cuốn có giá <= P
                    7. Nhập danh sách tác giả: in ra sách của các tác giả đó
                    0. Thoát
                    Chọn:
                    """;

            int ch;
            do {
                ch = readInt(sc, menu);

                switch (ch) {
                    case 1 -> {
                        Book b = new Book();
                        b.input(sc);

                        boolean exists = listBook.stream().anyMatch(x -> x.getId() == b.getId());
                        if (exists) {
                            System.out.println("Mã sách đã tồn tại!");
                        } else {
                            listBook.add(b);
                            System.out.println("Đã thêm.");
                        }
                    }

                    case 2 -> {
                        int id = readInt(sc, "Nhập mã sách cần xóa: ");

                        Book found = listBook.stream()
                                .filter(x -> x.getId() == id)
                                .findFirst()
                                .orElse(null);

                        if (found == null) {
                            System.out.println("Không tìm thấy.");
                        } else {
                            listBook.remove(found);
                            System.out.println("Đã xóa.");
                        }
                    }

                    case 3 -> {
                        int id = readInt(sc, "Nhập mã sách cần sửa: ");

                        Book found = listBook.stream()
                                .filter(x -> x.getId() == id)
                                .findFirst()
                                .orElse(null);

                        if (found == null) {
                            System.out.println("Không tìm thấy.");
                        } else {
                            System.out.print("Tên mới: ");
                            found.setTitle(sc.nextLine().trim());

                            System.out.print("Tác giả mới: ");
                            found.setAuthor(sc.nextLine().trim());

                            found.setPrice(readDouble(sc, "Giá mới: "));
                            System.out.println("Đã cập nhật.");
                        }
                    }

                    case 4 -> {
                        if (listBook.isEmpty()) System.out.println("Danh sách rỗng.");
                        else listBook.forEach(Book::output); // method reference
                    }

                    case 5 -> {
                        var rs = listBook.stream()
                                .filter(b -> b.getTitle() != null
                                        && b.getTitle().toLowerCase().contains("lập trình"))
                                .toList();

                        if (rs.isEmpty()) System.out.println("Không có sách phù hợp.");
                        else rs.forEach(Book::output);
                    }

                    case 6 -> {
                        int k = readInt(sc, "Nhập K: ");
                        double p = readDouble(sc, "Nhập P: ");

                        var rs = listBook.stream()
                                .filter(b -> b.getPrice() <= p)
                                .limit(k)
                                .toList();

                        if (rs.isEmpty()) System.out.println("Không có sách phù hợp.");
                        else rs.forEach(Book::output);
                    }

                    case 7 -> {
                        System.out.print("Nhập danh sách tác giả (cách nhau bằng dấu phẩy): ");
                        String line = sc.nextLine();

                        Set<String> authors = Arrays.stream(line.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isBlank())
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());

                        var rs = listBook.stream()
                                .filter(b -> b.getAuthor() != null
                                        && authors.contains(b.getAuthor().trim().toLowerCase()))
                                .toList();

                        if (rs.isEmpty()) System.out.println("Không có sách phù hợp.");
                        else rs.forEach(Book::output);
                    }

                    case 0 -> System.out.println("Thoát!");
                    default -> System.out.println("Chọn sai!");
                }

            } while (ch != 0);

            sc.close();
        };
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