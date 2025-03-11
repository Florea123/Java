import java.util.Date;

public class Teacher extends Person {
    private String[] proposedProjects;
    public Teacher(String name, String[] proposedProjects, String dateOfBirth) {
        super(name, dateOfBirth);
        this.proposedProjects = proposedProjects;
    }
    public String[] getProposedProjects() {
        return proposedProjects;
    }
    public void setProposedProjects(String[] proposedProjects) {
        this.proposedProjects = proposedProjects;
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public String toString() {
        return super.toString() + " " + proposedProjects;
    }
}
