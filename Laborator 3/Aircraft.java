public class Aircraft {
    private String name;
    private String tailNumber;
    private String model;

    public Aircraft(String name, String tailNumber, String model) {
        this.name = name;
        this.tailNumber = tailNumber;
        this.model = model;
    }
    public String getName() {
        return name;
    }
    public String getTailNumber() {
        return tailNumber;
    }
    public String getModel(){
        return model;
    }
    @Override
    public String toString() {
        return "Aircraft{" +
                "name='" + name + '\'' +
                ", tailNumber=" + tailNumber +
                ", model='" + model + '\'' +
                '}';
    }
}
