package Train;

// Represents a passenger who extends the Person class

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Passenger extends Person {

    private String password;
    // Constructor to initialize passenger attributes
    public Passenger(String name, int age, String tel, String email, String password) {
        super(name, email, age, tel); // Call to parent class constructor
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    // Method to authenticate passenger using email and password
    public static boolean authenticatePassenger(String email, String password) {
        // Retrieve the password associated with the provided email from the database
        String storedPassword = DatabaseManager.getPassengerPassword(email);
        
        // Check if a password was found and if it matches the provided password
        return storedPassword != null && storedPassword.equals(doHashing(password));
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


    // Override toString method for better representation
    @Override
    public String toString() {
        return "{Name: " + getName() + ", Age: " + getAge() + ", Email: " + getEmail() + ", Tel: " + getTel() + "}";
    }

    
}
