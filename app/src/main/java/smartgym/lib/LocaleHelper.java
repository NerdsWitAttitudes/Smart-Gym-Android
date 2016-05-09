package smartgym.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by robin on 9-5-16.
 */
public class LocaleHelper {
    public static List<String> getCountryNames() {
        List<String> isoCountryCodes = new ArrayList<>();
        for (String countryCode : Locale.getISOCountries()) {
            // Make locale of countrycode and get the display name
            isoCountryCodes.add(new Locale("", countryCode).getDisplayCountry());
        }

        return isoCountryCodes;
    }
}
