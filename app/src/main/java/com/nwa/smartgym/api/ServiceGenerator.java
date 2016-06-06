package com.nwa.smartgym.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robin on 19-4-16.
 */
public class ServiceGenerator {

    public static final String baseURL = "http://192.168.1.226:6543/";
    public static final String timePattern = "HH:mm:ssZ";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                @Override
                public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return DateTimeFormat.forPattern(timePattern).parseLocalTime(json.getAsString());
                }
            })
            .registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(DateTimeFormat.forPattern(timePattern).print(src));
                }
            })
            .registerTypeAdapter(LocalDate.Property.class, new JsonDeserializer<LocalDate.Property>() {
                @Override
                public LocalDate.Property deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new LocalDate().withDayOfWeek(json.getAsInt()).dayOfWeek();
                }
            })
            .registerTypeAdapter(LocalDate.Property.class, new JsonSerializer<LocalDate.Property>() {
                @Override
                public JsonElement serialize(LocalDate.Property src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.getLocalDate().getDayOfWeek());
                }
            }).create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createSmartGymService(Class<S> serviceClass){
        return createSmartGymService(serviceClass, null);
    }

    public static <S> S createSmartGymService(Class<S> serviceClass, final String authToken){
        if (authToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("cookie", authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        OkHttpClient client = httpClient
                .build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
