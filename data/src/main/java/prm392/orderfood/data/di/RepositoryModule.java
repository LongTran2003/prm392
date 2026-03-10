package prm392.orderfood.data.di;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import prm392.orderfood.data.repositoryImpl.AuthRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.CategoryRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.MenuItemRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.OrderRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.PaymentRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.ShopRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.TransactionRepositoryImpl;
import prm392.orderfood.data.repositoryImpl.UserRepositoryImpl;
import prm392.orderfood.domain.repositories.AuthRepository;
import prm392.orderfood.domain.repositories.CategoryRepository;
import prm392.orderfood.domain.repositories.MenuItemRepository;
import prm392.orderfood.domain.repositories.OrderRepository;
import prm392.orderfood.domain.repositories.PaymentRepository;
import prm392.orderfood.domain.repositories.ShopRepository;
import prm392.orderfood.domain.repositories.TransactionRepository;
import prm392.orderfood.domain.repositories.UserRepository;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {
    @Binds
    @Singleton
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract UserRepository bindUserRepository(UserRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract ShopRepository bindShopRepository(ShopRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract CategoryRepository bindCategoryRepository(CategoryRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract MenuItemRepository bindMenuItemRepository(MenuItemRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract OrderRepository bindOrderRepository(OrderRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract PaymentRepository bindPaymentRepository(PaymentRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract TransactionRepository bindTransactionRepository (TransactionRepositoryImpl impl);
}
