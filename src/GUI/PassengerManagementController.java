/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.DatabaseManager;
import Train.Line;
import Train.Passenger;
import Train.Train;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class PassengerManagementController extends Methods implements Initializable {

    @FXML
    private Pane topMenu;
    @FXML
    private Button closeButton;
    @FXML
    private Label mainTitle;
    @FXML
    private Button backButton;
    @FXML
    private Button refresh;
    @FXML
    private TextField passengerSearchField;
    @FXML
    private Button cancelBookingButton;
    @FXML
    private TableView <Passenger> passengerTable;
    @FXML
    private TableColumn<Passenger, String> nameCol;
    @FXML
    private TableColumn<Passenger, Integer> ageCol;
    @FXML
    private TableColumn<Passenger, String> telCol;
    @FXML
    private TableColumn<Passenger, String> emailCol;
    @FXML
    private TableColumn<Passenger, String> passwordCol;
    
    private Stage stage;
    private Parent root;
    private Scene scene;
    
    public void viewTableItems(){
        ObservableList<Passenger> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM Passengers")) {
            
            while (rs.next()) {
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                String tel = rs.getString("tel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                
                data.add(new Passenger(name, age, tel, email, password));
            }
            nameCol.setCellValueFactory(new PropertyValueFactory("name"));
            ageCol.setCellValueFactory(new PropertyValueFactory("age"));
            telCol.setCellValueFactory(new PropertyValueFactory("tel"));
            emailCol.setCellValueFactory(new PropertyValueFactory("email"));
            passwordCol.setCellValueFactory(new PropertyValueFactory("password"));
            passengerTable.setItems(data);
            
            // Initializing filtered list
            FilteredList<Passenger> filteredData = new FilteredList<>(data, b -> true);

            // Handling search field for filtering
            passengerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(passenger -> {
                    if (newValue.trim().isEmpty()) {
                        return true; // Show all trains if search field is empty
                    }

                    String searchKeyword = newValue.toLowerCase();

                    // Check if any train attribute contains the search keyword
                    return passenger.getName().toLowerCase().contains(searchKeyword) || 
                           String.valueOf(passenger.getAge()).contains(searchKeyword) || 
                           passenger.getTel().toLowerCase().contains(searchKeyword) || 
                           passenger.getEmail().toLowerCase().contains(searchKeyword) || 
                           passenger.getPassword().toLowerCase().contains(searchKeyword);
                });
            });

            SortedList<Passenger> sortedData = new SortedList<>(filteredData);

            // Bind sorted results with table view
            sortedData.comparatorProperty().bind(passengerTable.comparatorProperty());
            
            passengerTable.setItems(sortedData);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ActionEvent e) {
        confirmAndExit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewTableItems();
    }    

    @FXML
    void back(ActionEvent e) throws IOException {
        loadFXML("EmployeeMenu.fxml", "", e);
    }


    
}
