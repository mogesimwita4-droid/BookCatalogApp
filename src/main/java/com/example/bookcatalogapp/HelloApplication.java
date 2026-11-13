package com.example.bookcatalogapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 680);
        stage.setTitle("Book Catalog");
        stage.setMinWidth(760);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }
}
