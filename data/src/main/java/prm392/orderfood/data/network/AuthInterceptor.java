package prm392.orderfood.data.network;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import prm392.orderfood.data.datasource.local.TokenLocalDataSource;

public class AuthInterceptor implements Interceptor {
    private final TokenLocalDataSource tokenLocalDataSource;

    public AuthInterceptor(TokenLocalDataSource tokenLocalDataSource) {
        this.tokenLocalDataSource = tokenLocalDataSource;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = tokenLocalDataSource.getAccessToken();
        Request.Builder builder = chain.request().newBuilder();
        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return chain.proceed(builder.build());
    }
}
