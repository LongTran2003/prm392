package prm392.orderfood.androidapp.ui.fragment.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentPhoneInputBinding;
import prm392.orderfood.androidapp.utils.PhoneNumberUtils;
import prm392.orderfood.androidapp.viewModel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneInputFragment extends Fragment {
    private final static String TAG = "PhoneInputFragment";

    private FragmentPhoneInputBinding binding;
    private UserViewModel mUserViewModel;

    private NavController navController;

    public PhoneInputFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PhoneInputFragment newInstance(String param1, String param2) {
        PhoneInputFragment fragment = new PhoneInputFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhoneInputBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireView());
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.btnSendOtp.setOnClickListener(v -> {
            String phoneNumber = binding.etPhone.getText().toString().trim();
            if (!PhoneNumberUtils.isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(requireContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            mUserViewModel.startPhoneVerification(phoneNumber, requireActivity());
        });

        mUserViewModel.getOtpSent().observe(getViewLifecycleOwner(), otpSent -> {
            if (otpSent) {
                navController.navigate(R.id.action_phoneInputFragment_to_otpFragment);

                mUserViewModel.setOtpSent(Boolean.FALSE); // Reset the flag after navigating
            }
        });

        mUserViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}