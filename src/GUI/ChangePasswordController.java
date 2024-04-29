/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.DatabaseManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import java.sql.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Viber
 */
public class ChangePasswordController implements Initializable {

    @FXML   
    private PasswordField oldPasswordField, newPasswordField;
    @FXML
    private Label newPasswordError, oldPasswordError;
    @FXML
    private Button changePasswordButton;
    private int id;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void initData(int id){
        this.id = id;
    }
    
 public void changePassword() {
    try {
        Connection c = DatabaseManager.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT password from passengers WHERE passenger_id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            String correctOldPassword = rs.getString("password");
            String oldPasswordInserted = DatabaseManager.doHashing(oldPasswordField.getText());
            String newPasswordInserted = DatabaseManager.doHashing(newPasswordField.getText());
            
            ps.close();
            rs.close();

            if (correctOldPassword.equals(oldPasswordInserted)) {
                if (DataValidation.passwordValidation(newPasswordField, newPasswordError, "Choose another password", "8")) {
                    ps = c.prepareStatement("UPDATE Passengers SET password = ? WHERE passenger_id = ?");
                    ps.setString(1, newPasswordInserted);
                    ps.setInt(2, id);
                    int rowsUpdated = ps.executeUpdate();
                    if (rowsUpdated > 0) {
                        Methods.informationAlert("Success", "Password Changed Successfully");
                        Stage stage = (Stage) changePasswordButton.getScene().getWindow(); // Assuming closeButton is the button to close the modal dialog
                        stage.close();
                    } else {
                        Methods.informationAlert("Failed", "Error while changing password");
                    }
                    ps.close();
                }
            } else {
                oldPasswordError.setText("Old password is incorrect");
            }
        } else {
            Methods.informationAlert("Failed", "Passenger ID not found");
        }
        c.close();
    } catch (SQLException e) {
        e.printStackTrace();
        Methods.informationAlert("Error", "An error occurred while changing password");
    }
}
    
    
    public PasswordField getOldPasswordField() {
        return oldPasswordField;
    }

    public PasswordField getNewPasswordField() {
        return newPasswordField;
    }
    
}
