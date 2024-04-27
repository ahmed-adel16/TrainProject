package Train;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Employee extends Person {

    private String password;
    public Employee(String name, String email, int age, String tel, String password) {
        super(name, email, age, tel);
        this.password = password;
    }

    public static boolean authenticateEmployee(String email, String password) {
        String storedPassword = DatabaseManager.getEmployeePassword(email);
        return storedPassword != null && storedPassword.equals(doHashing(password));
    }

    public String getPassword() {
        return password;
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
