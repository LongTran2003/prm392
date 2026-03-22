package prm392.orderfood.androidapp.ui.fragment.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import prm392.orderfood.androidapp.ui.adapter.AdminCategoryListAdapter;
import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.viewModel.CategoryViewModel;

@AndroidEntryPoint
public class AdminCategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;

    private EditText etCategoryName;
    private EditText etCategoryDescription;
    private EditText etCategoryImageUrl;
    private Button btnCreateCategory;
    private ProgressBar progressBar;
    private RecyclerView rvCategories;
    private AdminCategoryListAdapter categoryListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        etCategoryName = view.findViewById(R.id.etCategoryName);
        etCategoryDescription = view.findViewById(R.id.etCategoryDescription);
        etCategoryImageUrl = view.findViewById(R.id.etCategoryImageUrl);
        btnCreateCategory = view.findViewById(R.id.btnCreateCategory);
        progressBar = view.findViewById(R.id.progressBarCreateCategory);

        rvCategories = view.findViewById(R.id.rvCategoriesAdmin);
        categoryListAdapter = new AdminCategoryListAdapter(item -> {
            if (item == null || item.getId() == null || item.getId().isEmpty()) {
                Toast.makeText(requireContext(), "Invalid category", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete " + (item.getName() != null ? item.getName() : "this category") + "?")
                    .setPositiveButton("Delete", (dialog, which) -> categoryViewModel.deleteCategory(item.getId()))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCategories.setAdapter(categoryListAdapter);

        btnCreateCategory.setOnClickListener(v -> onCreateCategoryClicked());

        categoryViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            boolean loading = isLoading != null && isLoading;
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            btnCreateCategory.setEnabled(!loading);
        });

        categoryViewModel.getToastMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        categoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        categoryViewModel.getCreateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                etCategoryName.setText("");
                etCategoryDescription.setText("");
                etCategoryImageUrl.setText("");
            }
        });

        categoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(),
                categories -> {
            categoryListAdapter.submitList(categories);
        });

        categoryViewModel.getAllCategories();

        categoryViewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                // ViewModel đã tự getAllCategories() lại rồi, không cần làm gì thêm.
            }
        });
    }

    private void onCreateCategoryClicked() {
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();
        String imageUrl = etCategoryImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etCategoryName.setError("Category name is required");
            etCategoryName.requestFocus();
            return;
        }

        categoryViewModel.createCategory(name, description, imageUrl);
    }
}