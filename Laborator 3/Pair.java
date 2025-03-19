
public class Pair<t1> {
    private t1 landingStart;
    private t1 landingEnd;

    public Pair(t1 landingStart, t1 landingEnd) {
        this.landingStart = landingStart;
        this.landingEnd = landingEnd;
    }
    public t1 getLandingStart() {
        return landingStart;
    }
    public t1 getLandingEnd(){
        return landingEnd;
    }
    @Override
    public String toString() {
        return "Pair{" +
                "landingStart=" + landingStart +
                ", landingEnd=" + landingEnd +
                '}';
    }
}
