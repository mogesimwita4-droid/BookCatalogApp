package com.example.bookcatalogapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class HelloController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private TextField searchField;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private Label statusLabel;

    private final ObservableList<Book> books = FXCollections.observableArrayList();
    private boolean storageReady = true;

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        FilteredList<Book> filteredBooks = new FilteredList<>(books, book -> true);
        booksTable.setItems(filteredBooks);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = newValue == null ? "" : newValue.trim().toLowerCase();
            filteredBooks.setPredicate(book -> query.isEmpty()
                    || book.getTitle().toLowerCase().contains(query)
                    || book.getAuthor().toLowerCase().contains(query)
                    || book.getGenre().toLowerCase().contains(query)
                    || String.valueOf(book.getYear()).contains(query));
        });
        updateStatus();
        try {
            books.addAll(BookStorage.load());
            updateStatus();
        } catch (IOException exception) {
            storageReady = false;
            statusLabel.setText("Could not load your saved catalog: " + exception.getMessage());
        }
    }

    @FXML
    private void onAddBook() {
        if (!storageReady) {
            statusLabel.setText("Your saved catalog could not be loaded. Fix the catalog file before making changes.");
            return;
        }
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = genreField.getText().trim();
        String yearText = yearField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
            statusLabel.setText("Fill in all four fields to add a book.");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
            if (year < 0 || year > 9999) throw new NumberFormatException();
        } catch (NumberFormatException exception) {
            statusLabel.setText("Enter a valid year (for example, 2024).");
            yearField.requestFocus();
            return;
        }

        books.add(new Book(title, author, genre, year));
        if (!saveBooks()) return;
        clearFields();
        updateStatus();
        statusLabel.setText("Added “" + title + "” to your catalog.");
    }

    @FXML
    private void onRemoveBook() {
        if (!storageReady) {
            statusLabel.setText("Your saved catalog could not be loaded. Fix the catalog file before making changes.");
            return;
        }
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            statusLabel.setText("Select a book in the table before removing it.");
            return;
        }
        books.remove(selectedBook);
        if (!saveBooks()) return;
        updateStatus();
        statusLabel.setText("Removed “" + selectedBook.getTitle() + "” from your catalog.");
    }

    @FXML
    private void onClearFields() {
        clearFields();
        statusLabel.setText("The entry fields are clear.");
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        genreField.clear();
        yearField.clear();
        titleField.requestFocus();
    }

    private void updateStatus() {
        int count = books.size();
        statusLabel.setText(count + (count == 1 ? " book in your catalog." : " books in your catalog."));
    }

    private boolean saveBooks() {
        try {
            BookStorage.save(books);
            return true;
        } catch (IOException exception) {
            statusLabel.setText("Could not save your catalog: " + exception.getMessage());
            return false;
        }
    }
}
