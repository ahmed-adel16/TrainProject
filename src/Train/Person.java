package Train;


// Represents a person with common attributes like name, email, age, and telephone
public class Person {
    private String name; // Name of the person
    private String email; // Email of the person
    private String tel; // Telephone number of the person
    private int age; // Age of the person

    // Constructor to initialize person attributes
    public Person(String name, String email, int age, String tel) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public int getAge() {
        return age;
    }
    
    
}
