import java.util.Objects;

public class Project {
    private Enum type;
    private String title;
    private Teacher proposedBy;
    private boolean isTaken;
    public Project(Enum type,String title) {
        this.type = type;
        this.title = title;
        isTaken = false;
    }
    public Enum getType() {
        return type;
    }
    public void setType(Enum type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Teacher getproposedBy() {
        return proposedBy;
    }
    public void setproposedBy(Teacher proposedBy) {
        this.proposedBy = proposedBy;
    }
    public boolean getIsTaken() {
        return isTaken;
    }
    public void setIsTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Project project = (Project) obj;
        return Objects.equals(title, project.title) && type == project.type && Objects.equals(proposedBy, project.proposedBy);
    }
    @Override
    public int hashCode() {
        return Objects.hash(type, title, proposedBy);
    }
    @Override
    public String toString()
    {
        return title;
    }
}
