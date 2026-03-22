package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentDetailShopBinding;
import prm392.orderfood.androidapp.ui.adapter.CategoryAdapter;
import prm392.orderfood.androidapp.ui.adapter.MenuItemAdapter;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.androidapp.viewModel.AuthViewModel;
import prm392.orderfood.androidapp.viewModel.OrderViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.category.CategoriesInShopMenu;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

@AndroidEntryPoint
public class ShopDetailFragment extends Fragment {

    public static final String TAG = "DetailShopFragment";
    private FragmentDetailShopBinding binding;
    private ShopViewModel mShopViewModel;
    private AuthViewModel mAuthViewModel;
    private OrderViewModel mOrderViewModel;
    private UserViewModel mUserViewModel;
    private NavController navController;

    private RecyclerView recyclerCategory;
    private RecyclerView recyclerMenuItem;

    private MenuItemAdapter menuItemAdapter;
    private CategoryAdapter categoryAdapter;
    private static final String REMOTE_IMAGE_HOST =
            "https://food-order-system-gndtevhzdef5hwgh.southeastasia-01.azurewebsites.net";
    private String normalizeRemoteUrl(String rawUrl) {
        if (rawUrl == null) return null;
        String url = rawUrl.trim();
        if (url.isEmpty() || "null".equalsIgnoreCase(url)) return null;

        if (url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith("content://")
                || url.startsWith("file://")) {
            return url;
        }

        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        return REMOTE_IMAGE_HOST + url;
    }

