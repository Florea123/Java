import java.time.LocalTime;
public class Flight {
    private String flightNumber;
    private Pair<LocalTime> landingInterval;

    public Flight(String flightNumber, LocalTime landingStart, LocalTime landingEnd) {
        this.flightNumber = flightNumber;
        this.landingInterval= new Pair<>(landingStart, landingEnd);
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public Pair<LocalTime> getLandingInterval() {
        return landingInterval;
    }
    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", landingInterval=" + landingInterval +
                '}';
    }
}
