package prm392.orderfood.androidapp.ui.fragment.intro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class IntroFragment extends Fragment {
    private static final String TAG = "IntroFragment";
    private AuthViewModel mAuthViewModel;

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance() {
        IntroFragment fragment = new IntroFragment();
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
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        NavController navController = NavHostFragment.findNavController(this);
        mAuthViewModel.getNavigateTo().observe(getViewLifecycleOwner(), destination -> {
            if (destination != null) {
                switch (destination) {
                    case "login":
                        navController.navigate(R.id.action_introFragment_to_loginFragment);
                        break;
                    case "home":
                        navController.navigate(R.id.action_introFragment_to_homeFragment);
                        break;
                    case "shopList":
                        navController.navigate(R.id.action_introFragment_to_myShopListFragment);
                        break;
                    case "adminShopTab":
                        navController.navigate(R.id.action_introFragment_to_adminShopTabFragment);
                        break;
                    default:
                        navController.navigate(R.id.action_introFragment_to_loginFragment);
                        break;
                }
            }
        });

        mAuthViewModel.checkAuth();
    }
}