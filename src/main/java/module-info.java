module bikram.techura {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.opencsv;
    requires java.sql;
    requires org.json;

    opens techura to javafx.fxml;
    opens techura.utils to javafx.fxml;
    opens techura.views to javafx.fxml;
    opens techura.models to javafx.fxml;

    exports techura;
    exports techura.utils;
    exports techura.views;
    exports techura.models;
}