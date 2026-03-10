package prm392.orderfood.androidapp.ui.fragment.customer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentFoodOrderBinding;
import prm392.orderfood.androidapp.ui.adapter.CartAdapter;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.androidapp.viewModel.OrderViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.orderItem.OrderItem;
import prm392.orderfood.domain.models.orders.Order;

@AndroidEntryPoint
public class CartFragment extends Fragment {
    private final static String TAG = "CartFragment";
    private FragmentFoodOrderBinding binding;
    private OrderViewModel mOrderViewModel;
    private ShopViewModel mShopViewModel;
    private UserViewModel mUserViewModel;
    private NavController navController;

    private CartAdapter adapter; // giữ lại adapter


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        navController = Navigation.findNavController(requireView());

        setupRecyclerView();  // khởi tạo trước
        setUpObserver();
        setUpEvents();

    }

    @SuppressLint("SetTextI18n")
    private void setUpObserver() {
        mOrderViewModel.getOrderItemsLiveData().observe(getViewLifecycleOwner(), orderItems -> {
            if (orderItems != null && !orderItems.isEmpty()) {
                binding.foodItemsRecyclerView.setVisibility(View.VISIBLE);
                adapter.updateList(orderItems);

                // Cập nhật số lượng món khác nhau trong giỏ hàng
                binding.tvTotalItems.setText(orderItems.size() + " items");

                // Cập nhật tổng tiền
                int total = 0;
                for (OrderItem item : orderItems) {
                    total += item.getItem().getPrice() * item.getQuantity();
                }
                binding.totalPriceTextView.setText(CurrencyUtils.formatToVND(total));
                binding.checkoutButton.setEnabled(true);
                binding.checkoutButton.setBackgroundColor(getResources().getColor(R.color.primary_color, null));

            } else {
                binding.foodItemsRecyclerView.setVisibility(View.GONE);
                binding.totalPriceTextView.setText("0 VND");
                binding.tvTotalItems.setText("0 items");
                binding.checkoutButton.setEnabled(false);
                binding.checkoutButton.setBackgroundColor(Color.GRAY);
            }
        });

        // Tên Shop
        mShopViewModel.getSelectedShop().observe(getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                binding.tvShopAddress.setText(shop.getAddress());
                binding.tvShopName.setText(shop.getName());
                binding.tvShopEndTime.setText("Open until " + shop.getEndHours());
            }
        });

        //Toast thông báo từ OrderViewModel
        mOrderViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                binding.getRoot().post(() -> {
                    // Hiển thị Toast
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                });
            }
        });
        // Hiển thị thông báo lỗi từ OrderViewModel
        mOrderViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                binding.getRoot().post(() -> {
                    // Hiển thị Toast
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setUpEvents() {
        binding.paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCod) {
                binding.checkoutButton.setText("Order Now");
            } else if (checkedId == R.id.radioBank) {
                binding.checkoutButton.setText("Pay");
            }
        });

        binding.checkoutButton.setOnClickListener(v -> {
            if (binding.radioCod.isChecked()) {
                // Gửi đơn hàng thanh toán COD
                Order newOrder = new Order();
                newOrder.setCustomerId(Objects.requireNonNull(mUserViewModel.getUserProfileLiveData().getValue()).getUserId());
                newOrder.setShopId(Objects.requireNonNull(mShopViewModel.getSelectedShop().getValue()).getId());
                newOrder.setOrderItems(mOrderViewModel.getOrderItemsLiveData().getValue());
                newOrder.setPaymentMethod("COD");
                newOrder.setTotalAmount(CurrencyUtils.parseVNDToDouble(binding.totalPriceTextView.getText().toString()));
                mOrderViewModel.submitCodOrder(newOrder);
            } else if (binding.radioBank.isChecked()) {
                Order newOrder = new Order();
                newOrder.setCustomerId(Objects.requireNonNull(mUserViewModel.getUserProfileLiveData().getValue()).getUserId());
                newOrder.setShopId(Objects.requireNonNull(mShopViewModel.getSelectedShop().getValue()).getId());
                newOrder.setOrderItems(mOrderViewModel.getOrderItemsLiveData().getValue());
                newOrder.setPaymentMethod("Bank"); // Bank transfer
                newOrder.setTotalAmount(CurrencyUtils.parseVNDToDouble(binding.totalPriceTextView.getText().toString()));
                mOrderViewModel.generateQrCode(newOrder);
//                navController.navigate(R.id.action_cartFragment_to_qrDisplayFragment);
                navController.navigate(R.id.action_cartFragment_to_paymentFragment);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(mOrderViewModel.getOrderItemsLiveData().getValue(), new CartAdapter.OnCartActionListener() {
            @Override
            public void onIncreaseQuantity(int position) {
                OrderItem item = adapter.getCartItems().get(position);
                item.setQuantity(item.getQuantity() + 1);
                mOrderViewModel.setOrderItems(new ArrayList<>(adapter.getCartItems()));
            }

            @Override
            public void onDecreaseQuantity(int position) {
                OrderItem item = adapter.getCartItems().get(position);
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    mOrderViewModel.setOrderItems(new ArrayList<>(adapter.getCartItems()));
                }
            }

            @Override
            public void onRemoveItem(int position) {
                adapter.getCartItems().remove(position);
                mOrderViewModel.setOrderItems(new ArrayList<>(adapter.getCartItems()));
            }
        });
        binding.foodItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.foodItemsRecyclerView.setAdapter(adapter);
        binding.foodItemsRecyclerView.setHasFixedSize(true);
    }


}
