package simplifiedcoding.net.kotlinretrofittutorial.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import simplifiedcoding.net.kotlinretrofittutorial.models.DefaultResponse
import simplifiedcoding.net.kotlinretrofittutorial.models.LoginResponse

interface Api {

    @FormUrlEncoded
    @POST("register")
    fun createUser(
            @Field("name") name:String,
            @Field("email") email:String,
            @Field("rol") rol:String,
            @Field("password") password:String

    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
            @Field("email") email:String,
            @Field("password") password: String
    ):Call<LoginResponse>
}