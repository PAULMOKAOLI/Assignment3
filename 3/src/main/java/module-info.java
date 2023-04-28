module com.example.assign22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.assign22 to javafx.fxml;
    exports com.example.assign22;
}