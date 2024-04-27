/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.LoginController.confirmationAlert;

import Train.*;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController extends Methods{
    
    @FXML
    private RadioButton rEmployee, rPassenger;

    @FXML
    private Label errorLabel, nameError, ageError, telError, emailError, passwordError;
    
    @FXML
    private TextField registerNameField, registerAgeField, registerTelField, registerEmailField ;
    
    @FXML
    private PasswordField registerPassField;
    
    public void close(ActionEvent e){
        boolean confirmed = confirmationAlert("Exit", "", "are you sure?");
        if (confirmed)
            Platform.exit();
    }
    
    public void SignUp(ActionEvent e) throws IOException{
        String name = registerNameField.getText();
        int age = DataValidation.isNumeric(registerAgeField.getText()) ? Integer.parseInt(registerAgeField.getText()) : 0;
        String tel = registerTelField.getText();
        String email =  registerEmailField.getText();
        String pass = registerPassField.getText();
        if (checkRequestedFields(registerNameField, registerAgeField, registerTelField, registerEmailField, registerPassField,
                 nameError, ageError, telError, emailError, passwordError
        )){
            
            if(rEmployee.isSelected()){
                if(!Employee.authenticateEmployee(email, pass)){
                    DatabaseManager.insertEmployee(name, email, age, tel, pass);
                    informationAlert("Done", "Successfully Registered");
                    loadFXML("LoginMenu.fxml", "", e);
                }else{
                errorLabel.setText("Already existed");
                }
            }
            
            else if (rPassenger.isSelected()){
                if(!Passenger.authenticatePassenger(email, pass)){
                    DatabaseManager.insertPassenger(name, age, tel, email, pass);
                    informationAlert("Done", "Successfully Registered");
                    loadFXML("LoginMenu.fxml", "", e);
                }else{
                errorLabel.setText("Already existed");
                }
            }

        }
    }
    
    public void back(ActionEvent e) throws IOException{
        loadFXML("LoginMenu.fxml", "", e);
    }
    
    public static boolean checkRequestedFields(TextField name, TextField age, TextField tel, TextField email, PasswordField password, Label nameError, Label ageError, Label telError, Label emailError, Label passwordError) {
        boolean isName, isAge, isTel, isEmail, isPassword;
        isName = DataValidation.dataLength(name, nameError, "invalid name", "5");
        isAge = DataValidation.textNumeric(age, ageError, "must be a number [1-9]");
        isTel = DataValidation.textPhone(tel, telError, "inavalid, i.e: 01xxxxxxxxx");
        isEmail = DataValidation.emailFormat(email, emailError, "Invalid, must be like: abcd@ghi.com");
        isPassword = DataValidation.passwordValidation(password, passwordError, "must be more at least 8 chars", "8");
        
        return isName && isAge && isTel && isEmail && isPassword;
    }
    

    
        
    

}

    
    

