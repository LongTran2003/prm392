package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentAddProductBinding;
import prm392.orderfood.androidapp.utils.FileUtils;
import prm392.orderfood.androidapp.viewModel.CategoryViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.domain.models.category.CategoryResponse;
import prm392.orderfood.domain.models.menuItem.MenuItem;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class AddProductFragment extends Fragment {
    private static final String TAG = "AddProductFragment";
    private FragmentAddProductBinding binding;
    private NavController navController;
    private CategoryViewModel mCategoryViewModel;
    private ShopViewModel mShopViewModel;
    private Uri selectedImageUri = null;
    private List<CategoryResponse> categoryList;

    MenuItemResponse selectedItem;
    boolean isEditMode;

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.ivPreview.setVisibility(View.VISIBLE);
                    Glide.with(requireContext())
                            .load(selectedImageUri)
                            .centerCrop()
                            .into(binding.ivPreview);              }
            });

    public static AddProductFragment newInstance() {
        AddProductFragment fragment = new AddProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        mCategoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        mShopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        navController = Navigation.findNavController(requireView());

        selectedItem = mShopViewModel.getSelectedMenuItem().getValue();
        isEditMode = selectedItem != null;

        binding.btnSubmit.setEnabled(false);
        binding.btnSubmit.setBackgroundColor(Color.GRAY);

        binding.btnCancel.setEnabled(true);
        binding.btnCancel.setBackgroundColor(getResources().getColor(R.color.primary_color, null));

        binding.btnPickImage.setEnabled(true);
        binding.btnPickImage.setBackgroundColor(getResources().getColor(R.color.primary_color, null));

        // Set up observers
        setUpObservers();
        // Set up events
        setUpEvents();
    }

    private void setUpObservers() {

        if (this.categoryList == null || this.categoryList.isEmpty()) {
            mCategoryViewModel.getAllCategories();
        }

        mCategoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                // Show error message to user
                Toast.makeText(requireContext(), "An Error Occur", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "setUpObservers: " + errorMessage);
            }
        });

        mCategoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null || categories.isEmpty()) {
                Toast.makeText(requireContext(),
                        "No categories available. Please ask admin to create categories first.",
                        Toast.LENGTH_SHORT).show();
                binding.btnSubmit.setEnabled(false);
                binding.btnSubmit.setBackgroundColor(Color.GRAY);
                return;
            }
            this.categoryList = categories;

            //Enale buttons after categories are loaded
            binding.btnSubmit.setEnabled(true);
            binding.btnSubmit.setBackgroundColor(getResources().getColor(R.color.primary_color, null));
            binding.btnCancel.setEnabled(true);
            binding.btnCancel.setBackgroundColor(getResources().getColor(R.color.primary_color));
            binding.btnPickImage.setEnabled(true);
            binding.btnPickImage.setBackgroundColor(getResources().getColor(R.color.primary_color));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    new java.util.ArrayList<>(
                            categories.stream().map(CategoryResponse::getName).collect(java.util.stream.Collectors.toList())
                    )
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spCategory.setAdapter(adapter);


            // Move edit-mode logic here (after categories are loaded)
            if (isEditMode && selectedItem != null) {
                binding.etName.setText(selectedItem.getName());
                binding.etDescription.setText(selectedItem.getDescription());
                binding.etPrice.setText(String.valueOf(selectedItem.getPrice()));

                Glide.with(requireContext())
                        .load(selectedItem.getImageUrl())
                        .centerCrop()
                        .into(binding.ivPreview);

                binding.ivPreview.setVisibility(View.VISIBLE);

                int index = 0;
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId().equals(selectedItem.getCategoryId())) {
                        index = i;
                        break;
                    }
                }
                binding.spCategory.setSelection(index);
            }
        });

        mShopViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                navController.popBackStack();
            }
        });

    }

    private void setUpEvents() {
        binding.btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pickImageLauncher.launch(Intent.createChooser(intent, "Select image"));
        });

        setupSubmitBtn();

        binding.btnCancel.setOnClickListener(v -> {
            navController.popBackStack();
        });
    }

    private void setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String desc = binding.etDescription.getText().toString().trim();
            String priceStr = binding.etPrice.getText().toString().trim();
            boolean isAvailable = true;

            // Kiểm tra các trường bắt buộc
            boolean isMissingImage = !isEditMode && selectedImageUri == null;
            if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || isMissingImage) {
                Toast.makeText(requireContext(), "Please fill all fields" + (isMissingImage ? " and select an image" : ""), Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse giá tiền
            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra category
            int selectedIndex = binding.spCategory.getSelectedItemPosition();
            if (selectedIndex < 0 || categoryList == null) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            CategoryResponse selectedCategory = categoryList.get(selectedIndex);
            String categoryId = selectedCategory.getId();
            if (categoryId == null || categoryId.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid category selected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra shop
            String shopId = mShopViewModel.getSelectedShop().getValue() != null ? mShopViewModel.getSelectedShop().getValue().getId() : null;
            if (shopId == null || shopId.isEmpty()) {
                Toast.makeText(requireContext(), "No shop selected", Toast.LENGTH_SHORT).show();
                return;
            }


            MenuItem newMenuItem = new MenuItem();
            newMenuItem.setName(name);
            newMenuItem.setDescription(desc);
            newMenuItem.setPrice(price);
            newMenuItem.setAvailable(isAvailable);
            newMenuItem.setCategoryId(categoryId);
            newMenuItem.setShopId(shopId);

            // Chuyển ảnh URI thành file
            File imageFile = null;
            String imageUrl;

            boolean pickedNewImage = selectedImageUri != null && "content".equals(selectedImageUri.getScheme());

            if (pickedNewImage) {
                // Chỉ chuyển thành file nếu là ảnh mới từ thư viện
                imageFile = FileUtils.getFileFromUri(requireContext(), selectedImageUri);
                imageUrl = "null"; // Server sẽ dùng imageFile để upload
            } else {
                imageUrl = selectedItem != null ? selectedItem.getImageUrl() : "null";
            }
            newMenuItem.setImageUrl(imageUrl);
            // Nếu là cập nhật thì giữ lại ID cũ
            if (isEditMode && selectedItem != null) {
                mShopViewModel.updateItemToShop(selectedItem.getId(), newMenuItem, imageFile);
            } else {
                mShopViewModel.addItemToShop(newMenuItem, imageFile);
            }
        });
    }

}