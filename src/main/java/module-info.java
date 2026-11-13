module com.example.bookcatalogapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.bookcatalogapp to javafx.fxml;
    exports com.example.bookcatalogapp;
}
