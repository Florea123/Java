import java.util.*;
import java.time.LocalTime;
public class Schedule {
    private Airport airport;
    private ArrayList<Flight> flights;
    private Map<Flight,String> flightRunwayMap;

    public Schedule(Airport airport) {
        this.airport = airport;
        this.flights = new ArrayList<>();
        this.flightRunwayMap = new HashMap<>();
    }
    public void addFlight(Flight flight) {
        flights.add(flight);
    }
    public Airport getAirport() {
        return airport;
    }
    public ArrayList<Flight> getFlights() {
        return flights;
    }
    public void setAirport(Airport airport) {
        this.airport = airport;
    }
    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
    public void getFlightRunwayMap() {
        for(Map.Entry<Flight,String> entry : flightRunwayMap.entrySet()) {
            Flight flight = entry.getKey();
            String runway = entry.getValue();
            System.out.println("Flight "+flight.getFlightNumber()+" assigned to runway "+runway);
        }
    }
    public void assignFlightsToRunways()
    {
        Map<String,LocalTime> runwayAvailability = new HashMap<>();
        boolean solutionExists = true;
        for(String runway : airport.getRunways())
        {
            runwayAvailability.put(runway,LocalTime.MIN);
        }
        flights.sort((f1,f2)->f1.getLandingInterval().getLandingStart().compareTo(f2.getLandingInterval().getLandingStart()));
        for(Flight flight : flights)
        {
            boolean found = false;
            LocalTime landingStart = flight.getLandingInterval().getLandingStart();
            LocalTime landingEnd = flight.getLandingInterval().getLandingEnd();
            for(String runway : airport.getRunways())
            {
                if(landingStart.isAfter(runwayAvailability.get(runway))||landingStart.equals(runwayAvailability.get(runway)))
                {
                    found=true;
                    flightRunwayMap.put(flight,runway);
                    runwayAvailability.put(runway,landingEnd);
                    break;
                }
            }
            if(!found)
            {
                System.out.println("No runway available for flight "+flight.getFlightNumber());
                solutionExists = false;
                break;
            }
        }
        if(solutionExists) {
            System.out.println("Flights assigned to runways successfully");
        }
        else
        {
            System.out.println("Flights could not be assigned to runways");
        }
    }
    @Override
    public String toString() {
        return "Schedule{" +
                "airport=" + airport +
                ", flights=" + flights +
                '}';
    }
}
