package prm392.orderfood.androidapp.ui.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.domain.models.shops.Shop;

@AndroidEntryPoint
public class AdminShopListFragment extends Fragment {

    private ShopViewModel shopViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ShopApprovalAdapter adapter;

    private String status = "Pending";
    private int pageIndex = 1;
    private final int pageSize = 10;
    private boolean isLoadingMore = false;

    public static AdminShopListFragment newInstance(String status) {
        AdminShopListFragment fragment = new AdminShopListFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_shop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy status từ arguments
        if (getArguments() != null) {
            status = getArguments().getString("status", "Pending");
        }

        recyclerView = view.findViewById(R.id.recyclerViewAdminShop);
        progressBar = view.findViewById(R.id.progressBarAdminShop);

        adapter = new ShopApprovalAdapter(new ArrayList<>(), status, this::onApproveClicked, this::onRejectClicked);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        // Observer
        shopViewModel.shops.observe(getViewLifecycleOwner(), shops -> {
            if (pageIndex == 0) {
                adapter.setShopList(shops);
            } else {
                adapter.appendShopList(shops);
            }
            isLoadingMore = false;
        });

        shopViewModel.loading.observe(getViewLifecycleOwner(), isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        shopViewModel.actionSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Thao tác thành công!", Toast.LENGTH_SHORT).show();
                reloadShopList();
            }
        });

        // Scroll to load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoadingMore) {
                    isLoadingMore = true;
                    pageIndex++;
                    shopViewModel.loadShopsByStatus(status, pageIndex, pageSize);
                }
            }
        });

        // Initial load
        reloadShopList();
    }

    private void reloadShopList() {
        pageIndex = 1;
        isLoadingMore = false;
        shopViewModel.loadShopsByStatus(status, pageIndex, pageSize);
    }

    private void onApproveClicked(Shop shop) {
        shopViewModel.approveOrRejectShop(shop.getId(), true);
    }

    private void onRejectClicked(Shop shop) {
        shopViewModel.approveOrRejectShop(shop.getId(), false);
    }
}
