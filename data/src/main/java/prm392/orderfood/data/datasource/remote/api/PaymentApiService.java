package prm392.orderfood.data.datasource.remote.api;

import io.reactivex.Single;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.payment.CheckOutResponse;
import prm392.orderfood.domain.models.payment.PaymentResultRequest;
import prm392.orderfood.domain.models.payment.PaymentResultResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentApiService {
    @POST("api/payments/create-payment")
    Single<CheckOutResponse> createPayment(@Body BankingOrderRequest request);
    @POST("api/payments/payment-result")
    Single<PaymentResultResponse> sendPaymentResult(@Body PaymentResultRequest request);
}
