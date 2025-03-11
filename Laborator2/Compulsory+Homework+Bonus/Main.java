public class Main {
    public static boolean BktForAllocatingProjects(Student[] students,int k,int start)
    {
        if(k==students.length)
            return true;
        for(int i=start;i<students.length;i++)
        {
            for(Project project:students[i].getProjects())
            {
                if(!project.getIsTaken())
                {
                    project.setIsTaken(true);
                    students[i].setProject(project);
                    if(BktForAllocatingProjects(students,k+1,i+1))
                        return true;
                    project.setIsTaken(false);
                    students[i].setProject((Project)null);
                }
            }
        }
        return false;
    }
    public static void main(String[] args) {
        Project project1 = new Project(Enum.Practical,"P1");
        Project project2 = new Project(Enum.Practical,"P2");
        Project project3 = new Project(Enum.Practical,"P3");
        Project project4 = new Project(Enum.Practical,"P4");
        Student[] students = new Student[4];
        students[0] = new Student("S1",new Project[]{project1,project2},1,"01-01-2001");
        students[1] = new Student("S2",new Project[]{project1,project3},2,"02-02-2002");
        students[2] = new Student("S3",new Project[]{project3,project4},3,"03-03-2003");
        students[3] = new Student("S4",new Project[]{project1,project4},4,"04-04-2004");
        int case1=1;
        if(case1==0)
        {
            for (Student student : students) {
                int ok = 0;
                for (Project project : student.getProjects()) {
                    if (!project.getIsTaken()) {
                        project.setIsTaken(true);
                        student.setProject(project);
                        ok = 1;
                        break;
                    }
                }
                if (ok == 0) {
                    System.out.println(student.getName() + " could not be assigned any project");
                } else
                    System.out.println(student);
            }
        }
        else
        {
            if(BktForAllocatingProjects(students,0,0))
            {
                System.out.println("Solution found");
                for(Student student:students)
                    System.out.println(student);
            }
            else
                System.out.println("No solution");
        }
    }
}