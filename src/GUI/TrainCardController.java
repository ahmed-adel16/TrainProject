/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import Train.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import java.sql.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Viber
 */
public class TrainCardController implements Initializable {

    @FXML
    private Label from, to, trainName, ticketsLeft;
    @FXML
    private Spinner<Integer> cardSpinner  = new Spinner<>();
    @FXML
    private Button cardBook;
    
    private static String email;
    private static int id;
    private int trainNumber;
    
    private Connection c;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    
    public void intitData(String email){
            this.email = email;
            try{
            c = DatabaseManager.getConnection();
            ps = c.prepareStatement("SELECT * FROM Passengers WHERE email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();

            this.id = rs.getInt("passenger_id");
            this.email = rs.getString("email");

            rs.close();
            ps.close();
            c.close();
            
        }catch(SQLException e){
        }
    }
    public void setData(Train trainData) {
        trainNumber = trainData.getTrainNumber();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, trainData.getTicketsLeft(), 0);
        trainName.setText("Train name: " + trainData.getTrainName());
        ticketsLeft.setText("Tickets left: " + trainData.getTicketsLeft());
        from.setText("From: " + trainData.getDepartureStation());
        to.setText("To: " + trainData.getArrivalStation());
        cardSpinner.setValueFactory(valueFactory);
        
        if(trainData.getTicketsLeft() == 0){
            cardBook.setDisable(true);
            cardSpinner.setDisable(true);
        }
    }

    public void book() {
        int tickets = cardSpinner.getValue();
        boolean confirmed = true;
        if(tickets > 0) confirmed = Methods.confirmationAlert("Reservation", "Do you want to Reserve " + tickets + " seat\n" +
                trainName.getText() + "Train?" +
                "From: "+from.getText() + "To: " + to.getText() , "press ok to confirm");
        if(confirmed){
            try {
                c = DatabaseManager.getConnection();

                // Decrease the number of available tickets
                ps = c.prepareStatement("UPDATE Trains SET tickets_left = tickets_left - ? WHERE train_number = ?");
                ps.setInt(1, tickets);
                ps.setInt(2, trainNumber);
                ps.executeUpdate();
                ps.close();

                // Insert booking information
                for (int i = 0; i < tickets; i++) {
                    ps = c.prepareStatement("INSERT INTO Tickets (passenger_email, train_number, passenger_id) VALUES (?, ?, ?)");
                    ps.setString(1, this.email);
                    ps.setInt(2, trainNumber);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    ps.close();
                }

                // Fetch and display the updated number of tickets left
                ps = c.prepareStatement("SELECT tickets_left FROM Trains WHERE train_number = ?");
                ps.setInt(1, trainNumber);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int newTicketsLeft = rs.getInt("tickets_left");
                    ticketsLeft.setText("Tickets left: " + newTicketsLeft);

                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, newTicketsLeft, 0);
                    cardSpinner.setValueFactory(valueFactory);
                }

                rs.close();
                ps.close();
                c.close();


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
