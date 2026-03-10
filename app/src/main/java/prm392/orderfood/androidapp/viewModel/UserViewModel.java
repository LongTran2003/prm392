package prm392.orderfood.androidapp.viewModel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import prm392.orderfood.androidapp.utils.PhoneNumberUtils;
import prm392.orderfood.domain.models.users.CustomerResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import prm392.orderfood.domain.usecase.UserUseCase;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserUseCase userUseCase;
    private final CompositeDisposable mCompositeDisposable;

    // MutableLiveData để lưu trữ thông tin người dùng
    private final MutableLiveData<UserProfile> userProfileLiveData = new MutableLiveData<>();

    // LiveData để quan sát thông tin người prdùng
    public LiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    // MutableLiveData để lưu trữ thông báo lỗi
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // MutableLiveData để lưu trữ trạng thái cập nhật thông tin người dùng
    public MutableLiveData<Boolean> updateStatus = new MutableLiveData<>(Boolean.FALSE);

    public LiveData<Boolean> getUpdateStatus() {
        return updateStatus;
    }

    private final MutableLiveData<List<CustomerResponse>> customerResponseLiveData = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<CustomerResponse>> getCustomerResponseLiveData() {
        return customerResponseLiveData;
    }

    // MutableLiveData để lưu trữ trạng thái gửi OTP
    public MutableLiveData<Boolean> otpSent = new MutableLiveData<>(Boolean.FALSE);

    public LiveData<Boolean> getOtpSent() {
        return otpSent;
    }

    public void setOtpSent(Boolean otpSent) {
        this.otpSent.setValue(otpSent);
    }

    // Firebase Auth instance
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    // LiveData for save phone number input
    public final MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    // Phương thức để lấy thông tin người dùng
    public void fetchUserProfile() {
        Disposable disposable = userUseCase.getUserProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> userProfileLiveData.setValue(response.body()),
                        throwable -> {
                            // Xử lý lỗi nếu cần
                            userProfileLiveData.setValue(null);
                        }
                );
        mCompositeDisposable.add(disposable);
    }

    public void updateUserProfile(UserProfile userProfile) {
        Disposable disposable = userUseCase.updateUserProfile(userProfile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            userProfileLiveData.setValue(userProfile);// Cập nhật lại thông tin người dùng sau khi cập nhật thành công
                            updateStatus.setValue(Boolean.TRUE); // Cập nhật trạng thái thành công
                        },
                        throwable -> {
                            // Xử lý lỗi nếu cần
                            errorMessage.setValue("Update failed: " + throwable.getMessage());
                        }
                );
        mCompositeDisposable.add(disposable);
    }

    public void startPhoneVerification(String phoneNumber, Activity activity) {
        Disposable disposable = userUseCase.checkPhoneNumberExists(phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            // Xử lý kết quả kiểm tra số điện thoại
                            if (response.body() != null && !response.body().isEmpty()) {
                                if (response.body().equals("Phone number already exists")) {
                                    errorMessage.setValue("Phone number already exists");
                                } else {
                                    // Handle Send OTP logic here
                                    if (!PhoneNumberUtils.isValidPhoneNumber(phoneNumber)) {
                                        errorMessage.setValue("Invalid Phone Number");
                                        return;
                                    }

                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                                            .setPhoneNumber(Objects.requireNonNull(PhoneNumberUtils.formatVietnamesePhone(phoneNumber))) // Số điện thoại đã được định dạng
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(activity)
                                            .setCallbacks(callbacks)
                                            .build();

                                    PhoneAuthProvider.verifyPhoneNumber(options);
//                                otpSent.setValue(Boolean.TRUE); // Giả sử gửi OTP thành công
                                }
                            }
                        },
                        throwable -> {
                            // Xử lý lỗi nếu cần
                            errorMessage.setValue("Check phone number failed");
                        }
                );
        mCompositeDisposable.add(disposable);
    }

    /**
     * Xác minh mã OTP người dùng nhập
     */
    public void verifyOtpCode(String code) {
        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            errorMessage.setValue("Verification ID is null");
        }
    }

    /**
     * Gửi lại OTP nếu cần
     */
    public void resendOtp(Activity activity) {
        String phone = phoneNumber.getValue();
        if (phone == null || phone.isEmpty()) {
            errorMessage.setValue("Invalid phone number");
            return;
        }

        startPhoneVerification(phone, activity);
    }

    // Phương thức để gửi OTP
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    signInWithPhoneAuthCredential(credential); // auto verify
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    errorMessage.setValue("Verification failed: " + e.getMessage());
                }

                @Override
                public void onCodeSent(@NonNull String verifId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    verificationId = verifId;
                    resendToken = token;
                    otpSent.setValue(true); // navigate to OTP screen
                }
            };

    // Phương thức để bắt đầu xác minh số điện thoại
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        String phone = firebaseUser.getPhoneNumber();
                        // Dùng phone này để cb update xuống DB
                        // Chuyển phoneNumber +84 sang 0
                        if (phone != null && phone.startsWith("+84")) {
                            phone = "0" + phone.substring(3);
                        }
                        UserProfile profile = userProfileLiveData.getValue();
                        if (profile != null) {
                            profile.setPhone(phone);
                            updateUserProfile(profile);
                        } else {
                            errorMessage.setValue("User profile is not loaded yet");
                        }
                        updateUserProfile(userProfileLiveData.getValue()); // Cập nhật thông tin người dùng với số điện thoại mới

                        // Xác thực thành công rồi → logout luôn
                        firebaseAuth.signOut();
                    } else {
                        errorMessage.setValue("OTP verification failed");
                    }
                });
    }


    public void getAllCustomers() {
        Disposable disposable = userUseCase.getAllCustomers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        customers -> {
                            if (customers != null && !customers.isEmpty()) {
                                customerResponseLiveData.setValue(customers);
                            }
                        },
                        throwable -> {
                            errorMessage.setValue("Failed to fetch customers: " + throwable.getMessage());
                        }
                );
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear(); // Giải phóng bộ nhớ khi ViewModel bị hủy
    }

}
