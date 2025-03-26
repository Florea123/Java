public class Freighter extends Aircraft  implements CargoCapable{
    private double maxPayload;

    public Freighter(String name, String tailNumber, String model, double maxPayload) {
        super(name, tailNumber, model);
        this.maxPayload = maxPayload;
    }

    @Override
    public double getMaxPayload() {
        return maxPayload;
    }

}
