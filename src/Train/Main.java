package Train;
import java.util.Scanner;
import java.sql.*;
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Employee e = new Employee("Jimmy", "015", 30, "jimmy191@hotmail.com" , "jimmy");
        e.saveData();
//        firstScene();
      
    }
}