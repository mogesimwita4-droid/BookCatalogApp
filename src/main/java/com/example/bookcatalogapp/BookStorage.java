package com.example.bookcatalogapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

final class BookStorage {
    private static final Path FILE = Path.of(System.getProperty("user.home"), ".bookcatalog", "books.csv");

    private BookStorage() { }

    static List<Book> load() throws IOException {
        List<Book> books = new ArrayList<>();
        if (!Files.exists(FILE)) return books;
        int lineNumber = 0;
        for (String line : Files.readAllLines(FILE, StandardCharsets.UTF_8)) {
            lineNumber++;
            if (line.isBlank()) continue;
            List<String> fields = parseLine(line);
            if (fields.size() != 4) throw new IOException("Invalid catalog data on line " + lineNumber + ".");
            try {
                books.add(new Book(fields.get(0), fields.get(1), fields.get(2), Integer.parseInt(fields.get(3))));
            } catch (NumberFormatException exception) {
                throw new IOException("Invalid publication year on line " + lineNumber + ".", exception);
            }
        }
        return books;
    }

    static void save(List<Book> books) throws IOException {
        Files.createDirectories(FILE.getParent());
        Path temporaryFile = Files.createTempFile(FILE.getParent(), "books-", ".tmp");
        try {
            List<String> lines = new ArrayList<>();
            for (Book book : books) {
                lines.add(csv(book.getTitle()) + "," + csv(book.getAuthor()) + "," + csv(book.getGenre()) + "," + book.getYear());
            }
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private static String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static List<String> parseLine(String line) throws IOException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (quoted && character == '\"' && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                field.append('\"');
                i++;
            } else if (character == '\"') {
                quoted = !quoted;
            } else if (character == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (quoted) throw new IOException("Unclosed quoted value in catalog data.");
        fields.add(field.toString());
        return fields;
    }
}
