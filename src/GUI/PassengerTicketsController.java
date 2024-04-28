/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class PassengerTicketsController extends Methods implements Initializable {

    @FXML
    private Pane topMenu;
    @FXML
    private Button closeButton;
    @FXML
    private Label title;
    @FXML
    private Button backButton;
    @FXML
    private Label passengerNameLabel;
    @FXML
    private TextField ticketSearchField;
    @FXML
    private Button cancelBookingButton;
    @FXML
    private Button refresh;
    @FXML
    private TableView<Ticket> tableView;
    @FXML
    private TableColumn<Ticket, Integer> ticketIdCol;
    @FXML
    private TableColumn<Ticket, Integer> trainNumberCol;
    @FXML
    private TableColumn<Ticket, String> ticketDateCol;

    private Ticket ticket;
    private int selectedId;

    private String name, email,tel, password;
    private int id, age; 
    
    private Connection c;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    
    public void initData(int id, String name, int age, String tel, String email, String password){
            this.id = id;
            this.name = name;
            this.age = age;
            this.tel = tel;
            this.email = email;
            this.password = password;
            passengerNameLabel.setText(name);
    }
            
    @FXML
    private void close(ActionEvent event) {
        confirmAndExit();
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        loadFXML("PassengerMenu.fxml","Main Menu",event);
    }

    @FXML
    private void cancelBooking(ActionEvent event) {
        try{
            c = DatabaseManager.getConnection();
            // deleting the ticket from the database
            ps = c.prepareStatement("DELETE FROM Tickets WHERE ticket_id = ?");
            ps.setInt(1, selectedId);
            // making confirmation for the user
            if (Methods.confirmationAlert("Confirm", "Cancelling your reservation with id: " + selectedId , "are you sure?")){
                ps.executeUpdate();
            }
            ps.close();
            
            // increasing the tickets left back
            ps = c.prepareStatement("SELECT train_number FROM Tickets where ticket_id = ?"); // selected the percised train
            ps.setInt(1, selectedId);
            rs = ps.executeQuery();
            int trainNumber = -1;
            if(rs.next()){
                trainNumber = rs.getInt("train_number");
            }
            ps.close();
            rs.close();
            
            ps = c.prepareStatement("UPDATE Trains SET tickets_left = tickets_left + 1 WHERE train_number = ?"); // updating tickets_left back
            ps.setInt(1, trainNumber);
              
            c.close();
            refresh();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void refresh() {
        viewTableItems();
    }
    
    
    public void viewTableItems(){
        ObservableList<Ticket> data = FXCollections.observableArrayList();
            int id = this.id;
        try{
            Connection c = DatabaseManager.getConnection();
            ps = c.prepareStatement("SELECT * FROM tickets NATURAL JOIN passengers NATURAL JOIN trains WHERE passenger_id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                int ticketID = rs.getInt("ticket_id");
                String ticketDate = rs.getString("ticket_date");
                Passenger passenger = new Passenger(
                    rs.getInt("passenger_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("tel"),
                    rs.getString("email"),
                    rs.getString("password"));
                Train train = new Train(
                    rs.getInt("train_number"),
                    rs.getString("train_name"),
                    rs.getInt("seats"),
                    new Line(rs.getString("departure_station"), rs.getString("arrival_station")),
                    rs.getInt("tickets_left"));
                ticket = new Ticket(ticketID, passenger, train, ticketDate); 
                data.add(ticket);
            }

            
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
                        
            c.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void getSelection(){
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check for single click
                // Get selected item
                Ticket selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedId = selectedItem.getTicketID(); // Retrieve ID
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewTableItems();
        getSelection();
    }    
}
