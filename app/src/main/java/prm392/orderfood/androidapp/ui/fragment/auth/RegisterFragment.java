package prm392.orderfood.androidapp.ui.fragment.auth;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentRegisterBinding;
import prm392.orderfood.androidapp.ui.states.SignUpState;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;
import prm392.orderfood.domain.models.users.UserRegister;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private FragmentRegisterBinding binding;
    private AuthViewModel mAuthViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        setupObservers();
        setupListeners();
    }

    private void setupListeners() {
        setupTVBackSignIn();
        setupBtnRegister();
    }

    private void setupObservers() {
        // Observe LiveData from ViewModel
        observeSignUpState();
    }

    private void setupTVBackSignIn() {
        binding.tvBackSignIn.setOnClickListener(v -> {
            // Handle back to sign-in action
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_registerFragment_to_loginFragment, null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build());
        });
        // Underline the TextView and set color
        binding.tvBackSignIn.setPaintFlags(binding.tvBackSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // Make the TextView clickable and focusable
        binding.tvBackSignIn.setClickable(true);
        binding.tvBackSignIn.setFocusable(true);
    }

    private void setupBtnRegister() {
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setBackgroundColor(getResources().getColor(R.color.grey_400, null));

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fullName = binding.etFullName.getText().toString().trim();
                String username = binding.etUsername.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
                String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
                boolean enable = !username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !fullName.isEmpty() &&
                        !confirmPassword.isEmpty() && !phoneNumber.isEmpty();
                binding.btnRegister.setEnabled(enable);
                if (enable) {
                    binding.btnRegister.setBackgroundColor(getResources().getColor(R.color.primary_color, null));
                } else {
                    binding.btnRegister.setBackgroundColor(getResources().getColor(R.color.grey_400, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        binding.etFullName.addTextChangedListener(watcher);
        binding.etUsername.addTextChangedListener(watcher);
        binding.etEmail.addTextChangedListener(watcher);
        binding.etPassword.addTextChangedListener(watcher);
        binding.etConfirmPassword.addTextChangedListener(watcher);
        binding.etPhoneNumber.addTextChangedListener(watcher);

        binding.btnRegister.setOnClickListener(v -> {
            String fullName = binding.etFullName.getText().toString();
            String username = binding.etUsername.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String confirmPassword = binding.etConfirmPassword.getText().toString();
            String phoneNumber = binding.etPhoneNumber.getText().toString();
            // Validate input fields
            if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
                binding.etConfirmPassword.setError("Confirm Password do not match");
                return;
            }

            String passwordRule = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{6,}$";
            if (!password.matches(passwordRule)) {
                binding.etPassword.setError("Password cần chữ hoa, chữ thường, số và ký tự đặc biệt");
                return;
            }

            UserRegister userRegister = new UserRegister();
            userRegister.setFullName(fullName);
            userRegister.setUserName(username);
            userRegister.setEmail(email);
            userRegister.setPassword(password);
            userRegister.setConfirmPassword(confirmPassword);
            userRegister.setPhone(phoneNumber);
            mAuthViewModel.registerShopOwner(userRegister);
        });
    }

    private void observeSignUpState() {
        mAuthViewModel.getSignUpState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof SignUpState.Loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnRegister.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
            }

            if (state instanceof SignUpState.Success) {
                String successMessage = ((SignUpState.Success) state).getMessage();
                Log.d(TAG, "observeSignUpState: " + successMessage);
                Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show();
                // Navigate to login screen after successful registration
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_registerFragment_to_loginFragment, null,
                        new NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build());
            } else

            if (state instanceof SignUpState.Error) {
                String errorMessage = ((SignUpState.Error) state).getErrorMessage();
                Log.d(TAG, "observeSignUpState: " + errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}