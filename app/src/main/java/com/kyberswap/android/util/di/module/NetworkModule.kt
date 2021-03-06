package com.kyberswap.android.util.di.module

import android.content.Context
import com.kyberswap.android.BuildConfig
import com.kyberswap.android.R
import com.kyberswap.android.data.api.home.CurrencyApi
import com.kyberswap.android.data.api.home.LimitOrderApi
import com.kyberswap.android.data.api.home.PromoApi
import com.kyberswap.android.data.api.home.SwapApi
import com.kyberswap.android.data.api.home.TokenApi
import com.kyberswap.android.data.api.home.TransactionApi
import com.kyberswap.android.data.api.home.UserApi
import com.kyberswap.android.data.repository.datasource.storage.StorageMediator
import com.kyberswap.android.util.ErrorHandler
import com.kyberswap.android.util.TokenClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(storageMediator: StorageMediator): OkHttpClient {
        val client = OkHttpClient().newBuilder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
        client.addInterceptor {
            val original = it.request()
            val builder = original.newBuilder()

            val accessToken = storageMediator.getAccessToken()
            if (!accessToken.isNullOrEmpty()) {
                builder.header(
                    "Authorization", accessToken

                )
            }

            val request = builder.method(original.method(), original.body())
                .build()
            it.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideHomeApi(context: Context, client: OkHttpClient): TokenApi {
        return createApiClient(
            TokenApi::class.java,
            context.getString(R.string.token_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideWeb3j(context: Context, client: OkHttpClient): Web3j {
        return Web3j.build(
            HttpService(
                context.getString(R.string.base_rpc_url),
                client,
                false
            )
        )
    }


    @Provides
    @Singleton
    fun provideTransactionApi(context: Context, client: OkHttpClient): TransactionApi {
        return createApiClient(
            TransactionApi::class.java,
            context.getString(R.string.transaction_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideRateApi(context: Context, client: OkHttpClient): SwapApi {
        return createApiClient(
            SwapApi::class.java,
            context.getString(R.string.rate_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideUserApi(context: Context, client: OkHttpClient): UserApi {
        return createApiClient(
            UserApi::class.java,
            context.getString(R.string.user_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideLimitOrderApi(context: Context, client: OkHttpClient): LimitOrderApi {
        return createApiClient(
            LimitOrderApi::class.java,
            context.getString(R.string.user_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun providePromoApi(context: Context, client: OkHttpClient): PromoApi {
        return createApiClient(
            PromoApi::class.java,
            context.getString(R.string.user_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideErrorHandler(context: Context): ErrorHandler {
        return ErrorHandler(context)
    }

    @Provides
    @Singleton
    fun provideChange24hApi(context: Context, client: OkHttpClient): CurrencyApi {
        return createApiClient(
            CurrencyApi::class.java,
            context.getString(R.string.currency_endpoint_url),
            client
        )
    }

    @Provides
    @Singleton
    fun provideTokenClient(web3j: Web3j): TokenClient {
        return TokenClient(web3j)
    }

    private fun <T> createApiClient(clazz: Class<T>, baseUrl: String, client: OkHttpClient): T {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(clazz)
    }
}
