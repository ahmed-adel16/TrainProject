/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.DatabaseManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class PassengerMenuController extends Methods implements Initializable {

    @FXML
    private Pane topMenu;
    @FXML
    private Label mainTitle;
    @FXML
    private Button showTicketsButton, reserveButton, logoutButton, closeButton;

    private String name, email,tel, password;
    private int id, age;
    
    private Connection c;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;

    
    public void initData(String email) throws IOException{

        this.email = email;
        try{
            c = DatabaseManager.getConnection();
            ps = c.prepareStatement("SELECT * FROM Passengers WHERE email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();

            this.id = rs.getInt("passenger_id");
            this.name = rs.getString("name");
            this.tel = rs.getString("tel");
            this.password = rs.getString("password");
            this.age = rs.getInt("age");

            rs.close();
            ps.close();
            c.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("TrainCard.fxml"));
            loader.load();
            TrainCardController controller = loader.getController();
            controller.intitData(this.id, this.name, this.age, this.tel, this.email, this.password);
            mainTitle.setText(name);
        }catch(SQLException e){
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void handleButtons(ActionEvent event) throws IOException {
        if (event.getSource() == closeButton){
            confirmAndExit();
        }
        if (event.getSource() == logoutButton){
            logout(event);
        }
        if (event.getSource() == showTicketsButton){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PassengerTickets.fxml"));
            Parent root = loader.load();
            PassengerTicketsController controller = loader.getController();
            controller.initData(id, name, age, tel, email, password);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        }
        if (event.getSource() == reserveButton){
            
            // loading up reserveMenu and passing the passenger's information to it
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReserveMenu.fxml"));
            Parent root = loader.load();
            ReserveMenuController controller = loader.getController();
            controller.initData(id, age, tel, email, password);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        
        }
    }
    
    @FXML
    private void changePassword(){
        
    }
    
}
