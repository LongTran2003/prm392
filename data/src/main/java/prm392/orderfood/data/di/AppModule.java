package prm392.orderfood.data.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import prm392.orderfood.data.datasource.remote.api.OrderApiService;
import prm392.orderfood.data.datasource.local.TokenLocalDataSource;
import prm392.orderfood.data.datasource.remote.api.AuthApiService;
import prm392.orderfood.data.datasource.remote.api.CategoryApiService;
import prm392.orderfood.data.datasource.remote.api.MenuItemApiService;
import prm392.orderfood.data.datasource.remote.api.PaymentApiService;
import prm392.orderfood.data.datasource.remote.api.ShopApiService;
import prm392.orderfood.data.datasource.remote.api.TransactionApi;
import prm392.orderfood.data.datasource.remote.api.UserApiService;
import prm392.orderfood.data.network.AuthInterceptor;
import prm392.orderfood.data.network.RetrofitClient;
import retrofit2.Retrofit;


@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public AuthInterceptor provideAuthInterceptor(TokenLocalDataSource tokenLocalDataSource) {
        return new AuthInterceptor(tokenLocalDataSource);
    }

    @Singleton
    @Provides
    public OrderApiService provideOrderApiService(Retrofit retrofit) {
        return retrofit.create(OrderApiService.class);
    }


    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS) // Thời gian tối đa để kết nối
                    .readTimeout(30, TimeUnit.SECONDS)    // Thời gian tối đa để đọc response
                    .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian tối đa để ghi request
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return RetrofitClient.create(okHttpClient);
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    public AuthApiService provideAuthApiService(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    // Nếu có các ApiService khác, cung cấp chúng tương tự như AuthApiService
    @Singleton
    @Provides
    public UserApiService provideUserApiService(Retrofit retrofit) {
        return retrofit.create(UserApiService.class);
    }

    @Singleton
    @Provides
    public ShopApiService provideShopApiService(Retrofit retrofit) {
        return retrofit.create(ShopApiService.class);
    }

    @Singleton
    @Provides
    public CategoryApiService provideCategoryApiService(Retrofit retrofit) {
        return retrofit.create(CategoryApiService.class);
    }

    @Singleton
    @Provides
    public MenuItemApiService provideMenuItemApiService(Retrofit retrofit) {
        return retrofit.create(MenuItemApiService.class);
    }

    @Singleton
    @Provides
    public PaymentApiService providePaymentApiService(Retrofit retrofit) {
        return retrofit.create(PaymentApiService.class);
    }

    @Singleton
    @Provides
    public TransactionApi provideTransactionApi(Retrofit retrofit) {
        return retrofit.create(TransactionApi.class);
    }
}