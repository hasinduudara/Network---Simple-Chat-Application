package lk.ijse.gdse.simplechatapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientInitializer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the welcome page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientPage.fxml"));
        Parent root = loader.load();

        // Set the title and icon for the stage
        stage.setTitle("Simple Chat Application - ClientInitializer");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/chat.png")));

        // Create a scene and set it to the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
