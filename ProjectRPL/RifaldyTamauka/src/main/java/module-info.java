module org.example.rifaldytamauka {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens org.example.rifaldytamauka to javafx.fxml;
    exports org.example.rifaldytamauka;
    exports org.example.rifaldytamauka.data;
    exports org.example.rifaldytamauka.util;
    opens org.example.rifaldytamauka.data to javafx.fxml;
    opens org.example.rifaldytamauka.util to javafx.fxml;
}