/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.DatabaseManager;
import Train.Line;
import Train.Passenger;
import Train.Ticket;
import Train.Train;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
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
    private Button deletePassengerButton;
    @FXML
    private TableView <Passenger> passengerTable;
    @FXML
    private TableColumn<Passenger, Integer> IDCol;
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
    public int selectedId;

    public void viewTableItems(){
        ObservableList<Passenger> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM Passengers")) {
            
            while (rs.next()) {
                int id = rs.getInt("passenger_id");
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                String tel = rs.getString("tel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                
                data.add(new Passenger(id, name, age, tel, email, password));
            }
            IDCol.setCellValueFactory(new PropertyValueFactory("id"));
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
    
        public void handleInputs(){
        passengerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            
        if (newSelection != null) {
            deletePassengerButton.setDisable(false);
        }
        else{
            deletePassengerButton.setDisable(true);
        }
        });
    }
    
    public void getSelection(){
        passengerTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check for single click
                // Get selected item
                Passenger selectedItem = passengerTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedId = selectedItem.getId(); // Retrieve ID
                }
            }
        });
    }
    
    public void deletePassenger(){
        try{
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement st = conn.prepareStatement("DELETE FROM Passengers WHERE passenger_id = " + selectedId);
        if(Methods.confirmationAlert("Confirm", "Delete passenger with id: " + selectedId, "are you sure?")){
            st.executeUpdate();
            refresh();
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void close(ActionEvent e) {
        confirmAndExit();
    }

    public void back(ActionEvent e) throws IOException {
        loadFXML("EmployeeMenu.fxml", "", e);
    }
    
    public void refresh(){
        viewTableItems();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewTableItems();
        getSelection();
        handleInputs();
    }

    


    
}
