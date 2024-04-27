package Train;

import java.text.SimpleDateFormat;
import java.util.Date;

// Represents a train ticket
public class Ticket {
    private int ticketID;
    private Passenger passenger;
    private Train train;
    private String ticketDate;

    // Constructor to initialize ticket attributes
    public Ticket(int ticketID, Passenger passenger, Train train, String ticketDate) {
        this.passenger = passenger; 
        this.train = train;
        this.ticketDate = ticketDate;
        this.ticketID = ticketID;
        // Retrieve the next available ticket ID from the database
     

    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Train getTrain() {
        return train;
    }

   
    // Getters and setters
    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getPassengerEmail() {
        return passenger.getEmail();
    }
    public String getPassengerName() {
        return passenger.getName();
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public int getTrainNumber() {
        return train.getTrainNumber();
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    // Method to insert a ticket into the database
   

    // Method to get the current date as a string

    @Override
    public String toString() {
        return "\nTicket ID: " + ticketID + "\nPassenger name: " + passenger.getName() + "\nTrain name: " + this.getTrain().getTrainName() + "\nTicket Date: " + ticketDate + "\n";
    }
}
