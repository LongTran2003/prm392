package prm392.orderfood.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import prm392.orderfood.domain.models.auth.Token;
import prm392.orderfood.domain.models.users.UserRegister;
import prm392.orderfood.domain.repositories.AuthRepository;
import retrofit2.Response;

public class AuthUseCase {
    private final AuthRepository authRepository;

    @Inject
    public AuthUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Single<Response<Token>> loginWithGoogle(String idToken) {
        return authRepository.loginWithGoogle(idToken);
    }

    public Completable signOut() {
        return authRepository.logOut();
    }

    public  Single<Response<Boolean>> validateAccessToken() {
        return authRepository.validateAccessToken();
    }

    public Single<Response<String>> registerShopOwner(UserRegister userRegister) {
        return authRepository.registerShopOwner(userRegister);
    }

    public Single<Response<Token>> shopOwnerLogin(String identifier, String password) {
        return authRepository.shopOwnerLogin(identifier, password);
    }

    public String getCurrentUserRole() {
        return authRepository.getCurrentUserRole();
    }
}
