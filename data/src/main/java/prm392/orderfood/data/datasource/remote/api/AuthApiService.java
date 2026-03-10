package prm392.orderfood.data.datasource.remote.api;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelRequest.IdTokenRequest;
import prm392.orderfood.data.datasource.remote.modelRequest.LoginRequest;
import prm392.orderfood.data.datasource.remote.modelRequest.RegisterRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.auth.TokenResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("api/v1/Authentications/student-login")
    Single<ApiResponse<TokenResponse>> sendIdToken(@Body IdTokenRequest idToken);
    @POST("api/v1/Authentications/register-shop-owner")
    Single<ApiResponse<String>> registerShopOwner(@Body RegisterRequest request);
    @POST("api/v1/Authentications/login")
    Single<ApiResponse<TokenResponse>> shopOwnerLogin(@Body LoginRequest request);

}
