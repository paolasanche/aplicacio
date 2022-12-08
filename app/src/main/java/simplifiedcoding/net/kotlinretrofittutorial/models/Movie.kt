package simplifiedcoding.net.kotlinretrofittutorial.models

import com.google.gson.annotations.SerializedName

data class Movie( val id: Int, val name: String, val price: String,val quantity: String)
data class MovieList (
        @SerializedName("compras" )
        val movies: List<Movie>
)
data class MovieEmbedded (
        @SerializedName("_embedded" )
        val list: MovieList
)

