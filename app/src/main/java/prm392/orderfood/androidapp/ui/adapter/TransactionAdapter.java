package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.domain.models.transactions.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList == null ? 0 : transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAmount, tvStatus, tvType, tvDate, tvDescription;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvStatus = itemView.findViewById(R.id.tvTransactionStatus);
            tvType = itemView.findViewById(R.id.tvTransactionType);
            tvDate = itemView.findViewById(R.id.tvTransactionDate);
            tvDescription = itemView.findViewById(R.id.tvTransactionDescription);
        }

        public void bind(Transaction transaction) {
            tvAmount.setText(String.format("%,.0fđ", transaction.getAmount()));

            // Set status
            String status = transaction.getStatus() != null ? transaction.getStatus() : "Unknown";
            tvStatus.setText("Status: " + status);

            // Set type
            String type = transaction.getType() != null ? transaction.getType() : "Unknown";
            tvType.setText("Type: " + type);

            // Set date
            Date date = transaction.getCreatedAt();
            if (date != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date);
                tvDate.setText("Date: " + formattedDate);
            } else {
                tvDate.setText("Date: Unknown");
            }

            // Description
            tvDescription.setText(transaction.getDescription() != null ? transaction.getDescription() : "");
        }
    }
}
