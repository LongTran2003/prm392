package prm392.orderfood.androidapp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.transactions.Transaction;
import prm392.orderfood.domain.usecase.TransactionUseCase;
import prm392.orderfood.androidapp.utils.SingleLiveEvent;
import retrofit2.Response;

@HiltViewModel
public class TransactionViewModel extends ViewModel {
    private final TransactionUseCase transactionUseCase;
    private final CompositeDisposable disposable;

    private final MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    private final SingleLiveEvent<String> toastMessage = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> errorMessage = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public TransactionViewModel(TransactionUseCase transactionUseCase) {
        this.transactionUseCase = transactionUseCase;
        this.disposable = new CompositeDisposable();
    }

    public LiveData<List<Transaction>> getTransactionsLiveData() {
        return transactions;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public void fetchTransactions(String userId) {
        isLoading.setValue(true);
        disposable.add(
                transactionUseCase.getAllTransactionsByUserId(userId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                transactionList -> {
                                    isLoading.setValue(false);
                                    if (transactionList != null && !transactionList.isEmpty()) {
                                        transactions.setValue(transactionList);
                                    } else {
                                        transactions.setValue(Collections.emptyList());
                                        toastMessage.setValue("No transactions found");
                                    }
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    errorMessage.setValue("Failed to load transactions: " + throwable.getMessage());
                                    Log.e("TransactionViewModel", "Error loading transactions", throwable);
                                }
                        )
        );
    }

    public void requestWithdraw(String userId, double amount, String description) {
        isLoading.setValue(true);
        disposable.add(
                transactionUseCase.requestWithdraw(userId, amount, description)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                transaction -> {
                                    isLoading.setValue(false);
                                    if (transaction != null) {
                                        toastMessage.setValue("Withdraw request submitted successfully");
                                    } else {
                                        errorMessage.setValue("Withdraw request failed: Empty response");
                                    }
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    errorMessage.setValue("Withdraw request failed: " + throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
