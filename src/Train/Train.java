package Train;

// Represents a train with attributes like train number, name, and seats
public class Train {
    private int trainNumber;
    private String trainName;
    private int seats;
    private Line line;
    private String departureStation;
    private String arrivalStation;
    private int ticketsLeft;

    // Constructor to initialize train attributes
    public Train(int trainNumber, String trainName, int seats, Line line, int ticketsLeft) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.seats = seats;
        this.line = line;
        this.ticketsLeft = ticketsLeft;
    }

    // In the Train class
    public int getTicketsLeft() {
        return ticketsLeft;
    }



    public void setTicketsLeft(int ticketsLeft) {
        this.ticketsLeft = ticketsLeft;
        // Update the database with the new number of tickets left
        DatabaseManager.updateTicketsLeft(trainNumber, ticketsLeft);
    }


    public int getTrainNumber() {
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
        String lineInfo = (line != null) ? "\nFrom: " + line.getDepartureStation() + " To: " + line.getArrivalStation() : "";
        return "\nTrain Name: " + trainName +
                "\nTrain Number: " + trainNumber +
                "\nSeats: " + seats +
                lineInfo;
    }

    public String getDepartureStation() {
        
        return line.getDepartureStation();
    }

    public String getArrivalStation() {
        return line.getArrivalStation();
    }
    

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }
    
    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }








}


