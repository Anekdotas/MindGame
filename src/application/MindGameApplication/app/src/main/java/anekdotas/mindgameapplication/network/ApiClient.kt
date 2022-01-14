package anekdotas.mindgameapplication.network

import anekdotas.mindgameapplication.objects.Const
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

object ApiClient {
    private const val BASE_URL =
        Const.ipForNetworking //change to path if question location would change

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

    @GET("/categories")
    fun getCategories(): Call<List<CategoryModel>>

    @GET
    fun getTopics(@Url url: String?): Call<List<TopicModel>>

    @GET
    fun getProperQuestions(@Url url: String?,
        @Header("Authorization") Bearer: String,): Call<QuestionModelWithGameSessionId>

    @GET
    fun getRatedTopics(@Url url: String?,
                       @Header("Authorization") Bearer: String,): Call<RatedListModel>

    @POST("/auth/login")
    fun pushPostLogin(
        @Body post: UserModelTest
    ): Call<JwtTestModel>

    @POST("/auth/register")
    fun pushPostSignup(
        @Body post: RegistrationModel
    ): Call<Void>

    @POST("/categories/1/topics")
    fun pushPostTopic(
        @Header("Authorization") Bearer: String,
        @Body post: TopicPostModel
    ): Call<Void>

    @POST
    fun pushPostQuestions(
        @Url url: String?,
        @Header("Authorization") Bearer: String,
        @Body post: QuestionModelForLevelCreator
    ): Call<Void>

    @POST
    fun postStats(
        @Url url: String?,
        @Header("Authorization") Bearer: String,
        @Body post: StatModel
    ): Call<Void>

}