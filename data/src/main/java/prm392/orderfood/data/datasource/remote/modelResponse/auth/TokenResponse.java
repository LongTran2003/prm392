package prm392.orderfood.data.datasource.remote.modelResponse.auth;

public class TokenResponse {
    private String accessToken;
    private String refreshToken; // Nếu BE có cấp thêm refreshToken
    private String userRole;     // Nếu bạn muốn biết role (student, shop, admin)
    private String userId;


    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUserRole() { return userRole; }
    public String getUserId() { return userId;}
}
