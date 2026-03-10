package prm392.orderfood.data.datasource.remote.api;

import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelRequest.transaction.WithdrawRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.transactions.Transaction;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransactionApi {

    @GET("api/v1/transactions/{userId}")
    Single<Response<ApiResponse<List<Transaction>>>> getAllTransactionsByUserId(@Path("userId") String userId);

    @POST("api/v1/transactions/withdraw")
    Single<Response<ApiResponse<Transaction>>> requestWithdraw(@Body WithdrawRequest request);
}
