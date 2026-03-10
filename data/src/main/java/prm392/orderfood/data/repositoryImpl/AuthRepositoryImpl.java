package prm392.orderfood.data.repositoryImpl;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import prm392.orderfood.data.datasource.local.TokenLocalDataSource;
import prm392.orderfood.data.datasource.remote.AuthDataSource;
import prm392.orderfood.data.datasource.remote.modelRequest.LoginRequest;
import prm392.orderfood.data.mapper.TokenMapper;
import prm392.orderfood.data.mapper.UserMapper;
import prm392.orderfood.domain.models.auth.Token;
import prm392.orderfood.domain.models.users.UserRegister;
import prm392.orderfood.domain.repositories.AuthRepository;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthDataSource authDataSource;
    private final TokenLocalDataSource tokenLocalDataSource;

    @Inject
    public AuthRepositoryImpl(AuthDataSource authDataSource, TokenLocalDataSource tokenLocalDataSource) {
        this.authDataSource = authDataSource;
        this.tokenLocalDataSource = tokenLocalDataSource;
    }

    @Override
    public Single<Response<Token>> loginWithGoogle(String idToken) {
        return authDataSource.sendRawGoogleIdToken(idToken)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    boolean success = response.isSuccess();
                    if (success) {
                        Token token = TokenMapper.mapToDomain(response.getData());
                        tokenLocalDataSource.saveToken(token.getAccessToken(), token.getRefreshToken(),
                                token.getUserRole(), token.getUserId());
                        return Response.success(token);
                    } else {
                        ResponseBody errorBody = ResponseBody.create(
                                response.getMessage() != null ? response.getMessage() : "Login failed",
                                (okhttp3.MediaType) null
                        );
                        return Response.error(400, errorBody); // Use appropriate error code if available
                    }
                });
    }

    @Override
    public Completable logOut() {
        // Clear the local token before logging out
        return authDataSource.logOut()
                .andThen(Completable.fromAction(tokenLocalDataSource::clearToken))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Response<Boolean>> validateAccessToken() {
        return Single.fromCallable(() -> {
            try {
                String accessToken = tokenLocalDataSource.getAccessToken();
                boolean isValid = tokenLocalDataSource.isTokenValid(accessToken);
                return Response.success(isValid);
            } catch (Exception e) {
                return Response.error(500, ResponseBody.create("Token validation failed", (okhttp3.MediaType) null));
            }
        });
    }

    @Override
    public String getCurrentUserRole() {
        return tokenLocalDataSource.getUserRole(); // Return null or empty string if no token is available
    }

    @Override
    public Single<Response<String>> registerShopOwner(UserRegister register) {
        return authDataSource.registerShopOwner(UserMapper.mapToRegisterRequest(register))
                .subscribeOn(Schedulers.io())
                .map(apiResponse -> {
                    boolean success = apiResponse.isSuccess();
                    if (success) {
                        return Response.success(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Registration successful");
                    } else {
                        ResponseBody errorBody = ResponseBody.create(
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "Registration failed",
                                (okhttp3.MediaType) null
                        );
                        return Response.error(400, errorBody); // Use appropriate error code if available
                    }
                });
    }

    @Override
    public Single<Response<Token>> shopOwnerLogin(String identifier, String password) {
        return authDataSource.shopOwnerLogin(new LoginRequest(identifier, password))
                .subscribeOn(Schedulers.io())
                .map(apiResponse -> {
                    boolean success = apiResponse.isSuccess();
                    if (success) {
                        Token token = TokenMapper.mapToDomain(apiResponse.getData());
                        tokenLocalDataSource.saveToken(token.getAccessToken(), token.getRefreshToken(),
                                token.getUserRole(), token.getUserId());
                        return Response.success(token);
                    } else {
                        ResponseBody errorBody = ResponseBody.create(
                                apiResponse.getMessage() != null ? apiResponse.getMessage() : "Login failed",
                                (okhttp3.MediaType) null
                        );
                        return Response.error(400, errorBody); // Use appropriate error code if available
                    }
                });
    }
}
