package prm392.orderfood.domain.repositories;

import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.domain.models.users.CustomerResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import retrofit2.Response;

public interface UserRepository {
    /**
     * Retrieves the user profile for a given user ID.
     *
     * @return A Single that emits a Response containing the UserProfile object if successful,
     *         or an error if the retrieval fails.
     */
    Single<Response<UserProfile>> getUserProfile();

    /**
     * Updates the user profile with the provided UserProfile object.
     *
     * @param userProfile The UserProfile object containing updated user information.
     * @return A Single that emits a Response containing the updated UserProfile object if successful,
     *         or an error if the update fails.
     */
    Single<Response<String>> updateUserProfile(UserProfile userProfile);
    Single<Response<String>> checkPhoneNumberExists(String phoneNumber);
    Single<List<CustomerResponse>> getAllCustomers();
}
