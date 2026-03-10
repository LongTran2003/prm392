package prm392.orderfood.androidapp.ui.fragment.customer;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentPendingOrdersBinding;
import prm392.orderfood.androidapp.ui.adapter.PendingOrderAdapter;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;
import prm392.orderfood.androidapp.viewModel.MenuItemViewModel;
import prm392.orderfood.androidapp.viewModel.OrderViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.models.orders.OrderRealTime;

public class PendingOrdersFragment extends Fragment {
    private static final String TAG = "PendingOrdersFragment";
    private FragmentPendingOrdersBinding binding;
    private OrderViewModel mOrderViewModel;
    private ShopViewModel mShopViewModel;
    private UserViewModel mUserViewModel;
    private MenuItemViewModel mMenuItemViewModel;
    private NavController navController;
    private PendingOrderAdapter adapter;

    private final List<OrderRealTime> pendingOrders = new ArrayList<>();
    private final List<MenuItemResponse> menuItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPendingOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mMenuItemViewModel = new ViewModelProvider(requireActivity()).get(MenuItemViewModel.class);
        navController = NavHostFragment.findNavController(this);

        mOrderViewModel.getPendingOrders(mUserViewModel.getUserProfileLiveData().getValue().getUserId());



        setupAdapter();
        setUpObservers();
    }

    private void setupAdapter() {
        //Chỗ này getall menuItems từ MenuItemViewModel
        adapter = new PendingOrderAdapter(pendingOrders, mMenuItemViewModel.getMenuItemsLiveData().getValue(), mShopViewModel.getPopularShopResponse().getValue());
        binding.rvOrders.setAdapter(adapter);
    }

    private void setUpObservers() {
        mOrderViewModel.getPendingOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            adapter.updateOrders(orders);
        });
    }
}