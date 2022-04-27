package az.zero.azpokedex.di

import az.zero.azpokedex.BuildConfig
import az.zero.azpokedex.data.remote.ApiService
import az.zero.azpokedex.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private const val TIME_OUT = 15L

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient().newBuilder().apply {
            callTimeout(TIME_OUT, TimeUnit.SECONDS)
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        }.build()


//    @Provides
//    @Singleton
//    fun provideMainRepository(api: ApiService) = PokemonRepository(api)

    @Provides
    @Singleton
    fun provideApiService(
        client: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)
    }


}