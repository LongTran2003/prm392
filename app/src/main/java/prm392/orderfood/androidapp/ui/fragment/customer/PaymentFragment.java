package prm392.orderfood.androidapp.ui.fragment.customer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.FragmentPaymentBinding;
import prm392.orderfood.androidapp.viewModel.OrderViewModel;
import prm392.orderfood.androidapp.viewModel.ShopViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private String paymentUrl;
    private OrderViewModel mOrderViewModel;
    private NavController navController;
    private String status;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        navController = Navigation.findNavController(requireView());
        // Lấy URL từ arguments hoặc ViewModel (ví dụ hardcode ở đây)
        mOrderViewModel.getCheckoutUrlLiveData().observe(getViewLifecycleOwner(), checkOutResponse -> {
            if (checkOutResponse != null && checkOutResponse.getCheckoutUrl() != null) {
                paymentUrl = checkOutResponse.getCheckoutUrl();
                setupWebView();
            }
        });

        mOrderViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                if (status != null && status.equals("PAID")) {
                    mOrderViewModel.clearOrderItems();
                    navController.popBackStack(R.id.homeFragment, false);
                } else if (status != null && status.equals("FAILED")) {
                    navController.popBackStack(R.id.cartFragment, false);
                }
            }
        });

        mOrderViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                if (status != null && status.equals("PAID")) {
                    mOrderViewModel.clearOrderItems();
                    navController.popBackStack(R.id.homeFragment, false);
                } else if (status != null && status.equals("FAILED")) {
                    navController.popBackStack(R.id.cartFragment, false);
                }
            }
        });
    }

    private void setupWebView() {
        WebView webView = binding.webView;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); // Required for PayOS React app to work properly
        webView.getSettings().setDatabaseEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            private boolean handleUrl(String url) {
                if (url == null) return false;
                if (url.contains("studentorderfood.app/return")) {
                    Uri uri = Uri.parse(url);
                    String orderCode = uri.getQueryParameter("orderCode");
                    status = "PAID";
                    mOrderViewModel.sendPaymentResult(orderCode, "PAID");
                    return true;
                } else if (url.contains("studentorderfood.app/cancel")) {
                    Uri uri = Uri.parse(url);
                    String orderCode = uri.getQueryParameter("orderCode");
                    status = "FAILED";
                    mOrderViewModel.sendPaymentResult(orderCode, "FAILED");
                    return true;
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (handleUrl(url)) {
                    view.stopLoading();
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (handleUrl(url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (handleUrl(request.getUrl().toString())) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.loadUrl(paymentUrl);
    }
}
