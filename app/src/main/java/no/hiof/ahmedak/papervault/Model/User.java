package no.hiof.ahmedak.papervault.Model;


public class User {

    private String firstName;
    private String lastName;
    private String eMail;
    private String user_id;



    // User Constructor
    public User(){

    }

    public User(String firstName, String lastName, String eMail, String user_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

// Getter And Setter for our Class.

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String geteMail() {
        return eMail;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", eMail='" + eMail + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
