package simplifiedcoding.net.kotlinretrofittutorial.api

import android.view.View
import com.google.android.material.navigation.NavigationView
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.nav_header_main.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import simplifiedcoding.net.kotlinretrofittutorial.api.Constants.Co.baseUrl
import simplifiedcoding.net.kotlinretrofittutorial.models.Movie
import simplifiedcoding.net.kotlinretrofittutorial.models.MovieList
import simplifiedcoding.net.kotlinretrofittutorial.models.ProductApi
import simplifiedcoding.net.kotlinretrofittutorial.models.ProductList

interface MovieApiClient {

    @GET("compras") fun getMovies(@Header("Authorization") token:String): Observable<MovieList>
    @POST("compras") fun addMovie(@Header("Authorization") token:String,@Body movie: Movie): Completable
    @DELETE("compras/{id}") fun deleteMovie(@Header("Authorization") token:String,@Path("id") id: Int) : Completable
    @POST("compras/{id}") fun updateMovie(@Header("Authorization") token:String,@Path("id")id: Int, @Body movie: Movie) : Completable

    @GET("productos") fun getProducts(@Header("Authorization") token:String): Observable<ProductList>
    @POST("productos") fun addProduct(@Header("Authorization") token:String,@Body product: ProductApi): Completable
    @DELETE("productos/{id}") fun deleteProduct(@Header("Authorization") token:String,@Path("id") id: Int) : Completable
    @POST("productos/{id}") fun updateProduct(@Header("Authorization") token:String,@Path("id")id: Int, @Body product: ProductApi) : Completable
    companion object {

        fun create(): MovieApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()

            return retrofit.create(MovieApiClient::class.java)
        }
    }
}
