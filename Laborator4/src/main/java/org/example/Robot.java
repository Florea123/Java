package org.example;

public class Robot {
    private String name;
    private Location location;

    Robot(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    @Override
    public String toString() {
        return name+ " " + location;
    }
}
