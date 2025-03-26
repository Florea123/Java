public class Airliner extends Aircraft implements PassengerCapable {
    private int passengerCapacity;

    public Airliner(String name,String tailNumber,String model, int passengerCapacity) {
        super(name,tailNumber,model);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
}
