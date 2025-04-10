package lk.ijse.gdse.simplechatapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientPageController {

    @FXML
    private Button btnclientSend;

    @FXML
    private AnchorPane clientPage;

    @FXML
    private TextArea clientTextArea;

    @FXML
    private TextField clientTextField;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 1234);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String message;
                while ((message = dataInputStream.readUTF()) != null) {
                    String finalMessage = message;
                    javafx.application.Platform.runLater(() ->
                            clientTextArea.appendText("Server: " + finalMessage + "\n")
                    );
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void btnclientSendOnAction(ActionEvent event) {
        try {
            String message = clientTextField.getText();
            dataOutputStream.writeUTF(message);
            clientTextArea.appendText("Client: " + message + "\n");
            clientTextField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
