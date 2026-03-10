package prm392.orderfood.domain.repositories;

import io.reactivex.Completable;
import io.reactivex.Single;
import prm392.orderfood.domain.models.auth.Token;
import prm392.orderfood.domain.models.users.UserRegister;
import retrofit2.Response;
/**
 * Interface representing the authentication repository.
 * Provides methods for handling user authentication and session management.
 */
public interface AuthRepository {

    /**
     * Logs in the user by sending the Firebase ID token to the server.
     *
     * @return A Single that emits a Response containing the Token object if successful,
     *         or an error if the login fails.
     */
    Single<Response<Token>> loginWithGoogle(String idToken);

    /**
     * Signs out the currently authenticated user.
     *
     * @return A Task containing a Response object indicating the result of the sign-out operation.
     */
    Completable logOut();

    /**
     * Checks if the access token is valid.
     *
     * @return A Task that emits true if the token is valid, false otherwise.
     */
    Single<Response<Boolean>> validateAccessToken();
    String getCurrentUserRole();
    /**
     * Registers a new shop owner with the provided registration details.
     *
     * @param userRegister The registration request containing user details.
     * @return A Single that emits a Response containing a success message if registration is successful,
     *         or an error if the registration fails.
     */
    Single<Response<String>> registerShopOwner(UserRegister userRegister);
    Single<Response<Token>> shopOwnerLogin(String identifier, String password);
}
