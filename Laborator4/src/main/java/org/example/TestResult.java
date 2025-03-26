package org.example;

public class TestResult {
    private int numberOfLocations;
    private String fastestRoute;
    private String safestRoute;

    public TestResult(int numberOfLocations, String fastestRoute, String safestRoute) {
        this.numberOfLocations = numberOfLocations;
        this.fastestRoute = fastestRoute;
        this.safestRoute = safestRoute;
    }

    public int getNumberOfLocations() {
        return numberOfLocations;
    }

    public String getFastestRoute() {
        return fastestRoute;
    }

    public String getSafestRoute() {
        return safestRoute;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "numberOfLocations=" + numberOfLocations +
                ", fastestRoute='" + fastestRoute + '\'' +
                ", safestRoute='" + safestRoute + '\'' +
                '}';
    }
}