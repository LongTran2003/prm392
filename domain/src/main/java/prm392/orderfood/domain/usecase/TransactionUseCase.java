package prm392.orderfood.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.transactions.Transaction;
import prm392.orderfood.domain.repositories.ShopRepository;
import prm392.orderfood.domain.repositories.TransactionRepository;
import retrofit2.Response;

public class TransactionUseCase {

    private final TransactionRepository transactionRepository;

    @Inject
    public TransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Admin
    public Single<List<Transaction>> getAllTransactionsByUserId(String userId) {
        return transactionRepository.getAllTransactionsByUserId(userId)
                .subscribeOn(Schedulers.io());
    }

    public Single<Transaction> requestWithdraw(String userId, double amount, String description) {
        return transactionRepository.requestWithdraw(userId, amount, description)
                .subscribeOn(Schedulers.io());
    }
}
