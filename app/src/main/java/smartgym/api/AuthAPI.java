package smartgym.api;

import smartgym.models.HTTPResponse;
import smartgym.models.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by robin on 19-4-16.
 */
public interface AuthAPI {
    @POST("auth/login")
    Call<HTTPResponse> logIn(
            @Body Login login
    );
}
