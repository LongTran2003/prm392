package prm392.orderfood.androidapp.ui.fragment.auth;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentOtpBinding;
import prm392.orderfood.androidapp.viewModel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtpFragment extends Fragment {
    private final static String TAG = "OtpFragment";
    private FragmentOtpBinding binding;
    private UserViewModel mUserViewModel;
    private NavController navController;

    public OtpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OtpFragment newInstance(String param1, String param2) {
        OtpFragment fragment = new OtpFragment();
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
        binding = FragmentOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // Initialize NavController
        navController = Navigation.findNavController(requireView());

        binding.btnVerify.setEnabled(true);

        binding.btnVerify.setOnClickListener(v -> {
            String otpCode = binding.otpView.getText().toString().trim();
            if (otpCode.length() < 6) {
                Toast.makeText(requireContext(), "Please enter a valid OTP code", Toast.LENGTH_SHORT).show();
                return;
            }

            mUserViewModel.verifyOtpCode(otpCode);
            binding.btnVerify.setEnabled(false);
        });

        mUserViewModel.errorMessage.observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                binding.btnVerify.setEnabled(true);
            }
        });


        mUserViewModel.updateStatus.observe(getViewLifecycleOwner(), updateStatus -> {
            if (updateStatus != null && updateStatus) {
                // Navigate to the next screen after successful OTP verification
                navController.popBackStack(R.id.profileFragment, false);
            }
        });

        binding.otpView.requestFocus();

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.otpView, InputMethodManager.SHOW_IMPLICIT);

    }
}