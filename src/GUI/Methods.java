/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.LoginController.confirmationAlert;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Viber
 */
public class Methods {
    private Parent root;
    private Scene scene;
    private Stage stage;

    public void loadFXML(String fxmlFileName, String title, ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        root = fxmlLoader.load();
        scene = new Scene(root);
        String css = this.getClass().getResource("style.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
    
    public void loadPassengerMain(ActionEvent e) throws IOException{
        loadFXML("PassengerMenu.fxml","Main Menu",e);
    }
    
    public void logout(ActionEvent e) throws IOException{
        boolean confirmed  = confirmationAlert("Logout", "", "Are You Sure?");
            if (confirmed){
                loadFXML("LoginMenu.fxml", "Manage Tickets", e);
            }
    }
    
    public static boolean informationAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static boolean confirmationAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Add "Yes" and "No" buttons
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    public static void confirmAndExit() {
        boolean confirmed = confirmationAlert("Exit", null, "Are you sure you want to exit?");
        if (confirmed)
            Platform.exit();
    }

}
        

