package prm392.orderfood.androidapp.ui.fragment.customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.ui.adapter.TransactionAdapter;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.androidapp.viewModel.TransactionViewModel;
import prm392.orderfood.androidapp.viewModel.UserViewModel;
import prm392.orderfood.domain.models.transactions.Transaction;

@AndroidEntryPoint
public class WalletFragment extends Fragment {
    private TextView tvBalance, tvNoTransactions;
    private Button btnWithdraw;
    private RecyclerView rvTransactions;

    private UserViewModel mUserViewModel;
    private TransactionViewModel mTransactionViewModel;

    private TransactionAdapter transactionAdapter;

    public WalletFragment() {
        super(R.layout.fragment_wallet); // Layout XML
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        tvBalance = view.findViewById(R.id.tvWalletBalance);
        tvNoTransactions = view.findViewById(R.id.tvNoTransactions);
        btnWithdraw = view.findViewById(R.id.btnRequestWithdraw);
        rvTransactions = view.findViewById(R.id.rvWalletTransactions);

        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mTransactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // RecyclerView setup
        transactionAdapter = new TransactionAdapter();
        rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTransactions.setAdapter(transactionAdapter);

        // Observe user data safely
        mUserViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Show balance
                tvBalance.setText("Balance: " + CurrencyUtils.formatToVND(user.getWalletBalance()));

                // Fetch transactions
                mTransactionViewModel.fetchTransactions(user.getUserId());

                // Withdraw button
                btnWithdraw.setOnClickListener(v -> showWithdrawDialog(user.getUserId()));
            }
        });

        // Observe transaction data
        mTransactionViewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions == null || transactions.isEmpty()) {
                tvNoTransactions.setVisibility(View.VISIBLE);
                rvTransactions.setVisibility(View.GONE);
            } else {
                tvNoTransactions.setVisibility(View.GONE);
                rvTransactions.setVisibility(View.VISIBLE);
                transactionAdapter.setTransactionList(transactions);
            }
        });

        mTransactionViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        mTransactionViewModel.getToastMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWithdrawDialog(String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter amount to withdraw");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Withdraw", (dialog, which) -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(text);
            if (amount <= 0) {
                Toast.makeText(requireContext(), "Amount must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            mTransactionViewModel.requestWithdraw(userId, amount, "Customer withdrawal request");
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
