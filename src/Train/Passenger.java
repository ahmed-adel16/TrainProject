package Train;

import java.util.ArrayList;
import java.util.List;

// Represents a passenger who extends the Person class
public class Passenger extends Person {
    private static int nextPassengerId = 1; // Static variable to generate unique passenger IDs
    private String passengerId; // Unique ID for the passenger
    private static List<Passenger> passengerList = new ArrayList<>(); // List to store passengers
    private List<Ticket> pasasengerTickets = new ArrayList<>();

    // Constructor to initialize passenger attributes
    public Passenger(String name, String email, int age, String tel) {
        super(name, email, age, tel); // Call to parent class constructor
        this.passengerId = "PSN" + nextPassengerId++; // Generate passenger ID
        passengerList.add(this); // Add passenger to the list
    }

    public void showTicket(){
        int i = 1;
        for (Ticket ticket : this.pasasengerTickets) {
            System.out.println("\n"+(i++)+". " + ticket.toString());
        }
    }
    
    public List<Ticket> getPasasengerTickets() {
        return pasasengerTickets;
    }

    public void addPasasengerTickets(Ticket pasasengerTickets) {
        this.pasasengerTickets.add(pasasengerTickets);
    }

    // Getter for passenger list
    public static List<Passenger> getPassengerList() {
        return passengerList;
    }

    // Method to display passengers
    public static void showPassengers() {
        int n = 1;
        for (Passenger passenger : passengerList) {
            System.out.println(n++ + ". " + passenger);
        }
    }

    // Override toString method for better representation
    @Override
    public String toString() {
        return "{Name: " + getName() + ", Age: " + getAge() + ", Email: " + getEmail() + ", Tel: " + getTel() + "}";
    }
}
