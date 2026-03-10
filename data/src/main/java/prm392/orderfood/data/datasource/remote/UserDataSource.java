package prm392.orderfood.data.datasource.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.api.UserApiService;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.user.GetUserResponse;
import prm392.orderfood.domain.models.users.CustomerResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import retrofit2.Response;

public class UserDataSource {
    private final UserApiService userApiService;

    @Inject
    public UserDataSource(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    public Single<ApiResponse<GetUserResponse>> getUserById(String userId) {
        return userApiService.getUserProfile(userId);
    }

    public Single<ApiResponse<String>> updateUserProfile(UserProfile userProfile) {
        return userApiService.updateUserProfile(userProfile);
    }

    public Single<ApiResponse<String>> checkPhoneNumberExists(String phoneNumber) {
        return userApiService.checkPhoneNumberExists(phoneNumber);
    }

    public Single<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        return userApiService.getAllCustomers();
    }


}
