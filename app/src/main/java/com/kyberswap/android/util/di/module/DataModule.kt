package com.kyberswap.android.util.di.module

import android.content.Context
import com.kyberswap.android.data.api.home.CurrencyApi
import com.kyberswap.android.data.api.home.LimitOrderApi
import com.kyberswap.android.data.api.home.PromoApi
import com.kyberswap.android.data.api.home.SwapApi
import com.kyberswap.android.data.api.home.TokenApi
import com.kyberswap.android.data.api.home.TransactionApi
import com.kyberswap.android.data.api.home.UserApi
import com.kyberswap.android.data.db.AlertDao
import com.kyberswap.android.data.db.ContactDao
import com.kyberswap.android.data.db.LimitOrderDao
import com.kyberswap.android.data.db.LocalLimitOrderDao
import com.kyberswap.android.data.db.OrderFilterDao
import com.kyberswap.android.data.db.PassCodeDao
import com.kyberswap.android.data.db.PendingBalancesDao
import com.kyberswap.android.data.db.RateDao
import com.kyberswap.android.data.db.SendDao
import com.kyberswap.android.data.db.SwapDao
import com.kyberswap.android.data.db.TokenDao
import com.kyberswap.android.data.db.TransactionDao
import com.kyberswap.android.data.db.TransactionFilterDao
import com.kyberswap.android.data.db.UnitDao
import com.kyberswap.android.data.db.UserDao
import com.kyberswap.android.data.db.WalletDao
import com.kyberswap.android.data.mapper.AlertMapper
import com.kyberswap.android.data.mapper.CapMapper
import com.kyberswap.android.data.mapper.ChartMapper
import com.kyberswap.android.data.mapper.FeeMapper
import com.kyberswap.android.data.mapper.GasMapper
import com.kyberswap.android.data.mapper.OrderMapper
import com.kyberswap.android.data.mapper.PromoMapper
import com.kyberswap.android.data.mapper.RateMapper
import com.kyberswap.android.data.mapper.TokenMapper
import com.kyberswap.android.data.mapper.TransactionMapper
import com.kyberswap.android.data.mapper.UserMapper
import com.kyberswap.android.data.repository.AlertDataRepository
import com.kyberswap.android.data.repository.BalanceDataRepository
import com.kyberswap.android.data.repository.ContactDataRepository
import com.kyberswap.android.data.repository.LimitOrderDataRepository
import com.kyberswap.android.data.repository.MnemonicDataRepository
import com.kyberswap.android.data.repository.SettingDataRepository
import com.kyberswap.android.data.repository.SwapDataRepository
import com.kyberswap.android.data.repository.TokenDataRepository
import com.kyberswap.android.data.repository.TransactionDataRepository
import com.kyberswap.android.data.repository.UserDataRepository
import com.kyberswap.android.data.repository.WalletDataRepository
import com.kyberswap.android.data.repository.datasource.storage.StorageMediator
import com.kyberswap.android.domain.repository.AlertRepository
import com.kyberswap.android.domain.repository.BalanceRepository
import com.kyberswap.android.domain.repository.ContactRepository
import com.kyberswap.android.domain.repository.LimitOrderRepository
import com.kyberswap.android.domain.repository.MnemonicRepository
import com.kyberswap.android.domain.repository.SettingRepository
import com.kyberswap.android.domain.repository.SwapRepository
import com.kyberswap.android.domain.repository.TokenRepository
import com.kyberswap.android.domain.repository.TransactionRepository
import com.kyberswap.android.domain.repository.UserRepository
import com.kyberswap.android.domain.repository.WalletRepository
import com.kyberswap.android.util.TokenClient
import dagger.Module
import dagger.Provides
import org.bitcoinj.crypto.MnemonicCode
import javax.inject.Singleton

@Module
object DataModule {
    @Singleton
    @Provides
    @JvmStatic
    fun provideWalletRepository(
        context: Context,
        walletDao: WalletDao,
        unitDao: UnitDao,
        tokenDao: TokenDao,
        promoApi: PromoApi,
        promoMapper: PromoMapper,
        swapDao: SwapDao,
        sendDao: SendDao,
        limitOrderDao: LocalLimitOrderDao
    ): WalletRepository =
        WalletDataRepository(
            context,
            walletDao,
            unitDao,
            tokenDao,
            promoApi,
            promoMapper,
            swapDao,
            sendDao,
            limitOrderDao
        )

