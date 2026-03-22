package prm392.orderfood.androidapp.ui.fragment.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminShopTabAdapter extends FragmentStateAdapter {

    public AdminShopTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> AdminShopListFragment.newInstance("Pending");
            case 1 -> AdminShopListFragment.newInstance("Approved");
            case 2 -> AdminShopListFragment.newInstance("Rejected");
            case 3 -> new AdminCategoryFragment();
            default -> AdminShopListFragment.newInstance("Pending");
        };
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}