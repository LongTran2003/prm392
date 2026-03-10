package prm392.orderfood.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.payment.CheckOutResponse;
import prm392.orderfood.domain.models.payment.PaymentResultResponse;
import prm392.orderfood.domain.repositories.PaymentRepository;

public class PaymentUseCase {
    private final PaymentRepository paymentRepository;

    @Inject
    public PaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Single<CheckOutResponse> createPayment(BankingOrderRequest request) {
        return paymentRepository.createPayment(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io());
    }

    public Single<PaymentResultResponse> sendPaymentResult(String orderCode, String status) {
        return paymentRepository.sendPaymentResult(orderCode, status)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io());
    }
}
