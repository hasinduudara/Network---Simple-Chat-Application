module lk.ijse.gdse.simplechatapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.gdse.simplechatapplication to javafx.fxml;
    exports lk.ijse.gdse.simplechatapplication;
    exports lk.ijse.gdse.simplechatapplication.controller;
    opens lk.ijse.gdse.simplechatapplication.controller to javafx.fxml;
}