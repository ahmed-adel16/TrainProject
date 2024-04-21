
package Train;

import java.security.*;
import java.sql.*;

public class Employee extends Person {
    private String employeeUID; // Unique ID for the employee
    private String password;
    static Connection c = null;
    // Constructor to initialize employee attributes
    public Employee(String name, String tel, int age, String email, String password) {
        super(name, email, age, tel); // Call to parent class constructor
        this.employeeUID = "EMP"; // Generate employee ID    
        this.password = password;
    }

    public String getEmployeeUID() {
        return employeeUID;
    }
    
    public String getPassword() {
        return doHashing(password);
    }
    
    
    public void saveData() throws ClassNotFoundException, SQLException{
            PreparedStatement ps = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Connected");
            String sql = "INSERT INTO Employee (Name, Age, Tel, Email, Password) VALUES (?, ?, ?, ?, ?)";
            ps = c.prepareStatement(sql);
            
            ps.setString(1, this.getName());
            ps.setInt(2, this.getAge());
            ps.setString(3, this.getTel());
            ps.setString(4, this.getEmail());
            ps.setString(5, this.getPassword());
            ps.executeUpdate();
            ps.close();
            c.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
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


