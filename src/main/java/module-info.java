module com.example.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.project2 to javafx.fxml;
    exports com.example.project2;
    exports com.example.project2.Controller;
    opens com.example.project2.Controller to javafx.fxml;
}