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
        String status = switch (position) {
            case 0 -> "Pending";
            case 1 -> "Approved";
            case 2 -> "Rejected";
            default -> "Pending";
        };
        return AdminShopListFragment.newInstance(status);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

