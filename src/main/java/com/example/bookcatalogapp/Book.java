package com.example.bookcatalogapp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty title = new SimpleStringProperty(this, "title");
    private final StringProperty author = new SimpleStringProperty(this, "author");
    private final StringProperty genre = new SimpleStringProperty(this, "genre");
    private final IntegerProperty year = new SimpleIntegerProperty(this, "year");

    public Book(String title, String author, String genre, int year) {
        this.title.set(title);
        this.author.set(author);
        this.genre.set(genre);
        this.year.set(year);
    }

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }
    public String getAuthor() { return author.get(); }
    public StringProperty authorProperty() { return author; }
    public String getGenre() { return genre.get(); }
    public StringProperty genreProperty() { return genre; }
    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }
}
