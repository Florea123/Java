public class Solution {
    private String details;
    public Solution(String details) {
        this.details = details;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    @Override
    public String toString()
    {
        return "Solution{" + "details=" + details + '}';
    }
}
