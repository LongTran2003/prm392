package prm392.orderfood.androidapp.viewModel;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
import retrofit2.HttpException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.androidapp.ui.states.SignInState;
import prm392.orderfood.androidapp.ui.states.SignUpState;
import prm392.orderfood.domain.models.auth.Token;
import prm392.orderfood.domain.models.users.UserRegister;
import prm392.orderfood.domain.usecase.AuthUseCase;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthUseCase mAuthUseCase;

    private final CompositeDisposable mCompositeDisposable;

    private final MutableLiveData<SignInState> mSignInState = new MutableLiveData<>(new SignInState.Idle());
    private final MutableLiveData<String> navigateTo = new MutableLiveData<>();
    private final MutableLiveData<SignUpState> mSignUpState = new MutableLiveData<>(new SignUpState.Idle());

    public LiveData<SignInState> getSignInState() {
        return mSignInState;
    }

    public LiveData<String> getNavigateTo() {
        return navigateTo;
    }

    public LiveData<SignUpState> getSignUpState() {
        return mSignUpState;
    }

    // MutableLiveData để lưu trữ role của user hiện tại sau khi login
    private MutableLiveData<String> userRole = new MutableLiveData<>();
    public LiveData<String> getUserRole() {
        return userRole;
    }

    @Inject
    public AuthViewModel(AuthUseCase authUseCase) {
        this.mAuthUseCase = authUseCase;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void loginWithGoogle(String idToken) {
        mSignInState.setValue(new SignInState.Loading());

        Disposable disposable = mAuthUseCase.loginWithGoogle(idToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful() && response.body() != null) {
                                Token token = response.body();
                                mSignInState.setValue(new SignInState.Success(token));
                                userRole.setValue("Student");
                            } else if (response.errorBody() != null) {
                                mSignInState.setValue(new SignInState.Error(response.errorBody().string()));
                            } else {
                                mSignInState.setValue(new SignInState.Error("Unknown error"));
                            }
                        },
                        throwable -> {
                            String message = throwable.getMessage();
                            if (message == null || message.isEmpty()) {
                                message = throwable.toString(); // fallback để log ra tên class của exception
                            }
                            mSignInState.setValue(new SignInState.Error(message));
                        });
        mCompositeDisposable.add(disposable);
    }

    public void loginShopOwner(String identifier, String password) {
        mSignInState.setValue(new SignInState.Loading());

        Disposable disposable = mAuthUseCase.shopOwnerLogin(identifier, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Token token = response.body();
                        mSignInState.setValue(new SignInState.Success(token)); // Set user role to ShopOwner
                        String roleFromToken = token.getUserRole();
                        if (roleFromToken == null || roleFromToken.trim().isEmpty()) {
                            roleFromToken = "ShopOwner";
                        }
                        userRole.setValue(roleFromToken);
                    } else if (response.errorBody() != null) {
                        mSignInState.setValue(new SignInState.Error(response.errorBody().string()));
                    } else {
                        mSignInState.setValue(new SignInState.Error("Unknown error"));
                    }
                }, throwable -> {
                    String message = throwable.getMessage();
                    if (message == null || message.isEmpty()) {
                        message = throwable.toString(); // fallback để log ra tên class của exception
                    }
                    mSignInState.setValue(new SignInState.Error(message));
                });

        mCompositeDisposable.add(disposable);
    }

    public void checkAuth() {
        Disposable disposable = mAuthUseCase.validateAccessToken()
                .subscribeOn(Schedulers.io()) // Run on background thread
                .observeOn(AndroidSchedulers.mainThread()) // Observe on main thread for UI
                .subscribe(response -> {
                    if (Boolean.TRUE.equals(response.body())) {
                        String currentRole = mAuthUseCase.getCurrentUserRole();
                        userRole.setValue(currentRole);

                        if ("ShopOwner".equalsIgnoreCase(currentRole)) {
                            navigateTo.setValue("shopList");
                        } else if ("Admin".equalsIgnoreCase(currentRole)) {
                            navigateTo.setValue("adminShopTab");
                        } else if ("Student".equalsIgnoreCase(currentRole)) {
                            navigateTo.setValue("home");
                        } else {
                            Log.e("AuthViewModel", "Unknown user role: " + currentRole);
                            navigateTo.setValue("login");
                        }
                    } else {
                        navigateTo.setValue("login");
                    }
                }, throwable -> {
                    navigateTo.setValue("login");
                });
        mCompositeDisposable.add(disposable);
    }

    public void logout(Runnable onSuccess) {
        Disposable disposable = mAuthUseCase.signOut()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mSignInState.setValue(null);
                    userRole.setValue(null);
                    onSuccess.run(); // gọi callback logout thành công
                }, throwable -> {
                    // Optional: xử lý lỗi nếu cần
                });

        mCompositeDisposable.add(disposable);
    }

    public void registerShopOwner(UserRegister register) {
        mSignUpState.setValue(new SignUpState.Loading());

        Disposable disposable = mAuthUseCase.registerShopOwner(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful() && response.body() != null) {
                        String successMsg = response.body();
                        mSignUpState.setValue(new SignUpState.Success(successMsg));
                    } else if (response.errorBody() != null) {
                        String errorMessage = response.errorBody().string();
                        mSignUpState.setValue(new SignUpState.Error(errorMessage));
                    } else {
                        mSignUpState.setValue(new SignUpState.Error("Unknown error"));
                    }
                }, throwable -> {
                    String message = parseRegisterError(throwable);
                    mSignUpState.setValue(new SignUpState.Error(message));
                });

        mCompositeDisposable.add(disposable);
    }
    // Helper method to update state on Google Sign-In UI failure
    public void handleSignInError(String errorMessage) {
        mSignInState.setValue(new SignInState.Error(errorMessage));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear(); // Giải phóng bộ nhớ khi ViewModel bị hủy
    }

    private String parseRegisterError(Throwable throwable) {
        try {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.response() != null && httpException.response().errorBody() != null) {
                    String raw = httpException.response().errorBody().string();

                    if (raw == null || raw.isEmpty()) {
                        return "HTTP " + httpException.code() + " Bad Request";
                    }

                    JSONObject obj = new JSONObject(raw);

                    String message = obj.optString("message", "");
                    if (!message.isEmpty()) {
                        return message;
                    }

                    if (obj.has("errors")) {
                        JSONObject errors = obj.getJSONObject("errors");
                        Iterator<String> keys = errors.keys();
                        if (keys.hasNext()) {
                            String firstKey = keys.next();
                            JSONArray arr = errors.optJSONArray(firstKey);
                            if (arr != null && arr.length() > 0) {
                                return arr.optString(0);
                            }
                        }
                        return "Invalid input data";
                    }

                    return raw;
                }
            }
        } catch (Exception ignored) {
        }

        String fallback = throwable.getMessage();
        return (fallback == null || fallback.isEmpty()) ? throwable.toString() : fallback;
    }
}
