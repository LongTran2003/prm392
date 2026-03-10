package prm392.orderfood.androidapp.ui.fragment.customer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentProfileMenuBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.androidapp.utils.PhoneNumberUtils;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.users.UserProfile;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileMenuBinding binding;
    private UserViewModel mUserViewModel;
    private AuthViewModel mAuthViewModel;
    private NavController navController;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupObservers();
        setupEvents();

        if (mUserViewModel.getUserProfileLiveData().getValue() == null) {
            // Chỉ gọi khi chưa có dữ liệu
            mUserViewModel.fetchUserProfile();
        }
    }

    private void setupObservers() {
        mUserViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                binding.tvUserName.setText(userProfile.getFullName());
                binding.tvUserBio.setText("@" + userProfile.getEmail().split("@")[0]);
                // Load ảnh bằng Glide (bạn cần thêm thư viện Glide vào nếu chưa có)
                Glide.with(this)
                        .load(userProfile.getAvatar()) // phải là URL hoặc uri hợp lệ
                        .placeholder(R.drawable.avatar) // fallback nếu không có ảnh
                        .into(binding.ivUserAvatar);
                if ("ShopOwner".equalsIgnoreCase(userProfile.getRoleName())) {
                    binding.llManageShops.setVisibility(View.VISIBLE);
                    binding.llManageShops.setOnClickListener(v -> {
                        navController.navigate(R.id.myShopListFragment);
                    });
                } else {
                    binding.llManageShops.setVisibility(View.GONE);
                }

                if ("ShopOwner".equalsIgnoreCase(userProfile.getRoleName())) {
                    binding.llWallet.setVisibility(View.VISIBLE);
                    String formatted = CurrencyUtils.formatToVND(userProfile.getWalletBalance());
                    binding.tvWalletBalance.setText(formatted);
                    binding.llPendingOrders.setVisibility(View.GONE);
                } else {
                    binding.llWallet.setVisibility(View.GONE);
                }

                // Hiển thị Card Missing information
                if (userProfile.getPhone() == null || userProfile.getPhone().isEmpty() || userProfile.getAddress() == null || userProfile.getAddress().isEmpty()) {
                    binding.cardViewUpdateRequired.setVisibility(View.VISIBLE);
                } else {
                    binding.cardViewUpdateRequired.setVisibility(View.GONE);
                }
                if (userProfile.getAddress() != null && !userProfile.getAddress().isEmpty()) {
                    binding.llUpdateAddress.setVisibility(View.GONE);
                } else {
                    binding.llUpdateAddress.setVisibility(View.VISIBLE);
                }
                if (userProfile.getPhone() != null && !userProfile.getPhone().isEmpty()) {
                    binding.llUpdatePhone.setVisibility(View.GONE);
                } else {
                    binding.llUpdatePhone.setVisibility(View.VISIBLE);
                }
            }
        });

        mUserViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
