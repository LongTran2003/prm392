package prm392.orderfood.data.datasource.remote.api;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

import prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto;
import prm392.orderfood.domain.models.orders.UpdateOrderStatusRequest;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface OrderApiService {
    @POST("api/orders/create")
    Single<Response<ApiResponse<Object>>> createOrder(@Body BankingOrderRequest request);

    @GET("api/orders/{customerId}")
    Single<Response<ApiResponse<List<OrderResponseDto>>>> getCustomerOrders(@Path("customerId") String customerId);

    @GET("api/orders/shop/{shopId}")
    Single<Response<ApiResponse<List<OrderResponseDto>>>> getShopOrders(@Path("shopId") String shopId);

    @PUT("api/orders/{orderId}/status")
    Single<Response<ApiResponse<String>>> updateOrderStatus(@Path("orderId") String orderId, @Body UpdateOrderStatusRequest request);
}
