# Book Catalog

A desktop book catalog built with Java and JavaFX. Add books with their title, author, genre, and publication year, then search or remove entries from the table.

## Features

- Add a book to the catalog
- Search by title, author, genre, or year
- Remove a selected book
- Validate required fields and publication year

## Requirements

- JDK 17 or newer
- Maven 3.9 or newer

## Run

From the project directory, run:

```bash
mvn javafx:run
```

Books are saved locally in `~/.bookcatalog/books.csv` and loaded the next time the app starts. The catalog file stays on your computer and should not be committed to GitHub.
