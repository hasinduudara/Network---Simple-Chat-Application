package lk.ijse.gdse.simplechatapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ClientPageController {

    @FXML
    private Button btnclientSend;

    @FXML
    private AnchorPane clientPage;

    @FXML
    private TextArea clientTextArea;

    @FXML
    private TextField clientTextField;

    @FXML
    private ImageView ImageViwe;

    @FXML
    private Button btnSendFile;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 1234);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String message = dataInputStream.readUTF();
                    if (message.equals("Image")) {
                        int fileByteLength = dataInputStream.readInt();
                        byte[] fileBytes = new byte[fileByteLength];
                        dataInputStream.readFully(fileBytes);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
                        ImageViwe.setImage(new Image(byteArrayInputStream));
                    }
                    clientTextArea.appendText("server: " + message + "\n");
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

    @FXML
    void btnSendFileOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getName());
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                dataOutputStream.writeUTF("Image");
                dataOutputStream .writeInt(fileBytes.length);
                dataOutputStream.write(fileBytes);
                dataOutputStream.flush();
                clientTextArea.appendText("Client: Sent file " + file.getName() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
