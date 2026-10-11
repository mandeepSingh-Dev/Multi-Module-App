package com.uae.core_network.di

//import com.uae.core_common.UserManager
//import com.uae.core_common.datastore.DatastoreManager
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.ApiConstants.CONNECTION_TIME
import com.uae.core_network.networkUtils.ApiConstants.WRITE_TIME
import com.uae.core_network.networkUtils.interceptors.AuthorizationInterceptor
import com.uae.core_network.networkUtils.interceptors.CacheInterceptors
import com.uae.core_network.networkUtils.interceptors.ErrorInterceptors
import com.uae.core_network.networkUtils.interceptors.UnAuthorizeInterceptor
import android.content.Context
import com.app.core_network.BuildConfig
import com.google.gson.GsonBuilder
import com.uae.core_network.data.DecryptionInterceptor
import com.uae.core_network.data.EncryptionInterceptor
import com.uae.core_network.data.service.ServerKeyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptors(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideErrorInterceptors(@ApplicationContext context: Context): ErrorInterceptors =
        ErrorInterceptors(context)

//    @Provides
//    fun provideAuthInterceptors(userManager: UserManager, appDatastore: DatastoreManager) : AuthorizationInterceptor = AuthorizationInterceptor(userManager = userManager, appDatastore = appDatastore)


    @Provides
    @Singleton
//    @Named(DIConstants.APP_RETROFIT)
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        cacheInterceptors: CacheInterceptors,
        errorInterceptors: ErrorInterceptors,
        encryptionInterceptor: EncryptionInterceptor,
        decryptionInterceptor: DecryptionInterceptor,
//        authorizationInterceptor: AuthorizationInterceptor,
        unAuthorizeInterceptor: UnAuthorizeInterceptor /* unreadCountsApiInterceptor: UnreadCountsApiInterceptor,*/
    ): OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
        .readTimeout(ApiConstants.READ_TIME, TimeUnit.SECONDS)
        .connectTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
        .let {
            if (BuildConfig.BUILD_TYPE.lowercase() == "debug") {
                it.addInterceptor(loggingInterceptor)
            } else {
                it
            }
        }
        .addInterceptor(errorInterceptors)
//        .addInterceptor(authorizationInterceptor)
        .addInterceptor(unAuthorizeInterceptor)
        .addInterceptor(encryptionInterceptor)
        .addInterceptor(decryptionInterceptor)
//        .addInterceptor(unreadCountsApiInterceptor)
//        .addInterceptor(cacheInterceptors)
        //10 MB cache size.
        .cache(Cache(context.cacheDir, (10 * 10 * 1024).toLong()))
        .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(/*@Named(DIConstants.APP_RETROFIT)*/ okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().disableHtmlEscaping().create()
                )
            )
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    @KeyOkHttp
    fun provideKeyOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            // IMPORTANT:
            // No EncryptionInterceptor here
            .build()
    }

    @Provides
    @Singleton
    @KeyRetrofit
    fun provideKeyRetrofit(
        @KeyOkHttp okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideServerKeyService(
        @KeyRetrofit retrofit: Retrofit
    ): ServerKeyService {
        return retrofit.create(ServerKeyService::class.java)
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KeyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KeyOkHttp