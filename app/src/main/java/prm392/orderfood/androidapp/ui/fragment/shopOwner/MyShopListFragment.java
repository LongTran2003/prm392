package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentOwnerShopListBinding;
import prm392.orderfood.androidapp.ui.adapter.ShopOwnerAdapter;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.domain.models.shops.Shop;

@AndroidEntryPoint
public class MyShopListFragment extends Fragment {
    private static final String TAG = "MyShopListFragment";
    private FragmentOwnerShopListBinding binding;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ShopViewModel shopViewModel;
    private ShopOwnerAdapter adapter;

    private int pageIndex = 1;
    private final int pageSize = 10;
    private boolean isLoadingMore = false;
    private Shop pendingDeleteShop = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOwnerShopListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewMyShop);
        progressBar = view.findViewById(R.id.progressBarMyShop);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);


        adapter = new ShopOwnerAdapter(new ArrayList<>(), new ShopOwnerAdapter.OnShopActionListener() {
            @Override
            public void onEdit(Shop shop) {
                navigateToEditShop(shop);
            }

            @Override
            public void onDelete(Shop shop) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa cửa hàng này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            pendingDeleteShop = shop;
                            shopViewModel.deleteShop(shop.getId());
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onShopSelected(Shop shop) {
                navToShopDetail(shop);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Observe shop list
        shopViewModel.shops.observe(getViewLifecycleOwner(), shops -> {
            if (pageIndex == 1) {
                adapter.setShopList(shops);
            } else {
                adapter.appendShopList(shops);
            }
            isLoadingMore = false;
        });

        // Observe loading
        shopViewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe error
        shopViewModel.errorMessage.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe delete success
        shopViewModel.actionSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success) && pendingDeleteShop != null) {
                adapter.removeShop(pendingDeleteShop);
                pendingDeleteShop = null;
                Toast.makeText(requireContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        // Infinite scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoadingMore) {
                    isLoadingMore = true;
                    pageIndex++;
                    shopViewModel.loadShopsByOwner(pageIndex, pageSize);
                }
            }
        });

        // Navigate add
        binding.btnAddShop.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_myShopListFragment_to_shopFormFragment));

        reloadShopList();
    }

    private void reloadShopList() {
        pageIndex = 1;
        isLoadingMore = false;
        shopViewModel.loadShopsByOwner(pageIndex, pageSize);
    }

    private void navigateToEditShop(Shop shop) {
        Bundle args = new Bundle();
        args.putString("shopId", shop.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_myShopListFragment_to_shopFormFragment, args);
    }

    private void navToShopDetail(Shop shop) {
        shopViewModel.setSelectedShop(shop);
        Log.d(TAG, "navToShopDetail: " + shop.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_myShopListFragment_to_shopDetailFragment);
    }
}
