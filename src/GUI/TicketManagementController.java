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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class TicketManagementController extends Methods implements Initializable {

    

    
    @FXML
    private TableView<Ticket> tableView;

    @FXML
    private TableColumn<Ticket, Integer> ticketIdCol;
    
    @FXML
    private TableColumn<Ticket, String> passengerEmailcol;
    
    @FXML
    private TableColumn<Train, Integer> trainNumberCol;
    
    @FXML
    private TableColumn<Ticket, String> ticketDateCol;


    @FXML
    private TextField ticketSearchField, emailField;
    @FXML
    private Pane topMenu;
    @FXML
    private ChoiceBox<String> trainChoice;
    @FXML
    private Button closeButton, backButton, refresh, cancelBookingButton, bookButton;
    @FXML
    private Label mainTitle;
    
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }
  
    @FXML
    public void viewTableItems(){
        ObservableList<Ticket> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM tickets NATURAL JOIN passengers NATURAL JOIN trains")) {
            
            while (rs.next()) {
                int ticketID = rs.getInt("ticket_id");
                String ticketDate = rs.getString("ticket_date");
                Passenger passenger = new Passenger(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("tel"),
                        rs.getString("email"),
                        rs.getString("password"));
                Train train = new Train(
                        rs.getInt("train_number"),
                        rs.getString("train_name"),
                        rs.getInt("seats"),
                        new Line(rs.getString("departure_station"),rs.getString("arrival_station")),
                        rs.getInt("tickets_left"));
                data.add(new Ticket(ticketID, passenger, train, ticketDate));
            }
            
            passengerEmailcol.setCellValueFactory(new PropertyValueFactory("passengerEmail"));
            ticketDateCol.setCellValueFactory(new PropertyValueFactory("ticketDate"));
            ticketIdCol.setCellValueFactory(new PropertyValueFactory("ticketID"));
            trainNumberCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
            tableView.setItems(data);
            
            // Initializing filtered list
            FilteredList<Ticket> filteredData = new FilteredList<>(data, b -> true);

            // Handling search field for filtering
            ticketSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ticket -> {
                    if (newValue.trim().isEmpty()) {
                        return true; // Show all trains if search field is empty
                    }

                    String searchKeyword = newValue.toLowerCase();

                    // Check if any train attribute contains the search keyword
                    return String.valueOf(ticket.getTicketID()).contains(searchKeyword) || 
                           ticket.getTicketDate().toLowerCase().contains(searchKeyword) ||
                            ticket.getPassengerEmail().toLowerCase().contains(searchKeyword) ||
                            String.valueOf(ticket.getTrainNumber()).contains(searchKeyword);
                });
            });

            SortedList<Ticket> sortedData = new SortedList<>(filteredData);

            // Bind sorted results with table view
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            
            tableView.setItems(sortedData);
            
            DatabaseManager.closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void getTrainChoices(){
        try {
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT train_number FROM Trains");
            while(rs.next()){
                int trainNumber = rs.getInt("train_number");
                trainChoice.getItems().add(String.valueOf(trainNumber));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void back(ActionEvent e) throws IOException{
        loadFXML("EmployeeMenu.fxml", "Employee Menu", e);
    }
    
    public void handleInputs(){
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            boolean anyEmpty = emailField.getText().trim().isEmpty() ||
                               trainChoice.getItems().isEmpty();

            bookButton.setDisable(anyEmpty);
        };
        emailField.textProperty().addListener(listener);
        trainChoice.valueProperty().addListener(listener);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            cancelBookingButton.setDisable(false);
        }
        else{
            cancelBookingButton.setDisable(true);
        }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewTableItems();
        handleInputs();
        getTrainChoices();
        
    }    
    
}
