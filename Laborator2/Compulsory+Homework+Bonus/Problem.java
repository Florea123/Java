public class Problem {
    private String description;
    private Person[] persons;
    private int personCount;
    public Problem(String description) {
        this.description = description;
        personCount = 0;
        persons = new Person[10];
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void AddPerson(Person person) {
        if(personCount < persons.length) {
            persons[personCount] = person;
            personCount++;
        }
    }
    public Person[] getPersons() {
        return persons;
    }
    @Override
    public String toString()
    {
        return "Problem{" + "description=" + description + '}';
    }
}
