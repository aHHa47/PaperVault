package no.hiof.ahmedak.papervault.Model;

public class User {

    private String firstName;
    private String lastName;
    private String eMail;
    private String password;



    // User Constructor
    public User(){

    }

    public User(String FirstName,String LastName, String Mail,String Password) {

        this.firstName = FirstName;
        this.lastName = LastName;
        this.eMail = Mail;
        this.password = Password;
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

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", eMail='" + eMail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