    @Singleton
    @Provides
    @JvmStatic
    fun provideBalanceRepository(
        api: TokenApi,
        currencyApi: CurrencyApi,
        tokenMapper: TokenMapper,
        client: TokenClient,
        tokenDao: TokenDao,
        walletDao: WalletDao,
        swapDao: SwapDao,
        sendDao: SendDao,
        localLimitOrderDao: LocalLimitOrderDao

    ): BalanceRepository =
        BalanceDataRepository(
            api,
            currencyApi,
            tokenMapper,
            client,
            tokenDao,
            walletDao,
            swapDao,
            sendDao,
            localLimitOrderDao
        )

    @Singleton
    @Provides
    @JvmStatic
    fun provideTokenRepository(
        client: TokenClient,
        api: SwapApi,
        tokenApi: TokenApi,
        rateDao: RateDao,
        tokenDao: TokenDao,
        rateMapper: RateMapper,
        chartMapper: ChartMapper,
        context: Context
    ): TokenRepository =
        TokenDataRepository(
            client,
            api,
            tokenApi,
            rateDao,
            tokenDao,
            rateMapper,
            chartMapper,
            context
        )

    @Singleton
    @Provides
    @JvmStatic
    fun provideMnemonicRepository(mnemonicCode: MnemonicCode): MnemonicRepository =
        MnemonicDataRepository(mnemonicCode)

    @Singleton
    @Provides
    @JvmStatic
    fun provideSwapRepository(
        context: Context,
        swapDao: SwapDao,
        tokenDao: TokenDao,
        sendDao: SendDao,
        contactDao: ContactDao,
        api: SwapApi,
        mapper: GasMapper,
        capMapper: CapMapper,
        tokenClient: TokenClient,
        transactionDao: TransactionDao,
        userDao: UserDao,
        userApi: UserApi,
        userMapper: UserMapper
    ): SwapRepository =
        SwapDataRepository(
            context,
            swapDao,
            tokenDao,
            sendDao,
            contactDao,
            api,
            mapper,
            capMapper,
            tokenClient,
            transactionDao,
            userDao,
            userApi,
            userMapper
        )

    @Singleton
    @Provides
    @JvmStatic
    fun provideContactRepository(
        contactDao: ContactDao,
        sendDao: SendDao
    ): ContactRepository =
        ContactDataRepository(contactDao, sendDao)


    @Singleton
    @Provides
    @JvmStatic
    fun provideTransactionRepository(
        api: TransactionApi,
        transactionDao: TransactionDao,
        mapper: TransactionMapper,
        tokenClient: TokenClient,
        tokenDao: TokenDao,
        swapDao: SwapDao,
        sendDao: SendDao,
        limitOrderDao: LocalLimitOrderDao,
        transactionFilterDao: TransactionFilterDao,
        context: Context
    ): TransactionRepository =
        TransactionDataRepository(
            api,
            transactionDao,
            mapper,
            tokenClient,
            tokenDao,
            swapDao,
            sendDao,
            limitOrderDao,
            transactionFilterDao,
            context
        )


    @Singleton
    @Provides
    @JvmStatic
    fun provideUserRepository(
        api: UserApi,
        userDao: UserDao,
        storageMediator: StorageMediator,
        userMapper: UserMapper,
        alertDao: AlertDao
    ): UserRepository =
        UserDataRepository(api, userDao, storageMediator, userMapper, alertDao)


    @Singleton
    @Provides
    @JvmStatic
    fun provideLimitOrderRepository(
        context: Context,
        tokenDao: TokenDao,
        localLimitOrderDao: LocalLimitOrderDao,
        orderFilterDao: OrderFilterDao,
        dao: LimitOrderDao,
        pendingBalancesDao: PendingBalancesDao,
        api: LimitOrderApi,
        mapper: OrderMapper,
        feeMapper: FeeMapper,
        tokenClient: TokenClient
    ): LimitOrderRepository =
        LimitOrderDataRepository(
            context,
            dao,
            localLimitOrderDao,
            orderFilterDao,
            tokenDao,
            pendingBalancesDao,
            api,
            tokenClient,
            mapper,
            feeMapper
        )


    @Singleton
    @Provides
    @JvmStatic
    fun provideAlertRepository(
        alertDao: AlertDao,
        tokenDao: TokenDao,
        userApi: UserApi,
        alertMapper: AlertMapper
    ): AlertRepository =
        AlertDataRepository(
            alertDao,
            tokenDao,
            userApi,
            alertMapper
        )

    @Singleton
    @Provides
    @JvmStatic
    fun providePassCodeRepository(
        passCodeDao: PassCodeDao
    ): SettingRepository =
        SettingDataRepository(
            passCodeDao
        )
}