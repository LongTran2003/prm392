package prm392.orderfood.domain.repositories;

import io.reactivex.Single;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.payment.CheckOutResponse;
import prm392.orderfood.domain.models.payment.PaymentResultResponse;

public interface PaymentRepository {
     Single<CheckOutResponse> createPayment(BankingOrderRequest request);
     Single<PaymentResultResponse> sendPaymentResult(String orderCode, String status);
}