//                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "setupObservers: " + errorMessage);
            }
        });

        mUserViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), updateStatus -> {
            if (updateStatus) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                mUserViewModel.fetchUserProfile(); // Refresh thông tin người dùng
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupEvents() {
//        binding.ivBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        binding.ivBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());
        binding.llLogOut.setOnClickListener(v -> {
            mAuthViewModel.logout(() -> {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true) // Clear toàn bộ stack
                        .build();

                NavHostFragment.findNavController(this).navigate(
                        R.id.loginFragment,
                        null,
                        navOptions
                );

                Toast.makeText(requireContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
            });
        });

        binding.llPendingOrders.setOnClickListener(v -> {
            mAuthViewModel.getUserRole().observe(getViewLifecycleOwner(), userRole -> {
                if ("Student".equalsIgnoreCase(userRole)) {
                    //Nav to your Pending Orders Fragment
                    navController.navigate(R.id.action_profileFragment_to_pendingOrdersFragment);
                }
            });
        });

        // setup click listener cho các mục trong profile
        UserProfile userProfile = mUserViewModel.getUserProfileLiveData().getValue();
        // Mục Personal Information
        binding.llPersonalInfo.setOnClickListener(v -> {
            if (userProfile == null) {
                Toast.makeText(requireContext(), "Information not loaded yet, please wait a moment", Toast.LENGTH_SHORT).show();
                return;
            }
//            Check nếu chưa có số điện thoại thì bắt cập nhật
            if (userProfile.getPhone() == null || userProfile.getPhone().isEmpty()) {
                Toast.makeText(requireContext(), "Please update your phone number first", Toast.LENGTH_SHORT).show();
                return;
            }

            toggleVisibility(binding.layoutPersonalInfoDetails, binding.ivArrowPersonalInfo);
            binding.tvNameHidden.setText("Name: " + userProfile.getFullName());
            binding.tvEmailHidden.setText("Email: " + userProfile.getEmail());
            binding.tvPhoneHidden.setText("Phone: " + userProfile.getPhone());
        });

        // Mục Address
        binding.llAddresses.setOnClickListener(v -> {
            if (userProfile == null) {
                Toast.makeText(requireContext(), "Information not loaded yet, please wait a moment", Toast.LENGTH_SHORT).show();
                return;
            }
            // Check nếu chưa có địa chỉ thì bắt cập nhật
            if (userProfile.getAddress() == null || userProfile.getAddress().isEmpty()) {
                Toast.makeText(requireContext(), "Please update your address first", Toast.LENGTH_SHORT).show();
                return;
            }
            toggleVisibility(binding.layoutAddressesDetails, binding.ivArrowAddresses);
            binding.tvAddressHidden.setText("Address: " + userProfile.getAddress());
        });

        // Mục Update Address and Phone
        binding.llUpdateAddress.setOnClickListener(v -> {
            toggleVisibility(binding.layoutAddressForm, binding.ivArrowAddress);
        });

        binding.btnSaveAddress.setOnClickListener(v -> {
            String address = binding.etAddressInput.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                if (userProfile != null) {
                    userProfile.setAddress(address);
//                    mUserViewModel.setUserProfileLiveData(userProfile);
//                    Gson gson = new GsonBuilder().serializeNulls().create();
//                    Log.d("UpdateDebug", gson.toJson(userProfile));
                    mUserViewModel.updateUserProfile(userProfile);
                }

                // Ẩn form sau khi cập nhật
                if (binding.layoutAddressForm.getVisibility() == View.VISIBLE) {
                    toggleVisibility(binding.layoutAddressForm, binding.ivArrowAddress);
                }
            }
        });

        binding.llUpdatePhone.setOnClickListener(v -> {
//            toggleVisibility(binding.layoutPhoneForm, binding.ivArrowPhone);
            navController.navigate(R.id.action_profileFragment_to_phoneInputFragment);
        });

        binding.llWallet.setOnClickListener(v -> {
            if (userProfile == null) {
                Toast.makeText(requireContext(), "Please wait for user data to load", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!"ShopOwner".equalsIgnoreCase(userProfile.getRoleName())) {
                Toast.makeText(requireContext(), "You do not have access to Wallet", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to wallet screen (you'll create this fragment)
            navController.navigate(R.id.walletFragment);
        });
    }

    private void toggleVisibility(View targetLayout, ImageView arrowIcon) {

//        // Áp dụng transition cho parent của target layout
//        ViewGroup parent = (ViewGroup) targetLayout.getParent();
//        TransitionManager.beginDelayedTransition(parent);

        if (targetLayout.getVisibility() == View.GONE) {
            targetLayout.setVisibility(View.VISIBLE);
            arrowIcon.setImageResource(R.drawable.ic_arrow_downward); // icon hướng xuống
        } else {
            targetLayout.setVisibility(View.GONE);
            arrowIcon.setImageResource(R.drawable.ic_arrow_forward); // icon hướng sang
        }
    }
}
