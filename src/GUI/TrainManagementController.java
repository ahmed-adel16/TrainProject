/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.*;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class TrainManagementController extends Methods implements Initializable {

    @FXML
    private TextField trainSearchField, trainNameField, seatsNumberField, fromField, toField, trainNumberField;
    
    @FXML
    private TableView<Train> tableView;

    @FXML
    private TableColumn<Train, Integer> trainNumberColumn;
    
    @FXML
    private TableColumn<Train, String> trainNameColumn;

    @FXML
    private TableColumn<Train, Integer> trainSeatsColumn;
    
    @FXML
    private TableColumn<Train, String> trainDepartureColumn;
    
    @FXML
    private TableColumn<Train, String> trainArrivalColumn;

    @FXML
    private TableColumn<Train, Integer> ticketsLeftColumn;

    @FXML
    private Button addTrainButton, updateTrainButton, deleteTrainButton;
    
    private Scene scene;
    private Stage stage;
    private Parent root;
    private int index = -1;
    
    public void close(ActionEvent e){
        Platform.exit();
    } 
    
    public void addTrain() {
        try {
            Connection c = DatabaseManager.getConnection();
            PreparedStatement st = c.prepareStatement("INSERT INTO Trains (train_name, seats, departure_station, arrival_station, tickets_left) VALUES (?, ?, ?, ?, ?)");
            st.setString(1, trainNameField.getText());
            st.setString(2, seatsNumberField.getText());
            st.setString(3, fromField.getText());
            st.setString(4, toField.getText());
            st.setInt(5, 100); // Assuming you want to set train_number to 100

            st.executeUpdate();
            st.close();
            Methods.informationAlert("Success", "Train added Successfully\nTrain Name: " + trainNameField.getText());
            viewTableItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete() {
        try {
            // Get a connection to the database
            Connection c = DatabaseManager.getConnection();

            // Prepare the SQL statement
            PreparedStatement st = c.prepareStatement("DELETE FROM trains WHERE train_number = ?");

            // Set the train number parameter for the prepared statement
            st.setInt(1, Integer.parseInt(trainNumberField.getText())); // Assuming seatsNumberField is a JTextField or similar

            Methods.informationAlert("delete", "Train is deleted successfully");

            // Execute the delete statement
            st.executeUpdate();

            viewTableItems();


            // Close the statement and connection
            st.close();
            c.close();
            
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }
    
    public void getSelected (MouseEvent e){
        index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        trainNumberField.setText(trainNumberColumn.getCellData(index).toString());
        trainNameField.setText(trainNameColumn.getCellData(index));
        seatsNumberField.setText(trainSeatsColumn.getCellData(index).toString());
        fromField.setText(trainDepartureColumn.getCellData(index));
        toField.setText(trainArrivalColumn.getCellData(index));
    }
    
    public void update(ActionEvent e){
        try{
            Connection c = DatabaseManager.getConnection();
            String name = trainNameField.getText();
            String number = trainNumberField.getText();
            String seats = seatsNumberField.getText();
            String departure = fromField.getText();
            String arrival = toField.getText();
            PreparedStatement st = c.prepareStatement("UPDATE Trains SET train_name = ?, seats = ?, departure_station = ?, arrival_station = ? WHERE train_number = ?");
            st.setString(1, name);
            st.setString(2, seats);
            st.setString(3, departure);
            st.setString(4, arrival);
            st.setString(5, number);
            
            st.executeUpdate();
            
            viewTableItems();
        }catch(Exception er){
            er.printStackTrace();
        }
    }
    
    public void viewTableItems(){
        
         ObservableList<Train> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM Trains")) {
            
            while (rs.next()) {
                Integer number = rs.getInt("train_number");
                String name = rs.getString("train_name");
                Integer seats = rs.getInt("seats");
                String departureStation = rs.getString("departure_station");
                String arrivalStation = rs.getString("arrival_station");
                Integer ticketsLeft = rs.getInt("tickets_left");
                data.add(new Train(number, name, seats, new Line(departureStation, arrivalStation), ticketsLeft));
            }
            
            trainNumberColumn.setCellValueFactory(new PropertyValueFactory("trainNumber"));
            trainNameColumn.setCellValueFactory(new PropertyValueFactory("trainName"));
            trainSeatsColumn.setCellValueFactory(new PropertyValueFactory("seats"));
            trainDepartureColumn.setCellValueFactory(new PropertyValueFactory("departureStation"));
            trainArrivalColumn.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
            ticketsLeftColumn.setCellValueFactory(new PropertyValueFactory("ticketsLeft"));
            tableView.setItems(data);
            
            // Initializing filtered list
            FilteredList<Train> filteredData = new FilteredList<>(data, b -> true);

            // Handling search field for filtering
            trainSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(train -> {
                    if (newValue.trim().isEmpty()) {
                        return true; // Show all trains if search field is empty
                    }

                    String searchKeyword = newValue.toLowerCase();

                    // Check if any train attribute contains the search keyword
                    return train.getTrainName().toLowerCase().contains(searchKeyword) || 
                           String.valueOf(train.getTrainNumber()).contains(searchKeyword) || 
                           train.getArrivalStation().toLowerCase().contains(searchKeyword) || 
                           train.getDepartureStation().toLowerCase().contains(searchKeyword) || 
                           String.valueOf(train.getSeats()).contains(searchKeyword) || 
                           String.valueOf(train.getTicketsLeft()).contains(searchKeyword);
                });
            });

            SortedList<Train> sortedData = new SortedList<>(filteredData);

            // Bind sorted results with table view
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            
            tableView.setItems(sortedData);
            
            DatabaseManager.closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void add(ActionEvent e){
        addTrain();
    }
    public void back(ActionEvent e) throws IOException{
        loadFXML("EmployeeMenu.fxml", "Employee Menu", e);
    }
    
    
    private boolean isAnyFieldEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return true; // At least one field is empty
            }
        }
        return false; // No empty fields
    }
        
    // to check if any field is empty and handle add button
    public void handleInputs(){
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            boolean anyEmpty = trainNameField.getText().trim().isEmpty() ||
                               seatsNumberField.getText().trim().isEmpty() ||
                               fromField.getText().trim().isEmpty() ||
                               toField.getText().trim().isEmpty() ;

            addTrainButton.setDisable(anyEmpty);
        };

        trainNameField.textProperty().addListener(listener);
        seatsNumberField.textProperty().addListener(listener);
        fromField.textProperty().addListener(listener);
        toField.textProperty().addListener(listener);
        
        // to handle update and delete buttons
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            deleteTrainButton.setDisable(false);
            updateTrainButton.setDisable(false);
        }
        else{
            updateTrainButton.setDisable(true);
            deleteTrainButton.setDisable(true);
        }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewTableItems();
        handleInputs();
        
    }   
    
}
