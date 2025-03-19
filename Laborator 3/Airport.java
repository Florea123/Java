import java.util.*;
public class Airport {
    private String name;
    private List<String> runways = new ArrayList<>();
    public Airport(String name) {
        this.name = name;
    }
    public void addRunway(String runway) {
        runways.add(runway);
    }
    public String getName() {
        return name;
    }
    public List<String> getRunways() {
        return runways;
    }
    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", runways=" + runways +
                '}';
    }
}
