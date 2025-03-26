package org.example;
import com.github.javafaker.Faker;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.shortestpath.SinglePairShortestPath;
import org.graph4j.util.Path;
import java.util.*;
import org.graph4j.shortestpath.DijkstraShortestPathDefault;
public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i=0; i<30;i++)
        {
            String locationName = faker.address().city();
            Type locationType = Type.values()[faker.random().nextInt(Type.values().length)];
            locations.add(new Location(locationName, locationType));
        }
        TreeSet<Location> friendlyLocations = new TreeSet<Location>();

        locations.stream()
                .filter(location -> location.getType() == Type.FRIENDLY)
                .forEach(friendlyLocations::add);

        System.out.println(friendlyLocations);

        LinkedList<Location> enemyLocations = new LinkedList<Location>();

        locations.stream()
                .filter(location -> location.getType() == Type.ENEMY)
                .sorted(Comparator.comparing(Location::getName))
                .forEach(enemyLocations::add);


        System.out.println(enemyLocations);


        ArrayList<Location> neutralLocations = locations.stream()
                .filter(location -> location.getType() == Type.NEUTRAL)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Location> friendlyLocationsList = locations.stream()
                .filter(location -> location.getType() == Type.FRIENDLY)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Location> enemyLocationsList = locations.stream()
                .filter(location -> location.getType() == Type.ENEMY)
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Time for neutral:" + solveFastestRoute(neutralLocations));
        System.out.println("Time for enemy:" + solveFastestRoute(enemyLocationsList));
        System.out.println("Time for friendly:" + solveFastestRoute(friendlyLocationsList));

        System.out.println("Safety for all locations:" + solveSafestRoute(locations));

        List<TestResult> testResults = generateTests(100,3);
        computeStatistic(testResults);
    }
    public static String solveFastestRoute(ArrayList<Location> locations) {
        Graph graph = GraphBuilder.empty().buildGraph();
        Random random = new Random();

        // Folosim un map pentru a asocia locațiile cu un ID unic
        Map<Location, Integer> locationToId = new HashMap<>();
        int id = 0;

        for (Location location : locations) {
            if (!locationToId.containsKey(location)) {
                locationToId.put(location, id);
                graph.addVertex(id);
                id++;
            }
        }

        // Adăugăm muchiile în graf
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                boolean canMoveDirectly = random.nextBoolean();
                if (canMoveDirectly) {
                    double timeToTravel = 1 + random.nextDouble(10);
                    int id1 = locationToId.get(locations.get(i));
                    int id2 = locationToId.get(locations.get(j));
                    graph.addEdge(id1, id2, timeToTravel);
                }
            }
        }

        // Selectăm o locație de start și calculăm rutele
        Location startLocation = locations.get(random.nextInt(locations.size()));
        int startId = locationToId.get(startLocation);
        StringBuilder response = new StringBuilder();
        var dijkstra = new DijkstraShortestPathDefault(graph, startId);

        for (Location location : locations) {
            if (!location.equals(startLocation)) {
                int destId = locationToId.get(location);
                response.append("\nShortest path from ")
                        .append(startLocation.getName())
                        .append(" to ")
                        .append(location.getName())
                        .append(": ")
                        .append(dijkstra.findPath(destId))
                        .append("\nTotal travel time: ")
                        .append(dijkstra.getPathWeight(destId));
            }
        }
        return response.toString();
    }

    public static String solveSafestRoute(ArrayList<Location> locations) {
        Graph graph = GraphBuilder.empty().buildGraph();
        Random random = new Random();

        // Map pentru a atribui un ID unic fiecărei locații
        Map<Location, Integer> locationToId = new HashMap<>();
        int id = 0;

        for (Location location : locations) {
            if (!locationToId.containsKey(location)) {
                locationToId.put(location, id);
                graph.addVertex(id);
                id++;
            }
        }

        // Adăugăm muchii în funcție de probabilitatea de siguranță
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                boolean canMoveDirectly = random.nextBoolean();
                if (canMoveDirectly) {
                    double probabilityToReachSafely;
                    if (locations.get(i).getType() == Type.FRIENDLY && locations.get(j).getType() == Type.FRIENDLY)
                        probabilityToReachSafely = 0.1;
                    else if (locations.get(i).getType() == Type.ENEMY && locations.get(j).getType() == Type.ENEMY)
                        probabilityToReachSafely = 0.9;
                    else
                        probabilityToReachSafely = 0.5;

                    int id1 = locationToId.get(locations.get(i));
                    int id2 = locationToId.get(locations.get(j));
                    graph.addEdge(id1, id2, probabilityToReachSafely);
                }
            }
        }

        // Selectăm o locație de start și calculăm cea mai sigură rută
        Location startLocation = locations.get(random.nextInt(locations.size()));
        int startId = locationToId.get(startLocation);
        StringBuilder response = new StringBuilder();
        var dijkstra = new DijkstraShortestPathDefault(graph, startId);

        for (Location location : locations) {
            if (!location.equals(startLocation)) {
                int destId = locationToId.get(location);
                response.append("\nSafest path from ")
                        .append(startLocation.getName())
                        .append(" to ")
                        .append(location.getName())
                        .append(": ")
                        .append(dijkstra.findPath(destId))
                        .append("\nProbability to reach safely: ")
                        .append(dijkstra.getPathWeight(destId));
            }
        }
        return response.toString();
    }
    public static List<TestResult> generateTests(int maxLocations,int numberOfGeneratedTests)
    {
        List<TestResult> testResults = new ArrayList<TestResult>();
        for(int i=0;i<numberOfGeneratedTests;i++)
        {
            int numberOfLocations = new Random().nextInt(maxLocations);
            ArrayList<Location> locations = new ArrayList<Location>();
            for(int j=0;j<numberOfLocations;j++)
            {
                String locationName = new Faker().address().city();
                Type locationType = Type.values()[new Random().nextInt(Type.values().length)];
                locations.add(new Location(locationName, locationType));
            }
            String fastestRoute = solveFastestRoute(locations);
            String safestRoute = solveSafestRoute(locations);
            testResults.add(new TestResult(numberOfLocations,fastestRoute,safestRoute));
        }
        return testResults;
    }

    public static void computeStatistic(List<TestResult> testResults)
    {
        double averageLocations=testResults.stream()
                .mapToInt(TestResult::getNumberOfLocations)
                .average()
                .orElse(0);
        int maxLocations = testResults.stream()
                .mapToInt(TestResult::getNumberOfLocations)
                .max()
                .orElse(0);
        int minLocations = testResults.stream()
                .mapToInt(TestResult::getNumberOfLocations)
                .min()
                .orElse(0);
        System.out.println("Average number of locations: "+averageLocations);
        System.out.println("Max number of locations: "+maxLocations);
        System.out.println("Min number of locations: "+minLocations);
    }
}