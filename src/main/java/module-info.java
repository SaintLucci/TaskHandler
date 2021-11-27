module com.example.taskhandler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.taskhandler to javafx.fxml;
    exports com.example.taskhandler;
}