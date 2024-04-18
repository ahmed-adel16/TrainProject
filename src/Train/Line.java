package Train;

// Represents a line or route between two stations
public class Line  {
    private String departureStation; // Departure station of the line
    private String arrivalStation; // Arrival station of the line

    // Constructor to initialize line attributes
    public Line(String departureStation, String arrivalStation) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    // Getter method to retrieve departure station
    public String getDepartureStation() {
        return departureStation;
    }

    // Getter method to retrieve arrival station
    public String getArrivalStation() {
        return arrivalStation;
    }
}
