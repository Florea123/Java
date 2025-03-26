public class Drone extends Aircraft implements CargoCapable{
    private double maxPayload;
    private double batteryLife;

    public Drone(String name, String tailNumber, String model, double maxPayload,double batteryLife) {
        super(name, tailNumber, model);
        this.maxPayload = maxPayload;
        this.batteryLife = batteryLife;
    }

    @Override
    public double getMaxPayload() {
        return maxPayload;
    }
}
