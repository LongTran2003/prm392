package prm392.orderfood.data.repositoryImpl;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.local.TokenLocalDataSource;
import prm392.orderfood.data.datasource.remote.UserDataSource;
import prm392.orderfood.data.mapper.UserMapper;
import prm392.orderfood.domain.models.users.CustomerResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import prm392.orderfood.domain.repositories.UserRepository;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {
    private final UserDataSource userDataSource;
    private final TokenLocalDataSource tokenLocalDataSource;

    @Inject
    public UserRepositoryImpl(UserDataSource userDataSource, TokenLocalDataSource tokenLocalDataSource) {
        this.userDataSource = userDataSource;
        this.tokenLocalDataSource = tokenLocalDataSource;
    }
    @Override
    public Single<Response<UserProfile>> getUserProfile() {
        String userId = tokenLocalDataSource.getUserId();
        return userDataSource.getUserById(userId)
                .map(response -> {
                    boolean success = response.isSuccess();
                    if (success) {
                        return Response.success(UserMapper.mapToUserProfileDomain(response.getData()));
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to fetch user profile";
                        return Response.error(400, okhttp3.ResponseBody.create(errorMessage, null));
                    }
                });
    }

    @Override
    public Single<Response<String>> updateUserProfile(UserProfile userProfile) {
        userProfile.setUserId(tokenLocalDataSource.getUserId());
        return userDataSource.updateUserProfile(userProfile)
                .map(response -> {
                    boolean success = response.isSuccess();
                    if (success) {
                        return Response.success("User profile updated successfully");
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to update user profile";
                        return Response.error(400, okhttp3.ResponseBody.create(errorMessage, null));
                    }
                });
    }

    @Override
    public Single<Response<String>> checkPhoneNumberExists(String phoneNumber) {
        return userDataSource.checkPhoneNumberExists(phoneNumber)
                .map(response -> {
                    // Success = exists
                    // Failure = does not exist
                    boolean success = response.isSuccess();
                    if (success) {
                        if (response.getMessage() != null && response.getMessage().equals("Phone number already exists")) {
                            return Response.success("Phone number already exists");
                        } else {
                            return Response.success("Phone number does not exist");
                        }
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Phone number does not exist";
                        return Response.error(400, okhttp3.ResponseBody.create(errorMessage, null));
                    }
                });
    }

    @Override
    public Single<List<CustomerResponse>> getAllCustomers() {
        return userDataSource.getAllCustomers()
                .map(response -> {
                    if (response.isSuccess()) {
                        return response.getData();
                    } else {
                        throw new RuntimeException("Failed to fetch customers: " + response.getMessage());
                    }
                });
    }
}
