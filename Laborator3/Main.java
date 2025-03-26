import java.time.LocalTime;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Airport airport=new Airport("Airport");
        Schedule schedule=new Schedule(airport);
        List<Aircraft> aircraftList = new ArrayList<>();
        aircraftList.add(new Airliner("Boeing 737", "737-800", "N12345", 180));
        aircraftList.add(new Freighter("Boeing 747", "747-400", "N54321", 100000));
        aircraftList.add(new Drone("DJI Phantom 4", "P4-12345", "P4", 2.0, 0.5));
        aircraftList.add(new Airliner("Airbus A320", "A320-200", "N67890", 150));

        airport.addRunway("Runway1");
        airport.addRunway("Runway2");

        Flight flight1 = new Flight("FL123", LocalTime.of(10, 30), LocalTime.of(11, 0));
        Flight flight2= new Flight("FL456", LocalTime.of(11, 30), LocalTime.of(12, 0));
        Flight flight3=new Flight("FL789", LocalTime.of(11, 30), LocalTime.of(12, 0));
        Flight flight4=new Flight("FL978", LocalTime.of(12, 0), LocalTime.of(12, 30));

        schedule.addFlight(flight1);
        schedule.addFlight(flight2);
        schedule.addFlight(flight3);
        schedule.addFlight(flight4);

        schedule.assignFlightsToRunways();
        schedule.getFlightRunwayMap();

    }
}