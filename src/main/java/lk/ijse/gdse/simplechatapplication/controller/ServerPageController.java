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

    @FXML
    private ImageView ImageViwe;

    @FXML
    private Button btnSendFile;

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1234);
                socket = serverSocket.accept();
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
                    serverTextArea.appendText("client: " + message + "\n");
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

    @FXML
    void btnSendFileOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getName());
        if (file != null) {
            try {
                byte[] fileBytes = new byte[(int) file.length()];
                dataOutputStream.writeUTF("Image");
                dataOutputStream .writeInt(fileBytes.length);
                dataOutputStream.write(fileBytes);
                dataOutputStream.flush();
                serverTextArea.appendText("Server: Sent file " + file.getName() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
