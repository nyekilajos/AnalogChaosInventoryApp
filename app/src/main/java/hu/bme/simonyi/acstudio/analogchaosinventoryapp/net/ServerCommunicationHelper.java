package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;

/**
 * Helper class for creating server requests to ac api.
 *
 * @author Lajos_Nyeki
 */
public class ServerCommunicationHelper {

    public static final int HTTP_UNAUTHORIZED = 401;

    private static final String AC_API_ACCOUNT_MODULE = "account/";
    private static final String AC_API_ITEMS_MODULE = "items/";

    private static final String AC_API_LIST_METHOD = "list";
    private static final String AC_API_LOGIN_METHOD = "login";
    private static final String AC_API_LOGOUT_METHOD = "logout";

    private static final String AC_API_ENDPOINT = "https://acstudio.sch.bme.hu/api/";
    private static final String AC_API_VERSION = "1.0/";

    private final ServerCommunicationService serverCommunicationService;

    public ServerCommunicationHelper() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AC_API_ENDPOINT + AC_API_VERSION)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create())).validateEagerly()
                .client(client).build();
        serverCommunicationService = retrofit.create(ServerCommunicationService.class);
    }

    public void listItems(ItemsListRequest itemsListRequest, Callback<ItemsListResponse> callback) {
        serverCommunicationService.listItems(itemsListRequest).enqueue(callback);
    }

    public void login(LoginRequest loginRequest, Callback<LoginResponse> callback) {
        serverCommunicationService.login(loginRequest).enqueue(callback);
    }

    public LoginResponse loginSynchronous(LoginRequest loginRequest) throws IOException {
        return serverCommunicationService.login(loginRequest).execute().body();
    }

    public void logout(LogoutRequest logoutRequest, Callback<LogoutResponse> callback) {
        serverCommunicationService.logout(logoutRequest).enqueue(callback);
    }

    private interface ServerCommunicationService {

        @Headers("Accept: application/json")
        @POST(AC_API_ITEMS_MODULE + AC_API_LIST_METHOD)
        Call<ItemsListResponse> listItems(@Body ItemsListRequest itemsListRequest);

        @Headers("Accept: application/json")
        @POST(AC_API_ACCOUNT_MODULE + AC_API_LOGIN_METHOD)
        Call<LoginResponse> login(@Body LoginRequest loginRequest);

        @Headers("Accept: application/json")
        @POST(AC_API_ACCOUNT_MODULE + AC_API_LOGOUT_METHOD)
        Call<LogoutResponse> logout(@Body LogoutRequest logoutRequest);
    }
}
