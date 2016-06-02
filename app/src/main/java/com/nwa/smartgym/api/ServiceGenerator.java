package com.nwa.smartgym.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
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
    public static final String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder()
                                    .registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
                                        @Override
                                        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                            return DateTimeFormat.forPattern(dateTimePattern).parseDateTime(json.getAsString());
                                        }
                                    })
                                    .registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
                                        @Override
                                        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
                                            return new JsonPrimitive(DateTimeFormat.forPattern(dateTimePattern).print(src));
                                        }
                                    })
                                .create()
                ));

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
