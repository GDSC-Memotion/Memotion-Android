package konkuk.gdsc.memotion.di


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import konkuk.gdsc.memotion.data.api.DiaryApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://34.64.240.16:8080/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val retrofit = provideRetrofit(
        provideOkHttpClient(),
        provideJsonConverterFactory(provideJson())
    )

    val diary =
        requireNotNull(retrofit.create(DiaryApi::class.java)) { "DiaryApi instance is null" }
//    val emotion = retrofit.create(EmotionApi::class.java)
//    val user = retrofit.create(UserApi::class.java)


    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun provideRetrofit(
        client: OkHttpClient,
        factory: Converter.Factory
    ) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(factory)
        .client(client)
        .build()

    private fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun provideJsonConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }
}