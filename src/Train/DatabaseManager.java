package Train;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;



public class DatabaseManager {
    // JDBC URL for SQLite database
    private static final String url = "jdbc:sqlite:database.db";

    // Method to get the connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
   
    // Method to close the connection
    public static void closeConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    // Method to close the statement
    public static void closeStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    // Method to close the result set in order to free up resources after we are done using it
    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    // Method to insert a new passenger into the database
    public static void insertPassenger(String name, int age, String tel, String email, String password) {
        String sql = "INSERT INTO Passengers(name, age, tel, email, password) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, tel);
            pstmt.setString(4, email);
            pstmt.setString(5, doHashing(password));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Passenger inserted successfully.");
            } else {
                System.out.println("Passenger insertion failed.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting passenger: " + e.getMessage());
        }
    }
    
    

    // Method to insert Employee to the database
    public static void insertEmployee(String name, String email, int age, String tel, String password) {
        String sql = "INSERT INTO Employees(name, email, age, tel, password) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.setString(4, tel);
            pstmt.setString(5, doHashing(password));
            pstmt.executeUpdate();
            System.out.println("Employee inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting employee: " + e.getMessage());
        }
    }



    // Method to retrieve the password of a passenger by email
    public static String getPassengerPassword(String email) {
        String password = null;
        String sql = "SELECT password FROM Passengers WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving passenger password: " + e.getMessage());
        }
        return password;
    }

    // Method to find and select employee password associated with given email
    public static String getEmployeePassword(String email) {
        String sql = "SELECT password FROM Employees WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee password: " + e.getMessage());
        }
        return null; // Return null if the email doesn't exist in the database
    }


//method to remove user (passenger) from the database using his email
public static void removeUser(String email) {
    String deletePassengerSql = "DELETE FROM Passengers WHERE email = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmtPassenger = conn.prepareStatement(deletePassengerSql)) {

        pstmtPassenger.setString(1, email);
        int passengerDeleted = pstmtPassenger.executeUpdate();

        if (passengerDeleted == 0) {
            System.out.println("Passenger with email " + email + " not found.");
        } else {
            System.out.println("Passenger with email " + email + " deleted successfully.");
        }
    } catch (SQLException e) {
        System.err.println("Error removing user: " + e.getMessage());
    }
}

// Method to remove employee from the database
public static void removeEmployee(String email) {
    String sql = "DELETE FROM Employees WHERE email = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, email);
        int rowsDeleted = pstmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Employee removed successfully.");
        } else {
            System.out.println("No employee found with the provided email.");
        }
    } catch (SQLException e) {
        System.err.println("Error removing employee: " + e.getMessage());
    }
}

// method to remove a train from the database
public static void removeTrain(String trainNumber) {
    String sql = "DELETE FROM Trains WHERE train_number = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, trainNumber);
        int rowsDeleted = pstmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Train removed successfully.");
        } else {
            System.out.println("No train found with the provided train number.");
        }
    } catch (SQLException e) {
        System.err.println("Error removing train: " + e.getMessage());
    }
}

// automatic method that checks for trains with no tickets i.e tickets = 0 and remove them
public static void removeTrainsWithNoTicketsLeft() {
    String sql = "DELETE FROM Trains WHERE tickets_left = 0";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        int rowsDeleted = pstmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Trains with no tickets left removed successfully.");
        } else {
            System.out.println("No trains with no tickets left found.");
        }
    } catch (SQLException e) {
        System.err.println("Error removing trains with no tickets left: " + e.getMessage());
    }
}


// method to show all passenger accounts
public static ResultSet getPassengrsResultSet() {
    String sql = "SELECT name, email, age, tel FROM Passengers";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            int age = rs.getInt("age");
            String tel = rs.getString("tel");
        }
        return rs;
    } catch (SQLException e) {
        System.err.println("Error retrieving passenger accounts: " + e.getMessage());
    }
    return null;
}



// Method used to insert a new train into the database
public static void insertTrain(String trainNumber, String trainName, int seats, String departureStation, String arrivalStation, int ticketsLeft) {
    String sql = "INSERT INTO Trains(train_number, train_name, seats, departure_station, arrival_station, tickets_left) VALUES(?,?,?,?,?,?)";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, trainNumber);
        pstmt.setString(2, trainName);
        pstmt.setInt(3, seats);
        pstmt.setString(4, departureStation);
        pstmt.setString(5, arrivalStation);
        pstmt.setInt(6, ticketsLeft); // Add tickets left parameter
        pstmt.executeUpdate();
        System.out.println("Train inserted successfully.");
    } catch (SQLException e) {
        System.err.println("Error inserting train: " + e.getMessage());
    }
}


// Method to update tickets left at each booked ticket
public static void updateTicketsLeft(int trainNumber, int ticketsLeft) {
    String sql = "UPDATE Trains SET tickets_left = ? WHERE train_number = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, ticketsLeft);
        pstmt.setInt(2, trainNumber);
        pstmt.executeUpdate();
        System.out.println("Tickets left for train " + trainNumber + " updated successfully.");
    } catch (SQLException e) {
        System.err.println("Error updating tickets left: " + e.getMessage());
    }
}


// Method to insert a new ticket to the database
public static void insertTicket(String passengerEmail, String trainNumber) {
    String sql = "INSERT INTO Tickets(passenger_email, train_number) VALUES(?,?)";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, passengerEmail);
        pstmt.setString(2, trainNumber);
        pstmt.executeUpdate();
        System.out.println("Ticket inserted successfully.");
    } catch (SQLException e) {
        System.err.println("Error inserting ticket: " + e.getMessage());
    }
}

public static String doHashing(String password) {
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        messageDigest.update(password.getBytes());

        byte[] resultByteArray = messageDigest.digest();

        StringBuilder sb = new StringBuilder();

        for (byte b : resultByteArray) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return "";
}




}

























