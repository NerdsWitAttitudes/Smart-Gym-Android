package smartgym.models;


/**
 * Created by robin on 8-5-16.
 */
public class SignUpData {
    String password;
    String password_confirm;
    String email;
    String firstname;
    String lastname;
    String country;

    public SignUpData(String password, String password_confirm, String email, String firstname, String lastname, String country) {
        this.password = password;
        this.password_confirm = password_confirm;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
    }
}
