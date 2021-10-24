package anekdotas.mindgameapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {
    private const val BASE_URL = "http://193.219.91.103:7576/" //change to path if question location would change

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // Retrofit client instance
    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiServices by lazy{
        retrofit.create(ApiServices::class.java)
    }
}

interface ApiServices {
    @GET("/topic2")
    fun fetchQuestions(): Call<List<QuestionModel>>
}