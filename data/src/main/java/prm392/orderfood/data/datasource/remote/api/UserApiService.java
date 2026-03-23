package prm392.orderfood.data.datasource.remote.api;

import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.user.GetUserResponse;
import prm392.orderfood.domain.models.users.CustomerResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import retrofit2.Response;
import retrofit2.http.*;
public interface UserApiService {
    @GET("api/users/{userId}")
    Single<ApiResponse<GetUserResponse>> getUserProfile(@Path("userId") String userId);
    @PUT("api/users")
    Single<ApiResponse<String>> updateUserProfile(@Body UserProfile userProfile);
    @GET("api/users/checkPhoneNumberExists")
    Single<ApiResponse<String>> checkPhoneNumberExists(@Query("phoneNumber") String phoneNumber);
    @GET("api/users/all-cus")
    Single<ApiResponse<List<CustomerResponse>>> getAllCustomers();
}