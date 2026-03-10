package prm392.orderfood.data.datasource.remote;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.api.PaymentApiService;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.payment.CheckOutResponse;
import prm392.orderfood.domain.models.payment.PaymentResultRequest;
import prm392.orderfood.domain.models.payment.PaymentResultResponse;

public class PaymentDataSource {
    private final PaymentApiService paymentApiService;

    @Inject
    public PaymentDataSource(PaymentApiService paymentApiService) {
        this.paymentApiService = paymentApiService;
    }

    public Single<CheckOutResponse> createPayment(BankingOrderRequest request) {
        return paymentApiService.createPayment(request);
    }

    public Single<PaymentResultResponse> sendPaymentResult(String orderCode, String status) {
        return paymentApiService.sendPaymentResult(new PaymentResultRequest(orderCode, status));
    }
}
