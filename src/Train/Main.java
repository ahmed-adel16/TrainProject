package Train;
//import java.sql*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int choice;
        System.out.println("hi");
        do {
            System.out.println("User Access Menu");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    signUp();
                    break;
                case 2:
                    if (login()){
                        mainMenu();
                        break;
                    }
                    else
                        System.out.println("password or email wrong!");
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }

    
    
    public static boolean login(){
        System.out.println("Enter a Email: ");
        String name = scanner.nextLine();
        System.out.println("Enter a Password: ");
        String password = scanner.nextLine();
        return Employee.authenticateEmployee(name, password);
    }
    // Main Menu
    public static void mainMenu() {
        int choice;
        do {
            System.out.println("Main Menu");
            System.out.println("1. Reserve");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Show Tickets");
            System.out.println("4. Add Train");
            System.out.println("5. Remove Train");
            System.out.println("6. Add User");
            System.out.println("7. Remove User");            
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    reserve();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    showTickets();
                    break;
                case 4:
                    addTrain();
                    break;
                case 5:
                    removeTrain();
                    break;
                case 6:
                    addUser();
                    break;
                case 7:
                    removeUser();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }

    public static void removeTrain(){
    	while (true) {
    		Train.showAllTrains();
            System.out.print("Enter the number of the Train you want to remove (0 to exit): ");
            int input = scanner.nextInt();
            if (input == 0) {
                System.out.println("Exiting...");
                break;
            } else if (Passenger.getPassengerList().isEmpty()) {
                System.out.println("No Trains left.");
                break;
            } else if (input > 0 && input <= Train.getTrains().size()) {
                Train removedTrain = Train.getTrains().remove(input - 1);
                System.out.println("Train: " + removedTrain.getTrainName() + " has been removed.");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    public static void addUser() {
    	// Create a new Passenger for the reservation
        System.out.print("Enter passenger name: ");
        String passengerName = scanner.nextLine();
        System.out.print("Enter passenger email: ");
        String passengerEmail = scanner.nextLine();
        System.out.print("Enter passenger age: ");
        int passengerAge = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter passenger tel: ");
        String passengerTel = scanner.nextLine();

        Passenger passenger = new Passenger(passengerName, passengerEmail, passengerAge, passengerTel);
        System.out.println("Passenger Created Successfully");
    }
    // Employee Sign Up
    public static void signUp() {
        System.out.println("Employee Sign Up");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter telephone: ");
        String tel = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Create a new employee object with the provided information
        Employee newEmployee = new Employee(name, email, age, tel, password);
    }

    // Method to remove a user from the system
    public static void removeUser() {
        while (true) {
            Passenger.showPassengers();
            System.out.print("Enter the number of the user you want to remove (0 to exit): ");
            int input = scanner.nextInt();
            if (input == 0) {
                System.out.println("Exiting...");
                break;
            } else if (Passenger.getPassengerList().isEmpty()) {
                System.out.println("No users left.");
                break;
            } else if (input > 0 && input <= Passenger.getPassengerList().size()) {
                Passenger removedPassenger = Passenger.getPassengerList().remove(input - 1);
                System.out.println(removedPassenger.getName() + " has been removed.");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Method to show user's tickets
    public static void showTickets() {
        System.out.println("User's Tickets:");
        // Get the user's tickets and display them
        while (true) {
                Passenger.showPassengers(); 
                System.out.print("Enter the number of the user you want to show tickets for (0 to exit): ");
                int input = scanner.nextInt();
                scanner.nextLine(); 

                if (input == 0) { 
                    System.out.println("Exiting...");
                    break;
                } else if (Passenger.getPassengerList().isEmpty()) { 
                    System.out.println("No passengers.");
                    break;
                } else if (input > 0 && input <= Passenger.getPassengerList().size()) { // If the input is within valid range
                    Passenger passenger = Passenger.getPassengerList().get(input - 1); // Get the selected passenger
                    passenger.showTicket(); 
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
        }
    }

    // Method to reserve a ticket
    public static void reserve() {
        // Display available trains for selection
        System.out.println("Available Trains:");
        if(!Train.getTrains().isEmpty()){
            for (int i = 0; i < Train.getTrains().size(); i++) {
                System.out.println((i + 1) + ". " + Train.getTrains().get(i));
            }

            // Prompt the user to select a train
            System.out.print("Select a train: ");
            int trainIndex = scanner.nextInt();
            scanner.nextLine();

            // Check if the selected index is valid
            if (trainIndex < 1 || trainIndex > Train.getTrains().size()) {
                System.out.println("Invalid train selection.");
                return;
            }
            Train selectedTrain = Train.getTrains().get(trainIndex - 1);
            while (true) {
                Passenger.showPassengers();
                System.out.print("Enter the number of the user you want to reserve (0 to exit): ");
                int input = scanner.nextInt();
                if (input == 0) {
                    System.out.println("Exiting...");
                    break;
                } else if (Passenger.getPassengerList().isEmpty()) {
                    System.out.println("No users");
                    break;
                } else if (input > 0 && input <= Passenger.getPassengerList().size()) {
                    Passenger passenger = Passenger.getPassengerList().get(input - 1);
        			Ticket ticket = new Ticket(passenger , selectedTrain);
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            System.out.println("\nTicket reserved successfully.\n");
        }
        else{
            System.out.print("No Trains, add Train? (y/n): ");
            if(scanner.nextLine().toLowerCase().equals("y")){
                addTrain();
                reserve();
            }
            else{
                System.out.println("\nGoing back\n");
                mainMenu();
            }
        }
    }



// Method to add a new train
public static void addTrain() {
    System.out.println("Add Train");

    // Prompt the user to enter train details
    System.out.print("Enter train name: ");
    String trainName = scanner.nextLine();

    System.out.print("Enter number of seats: ");
    int seats = scanner.nextInt();
    scanner.nextLine(); // Consume newline character

    System.out.print("Enter departure station: ");
    String departureStation = scanner.nextLine();

    System.out.print("Enter arrival station: ");
    String arrivalStation = scanner.nextLine();

    // Create a new Line object with the provided departure and arrival stations
    Line line = new Line(departureStation, arrivalStation);

    // Create a new Train object with the provided information
    Train newTrain = new Train(trainName, seats, line);

    System.out.println("Train added successfully.");
}




    // Method to cancel a reservation
    public static void cancelReservation() {
        System.out.println("Working on it");
    }
}
