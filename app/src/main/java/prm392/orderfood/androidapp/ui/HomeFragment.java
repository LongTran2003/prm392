package prm392.orderfood.androidapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ActivityMainBinding;
import prm392.orderfood.androidapp.databinding.FragmentHomeBinding;
import prm392.orderfood.androidapp.ui.adapter.HomeCategoryAdapter;
import prm392.orderfood.androidapp.ui.adapter.PopularShopAdapter;
import prm392.orderfood.androidapp.utils.DateTimeUtils;
import prm392.orderfood.androidapp.viewModel.CategoryViewModel;
import prm392.orderfood.androidapp.viewModel.MenuItemViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.category.CategoryResponse;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import prm392.orderfood.domain.models.shops.Shop;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private UserViewModel mUserViewModel;
    private ShopViewModel mShopViewModel;
    private MenuItemViewModel mMenuItemViewModel;
    private CategoryViewModel mCategoryViewModel;
    private NavController navController;

    private HomeCategoryAdapter homeCategoryAdapter;
    private PopularShopAdapter popularShopAdapter;

    private List<PopularShopResponse> fullShopList;
    private List<CategoryResponse> fullCategoryList;

    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    private ActivityResultLauncher<String> locationPermissionLauncher;
    private LocationCallback locationCallback;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Quyền được cấp -> gọi hàm lấy vị trí
                        getCurrentLocation();
                    } else {
                        // Quyền bị từ chối
                        Toast.makeText(requireContext(), "Ứng dụng cần quyền vị trí để tìm cửa hàng gần bạn", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getCurrentLocation();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mCategoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        mMenuItemViewModel = new ViewModelProvider(requireActivity()).get(MenuItemViewModel.class);
        navController = Navigation.findNavController(requireView());

        fullShopList = new ArrayList<>();
        fullCategoryList = new ArrayList<>();

        setupObservers();
        setupEvents();
        setupRecyclerView();

        if (mUserViewModel.getUserProfileLiveData().getValue() == null) {
            // Chỉ gọi khi chưa có dữ liệu
            mUserViewModel.fetchUserProfile();
        }

        mCategoryViewModel.getAllCategories();
        mShopViewModel.fetchPopularShops(DateTimeUtils.getCurrentTime());
//        Log.d(TAG, "onViewCreated: Current Time: " + DateTimeUtils.getCurrentTime());

        mMenuItemViewModel.getAllMenuItems();

        binding.tvViewAllShops.setOnClickListener(v -> {
            popularShopAdapter.updateData(fullShopList);
        });
    }

    private void setupObservers() {
        mUserViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                // Sử dụng Glide để tải ảnh đại diện
                Glide.with(requireContext())
                        .load(userProfile.getAvatar())
                        .placeholder(R.drawable.avatar)
                        .into(binding.civProfileImg);
            } else {
                // Xử lý trường hợp không có dữ liệu người dùng
                binding.civProfileImg.setImageResource(R.drawable.avatar); // Hoặc một hình ảnh mặc định khác
            }
        });

        mCategoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categoryList -> {
            if (categoryList != null && !categoryList.isEmpty()) {
                fullCategoryList = categoryList;
                fullCategoryList.add(0, new CategoryResponse("ALL", "All"));
                homeCategoryAdapter.updateData(categoryList);
            }
        });

        mShopViewModel.getPopularShopResponse().observe(getViewLifecycleOwner(), popularShopResponse -> {
            if (popularShopResponse != null) {
                fullShopList = popularShopResponse;

                CategoryResponse selected = homeCategoryAdapter.getSelectedCategory();
                if (selected != null) {
                    applyCategoryFilter(selected.getId());
                } else {
                    popularShopAdapter.updateData(fullShopList);
                }
            }
        });
    }

    private void setupEvents() {
        binding.civProfileImg.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_global_profileFragment);
        });
    }

    private void setupRecyclerView() {
        homeCategoryAdapter = new HomeCategoryAdapter(
                fullCategoryList,
                position -> {
                    CategoryResponse selected = mCategoryViewModel.getCategoriesLiveData().getValue().get(position);
                    if (selected == null || selected.getId() == null) return;

                    applyCategoryFilter(selected.getId());
                }
        );
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setHasFixedSize(true);
        binding.rvCategories.setItemAnimator(new DefaultItemAnimator());
        binding.rvCategories.setAdapter(homeCategoryAdapter);

        popularShopAdapter = new PopularShopAdapter(
                fullShopList,
                shop -> {
                    Shop selectedShop = new Shop();
                    selectedShop.setId(shop.getId());
                    selectedShop.setName(shop.getName());
                    selectedShop.setAddress(shop.getAddress());
                    selectedShop.setImageUrl(shop.getImageUrl());
                    selectedShop.setOpenHours(shop.getOpenHours());
                    selectedShop.setEndHours(shop.getEndHours());
                    selectedShop.setRating(shop.getRating());
                    mShopViewModel.setSelectedShop(selectedShop);
                    navController.navigate(R.id.action_homeFragment_to_shopDetailFragment);
                }
        );
        binding.rvPopularShops.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        binding.rvPopularShops.setHasFixedSize(true);
        binding.rvPopularShops.setItemAnimator(new DefaultItemAnimator());
        binding.rvPopularShops.setAdapter(popularShopAdapter);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: yêu cầu permission nếu chưa có
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, // priority
                5000 // interval in milliseconds
        ).setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    Toast.makeText(requireContext(), "Không lấy được vị trí hiện tại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentLat = locationResult.getLastLocation().getLatitude();
                currentLng = locationResult.getLastLocation().getLongitude();
                Log.d(TAG, "Current location (real-time): " + currentLat + ", " + currentLng);

                // Gọi API sau khi lấy được vị trí
                mShopViewModel.fetchPopularShops(DateTimeUtils.getCurrentTime());

                // Hủy request sau khi lấy xong
                fusedLocationClient.removeLocationUpdates(this);
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void applyCategoryFilter(String selectedCategoryId) {
        List<PopularShopResponse> filteredShops = new ArrayList<>();
        boolean skipDistance = (currentLat == 0.0 && currentLng == 0.0);
        for (PopularShopResponse shop : fullShopList) {
            double shopLat = shop.getLatitude();
            double shopLng = shop.getLongitude();

            boolean matchCategory = "ALL".equalsIgnoreCase(selectedCategoryId) ||
                    (shop.getCategoryIds() != null && shop.getCategoryIds().contains(selectedCategoryId));

            double distance = calculateDistance(currentLat, currentLng, shopLat, shopLng);
            boolean inRange = skipDistance || distance <= 5.0;

            if (matchCategory && inRange) {
                filteredShops.add(shop);
            }
        }
        popularShopAdapter.updateData(filteredShops);
    }


    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Bán kính Trái đất (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Khoảng cách (km)
    }
}