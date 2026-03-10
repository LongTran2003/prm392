package prm392.orderfood.data.repositoryImpl;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.PaymentDataSource;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.payment.CheckOutResponse;
import prm392.orderfood.domain.models.payment.PaymentResultResponse;
import prm392.orderfood.domain.repositories.PaymentRepository;

public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentDataSource paymentDataSource;

    @Inject
    public PaymentRepositoryImpl(PaymentDataSource paymentDataSource) {
        this.paymentDataSource = paymentDataSource;
    }

    @Override
    public Single<CheckOutResponse> createPayment(BankingOrderRequest request) {
        return paymentDataSource.createPayment(request);
    }

    @Override
    public Single<PaymentResultResponse> sendPaymentResult(String orderCode, String status) {
        return paymentDataSource.sendPaymentResult(orderCode, status);
    }


}