    private static String normalizeRole(String role) {
        if (role == null) return "";
        return role.replace("_", "")
                .replace("-", "")
                .replace(" ", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private boolean isShopOwnerRole(String role) {
        return "shopowner".equals(normalizeRole(role));
    }

    private boolean isStudentRole(String role) {
        return "student".equals(normalizeRole(role));
    }

    private void applyRoleUi(String role) {
        boolean isOwner = isShopOwnerRole(role);
        binding.btnAddProd.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        if (menuItemAdapter != null) {
            menuItemAdapter.setUserRole(role);
        }
    }

    private int quantity = 1;

    public ShopDetailFragment() {
        // Required empty public constructor
    }

    public static ShopDetailFragment newInstance(String param1, String param2) {
        ShopDetailFragment fragment = new ShopDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        navController = Navigation.findNavController(requireView());
        recyclerCategory = binding.recyclerCategory;
        recyclerMenuItem = binding.recyclerMenuItems;
        setUpObservers();
        setUpEvents();

        // Mặc định ẩn thông tin sản phẩm đã chọn
        binding.layoutSelectedProductInfo.setVisibility(View.GONE);
        binding.btnPlus.setVisibility(View.GONE);
        binding.btnMinus.setVisibility(View.GONE);
        binding.layoutBottomAddToCart.setVisibility(View.GONE);

        applyRoleUi(mAuthViewModel.getUserRole().getValue());
        mAuthViewModel.getUserRole().observe(getViewLifecycleOwner(), this::applyRoleUi);

        mUserViewModel.getAllCustomers();
    }

    private void setUpEvents() {
        // Set up click listeners for back button
        binding.ivBack.setOnClickListener(v -> {
            navController.popBackStack();
        });

        // Set up click listener for add product button
        binding.btnAddProd.setOnClickListener(v -> {
            // Navigate to add product screen
            navController.navigate(R.id.action_shopDetailFragment_to_addProductFragment);
        });

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice(CurrencyUtils.parseVNDToRaw(binding.tvSelectedProductPrice.getText().toString()));
        });

        binding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice(CurrencyUtils.parseVNDToRaw(binding.tvSelectedProductPrice.getText().toString()));
            }
        });

        // Set up click nav to cart
        binding.layoutSelectedProductInfo.setOnClickListener(v -> {
            navController.navigate(R.id.action_shopDetailFragment_to_cartFragment);
        });

        binding.tvCloseSelectedProductLayout.setOnClickListener(v -> {
            // Ẩn thông tin sản phẩm đã chọn
            binding.layoutBottomAddToCart.setVisibility(View.GONE);
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            // Thêm sản phẩm vào giỏ hàng
            MenuItemResponse selectedItem = mShopViewModel.getSelectedMenuItem().getValue();
            if (selectedItem != null) {
                mOrderViewModel.addItemToOrder(selectedItem, quantity);
//                Toast.makeText(getContext(), "Added " + quantity + " " + selectedItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
                // Ẩn thông tin sản phẩm đã chọn
                binding.layoutBottomAddToCart.setVisibility(View.GONE);
            } else {
                Toast.makeText(getContext(), "No product selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cvOrders.setOnClickListener(v -> {
            String currentRole = mAuthViewModel.getUserRole().getValue();
            if (isShopOwnerRole(currentRole)) {
                navController.navigate(R.id.action_shopDetailFragment_to_orderListFragment);
            } else if (isStudentRole(currentRole)) {
                navController.navigate(R.id.action_shopDetailFragment_to_cartFragment);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUpObservers() {
        mShopViewModel.getSelectedShop().observe(getViewLifecycleOwner(), shop -> {
            if (shop == null) {
                Log.e(TAG, "Selected shop is null");
                Toast.makeText(getContext(), "No shop selected, cannot fetch shop detail", Toast.LENGTH_SHORT).show();
                return;
            }

            mShopViewModel.getShopDetail(shop.getId());
        });

        mShopViewModel.getShopDetailResponse().observe(getViewLifecycleOwner(), shopDetailResponse -> {
            if (shopDetailResponse == null) {
                Log.e(TAG, "Shop detail response is null");
                Toast.makeText(getContext(), "Failed to fetch shop details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Loading BG Shop
            String normalizedShopImageUrl = normalizeRemoteUrl(shopDetailResponse.getImageUrl());
            if (normalizedShopImageUrl == null) {
                if (mShopViewModel.getSelectedShop().getValue() != null) {
                    normalizedShopImageUrl = normalizeRemoteUrl(
                            mShopViewModel.getSelectedShop().getValue().getImageUrl()
                    );
                }
            }

            Glide.with(binding.getRoot())
                    .load(normalizedShopImageUrl)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .into(binding.ivShopBG);

            Glide.with(binding.getRoot())
                    .load(normalizedShopImageUrl)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .into(binding.ivShopLogo);

            // Set shop name
            binding.tvShopName.setText(shopDetailResponse.getName());
            // set shop rating
//            binding.tvShopRating.setText(String.valueOf(shopDetailResponse.getRating()));

            // Set Open/End Hours
            try {
                // Giả sử dữ liệu ban đầu có định dạng "HH:mm:ss"
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                Date openTime = inputFormat.parse(shopDetailResponse.getOpenHours());
                Date endTime = inputFormat.parse(shopDetailResponse.getEndHours());

                String formattedOpenTime = outputFormat.format(openTime);
                String formattedEndTime = outputFormat.format(endTime);

                binding.tvOpenTime.setText(formattedOpenTime + " - " + formattedEndTime);
            } catch (Exception e) {
                Log.e(TAG, "setUpObservers: " + e.getMessage());
                binding.tvOpenTime.setText(shopDetailResponse.getOpenHours() + " - " + shopDetailResponse.getEndHours());
            }
            // Xử lý thời gian mở cửa và đóng cửa
            Calendar calendar = Calendar.getInstance();
            int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
            int nowMinute = calendar.get(Calendar.MINUTE);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date openTime = sdf.parse(shopDetailResponse.getOpenHours());
                Date endTime = sdf.parse(shopDetailResponse.getEndHours());

                Calendar openCal = Calendar.getInstance();
                openCal.setTime(openTime);
                int openHour = openCal.get(Calendar.HOUR_OF_DAY);
                int openMinute = openCal.get(Calendar.MINUTE);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(endTime);
                int endHour = endCal.get(Calendar.HOUR_OF_DAY);
                int endMinute = endCal.get(Calendar.MINUTE);

                // Chuyển về phút để so sánh
                int nowTotalMin = nowHour * 60 + nowMinute;
                int openTotalMin = openHour * 60 + openMinute;
                int endTotalMin = endHour * 60 + endMinute;

                if (nowTotalMin >= openTotalMin && nowTotalMin < endTotalMin) {
                    binding.tvShopStatus.setText("Opening");
                    binding.tvShopStatus.setTextColor(Color.GREEN);
                } else {
                    binding.tvShopStatus.setText("Closing");
                    binding.tvShopStatus.setTextColor(Color.RED);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing time", e);
                binding.tvShopStatus.setText("Unknown");
                binding.tvShopStatus.setTextColor(Color.GRAY);
            }


            // Set Category List
            List<CategoriesInShopMenu> categories = Optional.ofNullable(shopDetailResponse.getCategories())
                    .orElse(new ArrayList<>());
            // Thêm Category đặc biệt "ALL" vào đầu danh sách
            CategoriesInShopMenu allCategory = new CategoriesInShopMenu("ALL", "All");
            categories.add(0, allCategory);

            categoryAdapter = new CategoryAdapter(categories, categoryId -> {
                List<MenuItemResponse> allItems = Optional.ofNullable(mShopViewModel.getShopDetailResponse().getValue().getMenuItems())
                        .orElse(new ArrayList<>());
                if ("ALL".equals(categoryId)) {
                    // Nếu chọn "All", hiển thị toàn bộ món ăn
                    menuItemAdapter.updateData(allItems);
                    binding.tvCategoryTitle.setText("All (" + allItems.size() + ")"); // All (10)
                } else {
                    // Lọc theo category
                    List<MenuItemResponse> filteredItems = new ArrayList<>();
                    for (MenuItemResponse item : allItems) {
                        if (categoryId.equals(item.getCategoryId())) {
                            filteredItems.add(item);
                        }
                    }
                    menuItemAdapter.updateData(filteredItems);
                    String categoryName = categories.stream()
                            .filter(category -> category.getId().equals(categoryId))
                            .findFirst()
                            .map(CategoriesInShopMenu::getName)
                            .orElse("Unknown Category");
                    binding.tvCategoryTitle.setText(categoryName + " (" + filteredItems.size() + ")");
                }

            });
            recyclerCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerCategory.setAdapter(categoryAdapter);
            // Set Menu Items
            List<MenuItemResponse> menuItems = Optional.ofNullable(shopDetailResponse.getMenuItems())
                    .orElse(new ArrayList<>());
//            Log.d(TAG, "setUpObservers: Menu Items: " + menuItems.size());
            String currentRole = mAuthViewModel.getUserRole().getValue();
            menuItemAdapter = new MenuItemAdapter(menuItems, currentRole != null ?
                    mAuthViewModel.getUserRole().getValue() : "Guest", new MenuItemAdapter.OnMenuItemActionListener() {
                @Override
                public void onUpdate(MenuItemResponse item) {
                    mShopViewModel.setSelectedMenuItem(item);
                    navController.navigate(R.id.action_shopDetailFragment_to_addProductFragment);
                }

                @Override
                public void onDelete(MenuItemResponse item) {
                    mShopViewModel.deleteMenuItem(item.getId());
                }

                @Override
                public void onClick(MenuItemResponse item) {
                    if (isShopOwnerRole(mAuthViewModel.getUserRole().getValue())) {
                        return;
                    }
//                    handleSelectItem(item);
                    mShopViewModel.setSelectedMenuItem(item);
                }
            });
            recyclerMenuItem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerMenuItem.setAdapter(menuItemAdapter);
            binding.tvCategoryTitle.setText("All (" + menuItems.size() + ")"); // All (10)

            mShopViewModel.getSelectedMenuItem().observe(getViewLifecycleOwner(), selectedItem -> {
                if (selectedItem != null) {
                    handleSelectItem(selectedItem);
                } else {
                    // Nếu không có sản phẩm nào được chọn, ẩn thông tin sản phẩm đã chọn
                    binding.layoutSelectedProductInfo.setVisibility(View.GONE);
                    binding.btnPlus.setVisibility(View.GONE);
                    binding.btnMinus.setVisibility(View.GONE);
                    binding.layoutBottomAddToCart.setVisibility(View.GONE);
                }
            });

        });

        mShopViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        mOrderViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void handleSelectItem(MenuItemResponse item) {
        // Hiển thị thông tin sản phẩm đã chọn
        binding.layoutSelectedProductInfo.setVisibility(View.VISIBLE);
        binding.btnPlus.setVisibility(View.VISIBLE);
        binding.btnMinus.setVisibility(View.VISIBLE);
        binding.layoutBottomAddToCart.setVisibility(View.VISIBLE);

        binding.tvSelectedProductName.setText(item.getName());
        binding.tvSelectedProductPrice.setText(CurrencyUtils.formatToVND(item.getPrice()));
        binding.tvSelectedProductDesc.setText(item.getDescription());

        quantity = 1;
        binding.tvQuantity.setText(String.valueOf(quantity));
        updateTotalPrice(CurrencyUtils.parseVNDToRaw(binding.tvSelectedProductPrice.getText().toString()));
    }

    private void updateTotalPrice(String price) {
        try {
            int total = Integer.parseInt(price) * quantity;
            binding.tvTotalPrice.setText(CurrencyUtils.formatToVND(total));
        } catch (NumberFormatException e) {
            binding.tvTotalPrice.setText("0 VND");
        }

    }

}