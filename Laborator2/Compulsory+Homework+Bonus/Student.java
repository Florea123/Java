import java.util.Date;
import java.util.Objects;

public class Student extends Person{
    private int registrationNumber;
    private Project[] projects;
    private Project project;
    public Student(String name, Project[] project, int registrationNumber, String dateOfBirth) {
        super(name, dateOfBirth);
        this.projects = project;
        this.registrationNumber = registrationNumber;
        project=null;
    }
    public Project[] getProjects() {
        return projects;
    }
    public void setProject(Project[] projects) {
        this.projects = projects;
    }
    public int getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return Objects.equals(registrationNumber, student.registrationNumber);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), registrationNumber);
    }
    @Override
    public String toString() {
        return super.toString() + " : " + project.toString();
    }
}
