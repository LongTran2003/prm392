package prm392.orderfood.data.datasource.local;

import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;

import java.util.Date;

import javax.inject.Inject;

public class TokenLocalDataSource {
    private final SharedPreferences sharedPreferences;

    @Inject
    public TokenLocalDataSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveToken(String accessToken, String refreshToken, String userRole, String userId) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", accessToken).apply();
        sharedPreferences.edit().putString("REFRESH_TOKEN", refreshToken).apply();
        sharedPreferences.edit().putString("USER_ROLE", userRole).apply();
        sharedPreferences.edit().putString("USER_ID", userId).apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString("ACCESS_TOKEN", null);
    }
    public String getRefreshToken() {
        return sharedPreferences.getString("REFRESH_TOKEN", null);
    }
    public String getUserRole() {
        return sharedPreferences.getString("USER_ROLE", null);
    }
    public String getUserId() {
        return sharedPreferences.getString("USER_ID", null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove("ACCESS_TOKEN").apply();
        sharedPreferences.edit().remove("REFRESH_TOKEN").apply();
        sharedPreferences.edit().remove("USER_ROLE").apply();
        sharedPreferences.edit().remove("USER_ID").apply();
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            JWT jwt = new JWT(token);
            Date expiresAt = jwt.getExpiresAt();
            return expiresAt != null && expiresAt.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
