/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Train.DatabaseManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Viber
 */
public class EmployeeMenuController extends Methods implements Initializable{

    @FXML
    private Button managePassengers, manageTickets, manageTrains, logOut, closeButton;

    private Parent root;
    private Scene scene;
    private Stage stage;
    private String name, email, tel, password;
    private int age;

    
    @FXML
    void handleButtons(ActionEvent e) throws IOException {
        if (e.getSource() == manageTrains){
            loadFXML("TrainManagement.fxml", "Manage Trains", e);
        }
        if (e.getSource() == manageTickets){
            loadFXML("TicketManagement.fxml", "Manage Tickets", e);
        }
        if (e.getSource() == managePassengers){
            loadFXML("PassengerManagement.fxml", "Manage Tickets", e);
        }
        if (e.getSource() == logOut){
            logout(e);
        }
        if (e.getSource() == closeButton){
            if(confirmationAlert("Exit?", "", "Are You Sure?"))
                Platform.exit();
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
