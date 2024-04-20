
package Train;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.sql.*;


public class Employee extends Person {
    private String employeeUID; // Unique ID for the employee
    private String password;

    private static HashMap <String, String> employeeCredentials = new HashMap<>();
    static Connection c = null;
    // Constructor to initialize employee attributes
    public Employee(String name, String email, int age, String tel, String password) {
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
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        Employee e2 = new Employee("Ahmed", "Ahmed11222@gmail.com", 20, "01029957328", "zaghloul22");
        e2.saveData();
        Employee e3 = new Employee("Zaghloul", "zaghloul222@gmail.com", 19, "01045822345", "zaghloul22");
        e3.saveData();



    }
    
    public void saveData() throws ClassNotFoundException, SQLException{
            PreparedStatement ps = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Connected");
            String sql = "INSERT INTO Employee (Name, Age, Email, Password) VALUES (?, ?, ?, ?)";
            ps = c.prepareStatement(sql);
            
            ps.setString(1, this.getName());
            ps.setInt(2, this.getAge());
            ps.setString(3, this.getEmail());
            ps.setString(4, this.getPassword());
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


