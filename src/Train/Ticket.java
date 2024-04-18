package Train;

import java.util.Date;

// Represents a train ticket which implements the Booking interface
public class Ticket implements Booking {
    private static int nextTicketID = 1;
    private String ticketID;
    private Passenger passenger;
    private Train train;
    private String ticketDate;

    // Constructor to initialize ticket attributes
    public Ticket(Passenger passenger, Train train) {
        this.ticketID = "TKT" + nextTicketID++;
        this.passenger = passenger;
        this.train = train;
        this.book();
    }

    // Method to book the ticket
    @Override
    public void book() {
        this.getTrain().setSeats(this.getTrain().getSeats() - 1);
        Date currentDate = new Date();
        this.ticketDate = String.format("%tF %tR", currentDate, currentDate);
        this.getPassenger().addPasasengerTickets(this);
    }

    // Method to cancel the booking
    @Override
    public void cancelBooking() {
        this.getTrain().setSeats(this.getTrain().getSeats() + 1);
        this.ticketDate = null;
    }

    // Getters and Setters
    public String getTicketID() {
        return ticketID;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Train getTrain() {
        return train;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketID + "\nPassenger: " + passenger.getName() + "\nTrain: " + this.getTrain().getTrainName() + "\nTicket Date: " + ticketDate;
    }


}
