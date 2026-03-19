package prm392.orderfood.androidapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ActivityMainBinding;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AuthViewModel mAuthViewModel;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mAuthViewModel =  new ViewModelProvider(this).get(AuthViewModel.class); // Assuming you have a way to get the ViewModel instance
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class); // Assuming you have a way to get the ViewModel instance
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHost != null) {
            navController = navHost.getNavController();
        }
        bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> onNavigationItemSelected(item.getItemId()));
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            Log.d(TAG, "Destination changed: " + destination.getLabel());
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.loginFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.introFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.registerFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.phoneInputFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.otpFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.shopDetailFragment && "Student".equalsIgnoreCase(mAuthViewModel.getUserRole().getValue())) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.cartFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.paymentFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            }
            else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        mUserViewModel.getUserProfileLiveData()
                .observe(this, userProfile -> {
                    if (userProfile != null) {
                        Log.d(TAG, "User profile loaded: " + userProfile.getUserId());
                    } else {
                        Log.e(TAG, "User profile is null");
                    }
                });
    }

    // Func handle bottom navigation item selection
    private boolean onNavigationItemSelected(int itemId) {
        String role = mAuthViewModel.getUserRole().getValue();
        if (role == null) {
            Log.e(TAG, "onNavigationItemSelected: userRole is null");
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            return false;
        }

        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build();

        if (itemId == R.id.navigation_home) {
            if (role.equalsIgnoreCase("ShopOwner")) {
                navController.navigate(R.id.action_global_shopListFragment, null, navOptions);
            } else if (role.equalsIgnoreCase("Admin")) {
                navController.navigate(R.id.action_global_adminShopTabFragment, null, navOptions);
            } else if (role.equalsIgnoreCase("Student")) {
                navController.navigate(R.id.action_global_homeFragment, null, navOptions);
            } else {
                Log.e(TAG, "onNavigationItemSelected: Unknown role: " + role);
                Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (itemId == R.id.navigation_profile) {
            navController.navigate(R.id.action_global_profileFragment, null, navOptions);
            return true;
        }
        return false;
    }
}