package prm392.orderfood.data.repositoryImpl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.TransactionDataSource;
import prm392.orderfood.data.datasource.remote.modelRequest.transaction.WithdrawRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.transactions.Transaction;
import prm392.orderfood.domain.repositories.TransactionRepository;
import retrofit2.Response;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionDataSource dataSource;

    @Inject
    public TransactionRepositoryImpl(TransactionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Single<List<Transaction>> getAllTransactionsByUserId(String userId) {
        return dataSource.getAllTransactionsByUserId(userId)
                .map(apiResponse -> {
                    if (apiResponse.isSuccessful() && apiResponse.body() != null) {
                        ApiResponse<List<Transaction>> body = apiResponse.body();
                        List<Transaction> data = body.getData();
                        return data != null ? data : Collections.emptyList();
                    } else {
                        throw new RuntimeException("Failed to load transactions: " + apiResponse.code());
                    }
                });
    }

    @Override
    public Single<Transaction> requestWithdraw(String userId, double amount, String description) {
        WithdrawRequest request = new WithdrawRequest(userId, amount, description);
        return dataSource.requestWithdraw(request)
                .map(apiResponse -> {
                    if (apiResponse.isSuccessful() && apiResponse.body() != null) {
                        Transaction data = apiResponse.body().getData();
                        if (data != null) return data;
                        else throw new RuntimeException("No transaction data found in response");
                    } else {
                        throw new RuntimeException("Withdraw request failed: " + apiResponse.code());
                    }
                });
    }
}
