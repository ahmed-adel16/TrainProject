/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Viber
 */
public class LoginController  extends Methods implements Initializable{
    
    private Stage stage;
    private Parent root;
    private Scene scene;
    
    @FXML
    private RadioButton rEmployee, rPassenger, rAdmin;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginMessage, emailLabel, passwordLabel;

    
    public void login(ActionEvent e) throws IOException{
        if (validateLogin(emailField, passwordField)){ // checks validation
            
            if (rEmployee.isSelected()){ // checks for radiobutton role selected
                if (Employee.authenticateEmployee(emailField.getText(), passwordField.getText())){ // checks the credentials
                    loadFXML("EmployeeMenu.fxml", "Employee Menu", e); // load up the menu
                }else{
                    loginMessage.setText("email or password don't match");
                }
            }
            
            else if (rPassenger.isSelected()){ // checks for radiobutton role selected
                if (Passenger.authenticatePassenger(emailField.getText(), passwordField.getText())){ // checks the credentials
                    loadFXML("UserMenu.fxml", "User menu", e); // load up the menu
                }else{
                    loginMessage.setText("email or password don't match");
                }
                
            }else{
                loginMessage.setText("Please choose a login role");
            }
        }
    }
    public void register(ActionEvent e) throws IOException{
        loadFXML("RegisterMenu.fxml", "Registration menu", e);
    }
    
    public void close(ActionEvent e){
        confirmAndExit();
    }

    public boolean validateLogin(TextField email, PasswordField password){
        boolean isEmail = DataValidation.emailFormat(emailField, emailLabel , "Invalid, must be like: abcd@ghi.com");
        boolean isPassword = DataValidation.passwordValidation(passwordField, passwordLabel , "must be more at least 8 chars", "8");
        return isEmail && isPassword;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginMessage.setText("");
    }    
    
}
