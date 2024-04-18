package Train;

import java.util.ArrayList;
import java.util.List;

// Represents a train with attributes like train number, name, and seats
public class Train {
    private static int nextTrainId = 1;
    private String trainNumber;
    private String trainName;
    private int seats;
    private Line line;
    private static List<Train> trains = new ArrayList<>(); // Stores Train's Details

    // Constructor to initialize train attributes
    public Train(String trainName, int seats, Line line) {
        this.trainNumber = "TRN" + nextTrainId++;
        this.trainName = trainName;
        this.line = line;
        this.seats = seats;
        trains.add(this);
    }

    public void removeTrain() {
        trains.remove(this);
    }

    public static void showAllTrains() {
        for (Train train : trains) {
            System.out.println(train);
        }
    }

    public static List<Train> getTrains() {
        return trains;
    }

    
    
    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "\nTrain Name: " + trainName +
                "\nTrain Number: " + trainNumber +
                "\nSeats: " + seats +
                "\nFrom: " + line.getDepartureStation() + " To: " + line.getArrivalStation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Train)) return false;
        Train train = (Train) o;
        return trainNumber.equals(train.trainNumber);
    }

    @Override
    public int hashCode() {
        return trainNumber.hashCode();
    }
}
