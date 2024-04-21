/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Viber
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        window.setTitle("Hello World");
        window.setScene(new Scene(root));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
