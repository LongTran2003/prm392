package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.ui.adapter.SubImageAdapter;
import prm392.orderfood.androidapp.utils.FileUtils;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.domain.models.shops.Shop;

@AndroidEntryPoint
public class ShopFormFragment extends Fragment {

    private RecyclerView rvSubImages;
    private SubImageAdapter subImageAdapter;
    private EditText etName, etAddress, etOpenHours, etEndHours;
    private Button btnSubmit, btnSelectImage, btnSelectSubImages, btnSelectBusinessImage, btnPickLocation;
    private ImageView ivPreviewImage, ivBusinessImage;
    private ProgressBar progressBar;
    TextView tvLocationInfo;

    private ShopViewModel shopViewModel;

    private File imageFile;
    private List<File> subImageFiles = new ArrayList<>();
    private File businessLicenseImageFile;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private String editingShopId = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    imageFile = FileUtils.getFileFromUri(requireContext(), uri);
                    ivPreviewImage.setImageURI(uri);
                    ivPreviewImage.setVisibility(View.VISIBLE);
                }
            });

    private final ActivityResultLauncher<Intent> businessImagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    businessLicenseImageFile = FileUtils.getFileFromUri(requireContext(), uri);
                    ivBusinessImage.setImageURI(uri);
                    ivBusinessImage.setVisibility(View.VISIBLE);
                }
            });

    private final ActivityResultLauncher<Intent> subImagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    List<Uri> uris = new ArrayList<>();
                    if (result.getData().getClipData() != null) {
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            uris.add(result.getData().getClipData().getItemAt(i).getUri());
                        }
                    } else {
                        uris.add(result.getData().getData());
                    }

                    for (Uri uri : uris) {
                        File file = FileUtils.getFileFromUri(requireContext(), uri);
                        subImageFiles.add(file);
                    }

                    Toast.makeText(requireContext(), "Đã chọn " + subImageFiles.size() + " ảnh phụ", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        checkEditMode();
        setupObservers();
        setupListeners();
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etOpenHours = view.findViewById(R.id.etOpenHours);
        etEndHours = view.findViewById(R.id.etEndHours);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectSubImages = view.findViewById(R.id.btnSelectSubImages);
        ivPreviewImage = view.findViewById(R.id.ivPreviewImage);
        progressBar = view.findViewById(R.id.progressBar);
        rvSubImages = view.findViewById(R.id.rvSubImages);
        btnSelectBusinessImage = view.findViewById(R.id.btnSelectBusinessImage);
        ivBusinessImage = view.findViewById(R.id.ivBusinessImage);
        btnPickLocation = view.findViewById(R.id.btnPickLocation);
        tvLocationInfo = view.findViewById(R.id.tvLocationInfo);
    }

    private void setupRecyclerView() {
        rvSubImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        subImageAdapter = new SubImageAdapter();
        rvSubImages.setAdapter(subImageAdapter);
    }

    private void setupViewModel() {
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
    }

    private void checkEditMode() {
        editingShopId = null;
        Bundle args = getArguments();
        if (args != null) {
            String id = args.getString("shopId");
            if (id != null && !id.trim().isEmpty()) {
                editingShopId = id;
                shopViewModel.getShopById(editingShopId);
            }
        }
    }

    private void setupObservers() {
        shopViewModel.shopDetail.observe(getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                etName.setText(shop.getName());
                etAddress.setText(shop.getAddress());
                etOpenHours.setText(shop.getOpenHours());
                etEndHours.setText(shop.getEndHours());
                latitude = shop.getLatitude();
                longitude = shop.getLongitude();

                tvLocationInfo = requireView().findViewById(R.id.tvLocationInfo);
                if (tvLocationInfo != null) {
                    tvLocationInfo.setText("Lat: " + latitude + ", Lng: " + longitude);
                }

                String normalizedCoverUrl = normalizeRemoteUrl(shop.getImageUrl());
                if (normalizedCoverUrl != null) {
                    Glide.with(this)
                            .load(normalizedCoverUrl)
                            .placeholder(R.drawable.bg_image_placeholder)
                            .error(R.drawable.bg_image_placeholder)
                            .into(ivPreviewImage);
                    ivPreviewImage.setVisibility(View.VISIBLE);
                } else {
                    ivPreviewImage.setVisibility(View.GONE);
                }

                String normalizedBusinessUrl = normalizeRemoteUrl(shop.getBusinessImageUrl());
                if (normalizedBusinessUrl != null) {
                    Glide.with(this)
                            .load(normalizedBusinessUrl)
                            .placeholder(R.drawable.bg_image_placeholder)
                            .error(R.drawable.bg_image_placeholder)
                            .into(ivBusinessImage);
                    ivBusinessImage.setVisibility(View.VISIBLE);
                } else {
                    ivBusinessImage.setVisibility(View.GONE);
                }

                List<String> normalizedUrls = new ArrayList<>();
                if (shop.getImages() != null) {
                    for (String url : shop.getImages()) {
                        String normalized = normalizeRemoteUrl(url);
                        if (normalized != null) {
                            normalizedUrls.add(normalized);
                        }
                    }
                }

                if (normalizedUrls.isEmpty()) {
                    rvSubImages.setVisibility(View.GONE);
                } else {
                    rvSubImages.setVisibility(View.VISIBLE);
                    subImageAdapter.setImageUrls(normalizedUrls);
                }
            }
        });

        shopViewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        shopViewModel.actionSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Thành công!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        shopViewModel.errorMessage.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSelectSubImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            subImagePickerLauncher.launch(intent);
        });

        btnSelectBusinessImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            businessImagePickerLauncher.launch(intent);
        });

        btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapPickerActivity.class);
            startActivityForResult(intent, 101); // MAP_REQUEST_CODE = 101
        });

        etOpenHours.setOnClickListener(v -> showTimePickerDialog(etOpenHours));
        etEndHours.setOnClickListener(v -> showTimePickerDialog(etEndHours));

        btnSubmit.setOnClickListener(v -> {
            if (!validateForm()) return;

            Shop shop = new Shop();
            shop.setName(etName.getText().toString().trim());
            shop.setAddress(etAddress.getText().toString().trim());
            shop.setOpenHours(etOpenHours.getText().toString().trim());
            shop.setEndHours(etEndHours.getText().toString().trim());
            shop.setLatitude(latitude);
            shop.setLongitude(longitude);

            if (editingShopId != null && !editingShopId.isEmpty()) {
                shop.setId(editingShopId);
                shopViewModel.updateShop(shop, imageFile, businessLicenseImageFile, subImageFiles);
            } else {
                shopViewModel.createShop(shop, imageFile, businessLicenseImageFile, subImageFiles);
            }
        });
    }

    private void showTimePickerDialog(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    target.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private boolean validateForm() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Vui lòng nhập tên cửa hàng");
            etName.requestFocus();
            return false;
        }
        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Vui lòng nhập địa chỉ");
            etAddress.requestFocus();
            return false;
        }
        if (etOpenHours.getText().toString().trim().isEmpty()) {
            etOpenHours.setError("Vui lòng chọn giờ mở cửa");
            etOpenHours.requestFocus();
            return false;
        }
        if (etEndHours.getText().toString().trim().isEmpty()) {
            etEndHours.setError("Vui lòng chọn giờ đóng cửa");
            etEndHours.requestFocus();
            return false;
        }
        if (imageFile == null && editingShopId == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn ảnh bìa cửa hàng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitude == 0.0 && longitude == 0.0) {
            Toast.makeText(requireContext(), "Vui lòng chọn vị trí trên bản đồ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);

            tvLocationInfo.setText("Lat: " + latitude + ", Lng: " + longitude);
            Toast.makeText(requireContext(), "Đã chọn vị trí: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        }
    }
}
