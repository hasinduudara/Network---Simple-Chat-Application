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
import java.net.ServerSocket;
import java.net.Socket;

public class ServerPageController {

    @FXML
    private AnchorPane ServerPage;

    @FXML
    private Button btnserverSend;

    @FXML
    private TextArea serverTextArea;

    @FXML
    private TextField serverTextField;

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1234);
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String message;
                while ((message = dataInputStream.readUTF()) != null) {
                    String finalMessage = message;
                    javafx.application.Platform.runLater(() ->
                            serverTextArea.appendText("Client: " + finalMessage + "\n")
                    );
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void btnserverSendOnAction(ActionEvent event) {
        try {
            String message = serverTextField.getText();
            dataOutputStream.writeUTF(message);
            serverTextArea.appendText("Server: " + message + "\n");
            serverTextField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
