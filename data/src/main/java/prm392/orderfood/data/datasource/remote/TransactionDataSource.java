package prm392.orderfood.data.datasource.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.api.ShopApiService;
import prm392.orderfood.data.datasource.remote.api.TransactionApi;
import prm392.orderfood.data.datasource.remote.modelRequest.shop.ApproveShopRequest;
import prm392.orderfood.data.datasource.remote.modelRequest.transaction.WithdrawRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.PagingResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopResponse;
import prm392.orderfood.domain.models.transactions.Transaction;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class TransactionDataSource {
    private final TransactionApi api;

    @Inject
    public TransactionDataSource(TransactionApi api) {
        this.api = api;
    }

    public Single<Response<ApiResponse<List<Transaction>>>> getAllTransactionsByUserId(@Path("userId") String userId) {
        return api.getAllTransactionsByUserId(userId);
    }

    public Single<Response<ApiResponse<Transaction>>> requestWithdraw(@Body WithdrawRequest request) {
        return api.requestWithdraw(request);
    }
}
