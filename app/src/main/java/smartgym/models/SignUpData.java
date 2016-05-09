package smartgym.models;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by robin on 8-5-16.
 */
public class SignUpData {
    String password;
    String password_confirm;
    String email;
    String first_name;
    String last_name;
    String country;
    String date_of_birth;

    public SignUpData(String password, String password_confirm, String email, String firstname, String lastname, String country, DateTime dateOfBirth) {
        this.password = password;
        this.password_confirm = password_confirm;
        this.email = email;
        this.first_name = firstname;
        this.last_name = lastname;
        this.country = country;
        this.date_of_birth = formatDate(dateOfBirth);
    }

    private String formatDate(DateTime date) {
        // format string for serialization into a Python, UTC compatible date
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return date.toString(dateTimeFormatter);
    }
}
