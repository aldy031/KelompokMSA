module org.example.rifaldytamauka {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.rifaldytamauka to javafx.fxml;
    exports org.example.rifaldytamauka;
}