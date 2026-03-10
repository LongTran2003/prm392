package prm392.orderfood.domain.models.auth;

public class Token {
    private String accessToken;
    private String refreshToken;
    private String userRole;
    private String userId;

    public Token() {
        // Default constructor
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUserRole() { return userRole; }
    public String getUserId() { return userId; }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
