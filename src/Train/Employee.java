
package Train;

import java.util.HashMap;


public class Employee extends Person {
    private static int nextEmployeeId = 1; // Static variable to generate unique employee IDs
    private String employeeId; // Unique ID for the employee
    private static HashMap <String, String> employeeCredentials = new HashMap<>();
    // Constructor to initialize employee attributes
    public Employee(String name, String email, int age, String tel, String password) {
        super(name, email, age, tel); // Call to parent class constructor
        this.employeeId = "EMP" + nextEmployeeId++; // Generate employee ID
        registerEmployee(name, email, age, tel, password);
    }
    public static boolean authenticateEmployee(String email, String password) {
        String storedPassword = employeeCredentials.get(email);
        return storedPassword != null && storedPassword.equals(password);
    }
        
    public static boolean registerEmployee(String name, String email, int age, String tel, String password) {
            // Check if the email is already in use
            if (employeeCredentials.containsKey(email)) {
                System.out.println("Email is already registered.");
                return false;
            }
            // Add the new employee to the credentials map
            employeeCredentials.put(email, password);
            System.out.println("Employee registered successfully.");
            return true;
    }
}


