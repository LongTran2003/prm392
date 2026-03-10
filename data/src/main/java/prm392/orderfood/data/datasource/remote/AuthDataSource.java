package prm392.orderfood.data.datasource.remote;

import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.data.datasource.local.TokenLocalDataSource;
import prm392.orderfood.data.datasource.remote.api.AuthApiService;
import prm392.orderfood.data.datasource.remote.modelRequest.IdTokenRequest;
import prm392.orderfood.data.datasource.remote.modelRequest.LoginRequest;
import prm392.orderfood.data.datasource.remote.modelRequest.RegisterRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.auth.TokenResponse;
import retrofit2.Response;

public class AuthDataSource {
    private final FirebaseAuth firebaseAuth;
    private final AuthApiService apiService;

    @Inject
    public AuthDataSource(FirebaseAuth firebaseAuth, AuthApiService apiService) {
        this.firebaseAuth = firebaseAuth;
        this.apiService = apiService;
    }

    public Single<ApiResponse<TokenResponse>> sendTokenToServer() {
        return getFirebaseIdToken()
                .doOnError(throwable -> Log.e("TOKEN_ERROR", "Lỗi khi lấy Firebase ID Token: " + throwable.getMessage(), throwable))
                .flatMap(idToken -> {
                    Log.d("TOKEN_DEBUG", "Firebase ID Token: " + idToken);
                    return apiService.sendIdToken(new IdTokenRequest(idToken));
                });
    }

    public Completable logOut() {
        return Completable.fromAction(firebaseAuth::signOut).subscribeOn(Schedulers.io());
    }

    public Single<ApiResponse<String>> registerShopOwner(RegisterRequest request) {
        return apiService.registerShopOwner(request)
                .subscribeOn(Schedulers.io());
    }

    public Single<ApiResponse<TokenResponse>> shopOwnerLogin(LoginRequest request) {
        return apiService.shopOwnerLogin(request)
                .subscribeOn(Schedulers.io());
    }

    private Single<String> getFirebaseIdToken() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            return Single.error(new Throwable("User not logged in"));
        }

//        if (user.getEmail() == null || !user.getEmail().endsWith(".edu.vn")) {
//            return Single.error(new Throwable("Only .edu.vn emails allowed"));
//        }

        return Single.fromCallable(() -> {
            // Chạy Blocking để lấy Token một cách đồng bộ
            return Tasks.await(user.getIdToken(true), 10, TimeUnit.SECONDS).getToken();
        }).subscribeOn(Schedulers.io());
    }

    public Single<ApiResponse<TokenResponse>> sendRawGoogleIdToken(String idToken) {
        Log.d("TOKEN_RAW", "Google ID Token: " + idToken);
        return apiService.sendIdToken(new IdTokenRequest(idToken));
    }
}
