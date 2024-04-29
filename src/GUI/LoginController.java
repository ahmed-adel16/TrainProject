/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Viber
 */
public class LoginController  extends Methods implements Initializable{
    
    @FXML
    private RadioButton rEmployee, rPassenger;
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
                    loadEmployeeScene(e, "EmployeeMenu.fxml"); // load up the menu
                }else{
                    loginMessage.setText("email or password don't match");
                }
            }
            
            else if (rPassenger.isSelected()){ // checks for radiobutton role selected
                if (Passenger.authenticatePassenger(emailField.getText(), passwordField.getText())){ // checks the credentials
                    loadPassengerScene(e, "PassengerMenu.fxml"); // load up the menu
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
    private void loadPassengerScene(ActionEvent e, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        PassengerMenuController controller = loader.getController();
        controller.initData(emailField.getText());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
    private void loadEmployeeScene(ActionEvent e, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        loginMessage.setText("");
    }    
    
}
