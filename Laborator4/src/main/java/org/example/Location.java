package org.example;

public class Location implements Comparable<Location> {
    private String name;
    private Type type;

    public Location(String name, Type type) {
        this.name = name;
        this.type = type;
    }
    public Type getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    @Override
    public boolean equals(Object object) {
        if(this==object) return true;
        if(object==null || getClass()!=object.getClass()) return false;
        Location location = (Location) object;
        return name.equals(location.name) && type==location.type;
    }
    @Override
    public int compareTo(Location location) {
        return name.compareTo(location.name);
    }
    @Override
    public String toString() {
        return name + " " + type;
    }
}
