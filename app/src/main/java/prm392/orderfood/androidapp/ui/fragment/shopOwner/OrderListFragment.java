package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentOrderListBinding;
import prm392.orderfood.androidapp.ui.adapter.OrderAdapter;
import prm392.orderfood.androidapp.viewModel.OrderViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.orders.OrderRealTime;

public class OrderListFragment extends Fragment {
    private static final String TAG = "OrderListFragment";
    private FragmentOrderListBinding binding;
    private NavController navController;
    private OrderViewModel mOrderViewModel;
    private ShopViewModel mShopViewModel;
    private UserViewModel mUserViewModel;
    private OrderAdapter adapter;
    private final List<OrderRealTime> orderList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        setupAdapter();
        setUpObservers();
    }

    // Setup adapter
    private void setupAdapter() {
        List<prm392.orderfood.domain.models.menuItem.MenuItemResponse> menuItems = new ArrayList<>();
        if (mShopViewModel.getShopDetailResponse().getValue() != null && mShopViewModel.getShopDetailResponse().getValue().getMenuItems() != null) {
            menuItems = mShopViewModel.getShopDetailResponse().getValue().getMenuItems();
        }

        List<prm392.orderfood.domain.models.users.CustomerResponse> customers = new ArrayList<>();
        if (mUserViewModel.getCustomerResponseLiveData().getValue() != null) {
            customers = mUserViewModel.getCustomerResponseLiveData().getValue();
        }

        adapter = new OrderAdapter(orderList, menuItems, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onConfirmClicked(OrderRealTime order) {
                mOrderViewModel.updateOrderStatus(order.getFirebaseId(), "Confirmed");
            }

            @Override
            public void onCancelClicked(OrderRealTime order) {
                mOrderViewModel.updateOrderStatus(order.getFirebaseId(), "Cancelled");
            }

            @Override
            public void onDeliveredClicked(OrderRealTime order) {
                mOrderViewModel.updateOrderStatus(order.getFirebaseId(), "Delivered");
            }

            @Override
            public void onDoneClicked(OrderRealTime order) {
                mOrderViewModel.updateOrderStatus(order.getFirebaseId(), "Completed");
            }
        }, customers);
        binding.rvOrders.setAdapter(adapter);
    }

    private void setUpObservers() {
        mOrderViewModel.getOrderByShopIdLiveData().observe(getViewLifecycleOwner(), orders -> {
            adapter.updateOrders(orders);
        });

        mShopViewModel.getSelectedShop().observe(getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                mOrderViewModel.getOrdersByShopId(shop.getId());
            }
        });

        mOrderViewModel.getToastMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                if (msg.contains("successfully")) {
                    if (mShopViewModel.getSelectedShop().getValue() != null) {
                        mOrderViewModel.getOrdersByShopId(mShopViewModel.getSelectedShop().getValue().getId());
                    }
                }
                android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}