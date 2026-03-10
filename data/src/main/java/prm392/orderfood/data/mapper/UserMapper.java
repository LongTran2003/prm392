package prm392.orderfood.data.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import prm392.orderfood.data.datasource.remote.modelRequest.RegisterRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.user.GetUserResponse;
import prm392.orderfood.domain.models.users.UserProfile;
import prm392.orderfood.domain.models.users.UserRegister;

public class UserMapper {
    public static UserProfile mapToUserProfileDomain(GetUserResponse response) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(response.getUserId());
        userProfile.setFullName(response.getFullName());
        userProfile.setEmail(response.getEmail());
        userProfile.setPhone(response.getPhone());
        userProfile.setAddress(response.getAddress());
        userProfile.setAvatar(response.getAvatar());
        userProfile.setDob(response.getDob());
        userProfile.setWalletBalance(response.getWalletBalance());
        userProfile.setRoleId(response.getRoleId());
        userProfile.setRoleName(response.getRoleName());
        return userProfile;
    }

    public static RegisterRequest mapToRegisterRequest(UserRegister userRegister) {
        RegisterRequest request = new RegisterRequest();
        request.setFullName(userRegister.getFullName());
        request.setUserName(userRegister.getUserName());
        request.setPassword(userRegister.getPassword());
        request.setConfirmPassword(userRegister.getConfirmPassword());
        request.setPhone(userRegister.getPhone());
        request.setEmail(userRegister.getEmail());
        // Assuming address and avatar are optional, you can set them to null or empty if not provided
        request.setAddress(""); // Set to empty string or null if not provided
        request.setAvatar(""); // Set to empty string or null if not provided
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        request.setDob(sdf.format(new Date()));
        return request;
    }
}
