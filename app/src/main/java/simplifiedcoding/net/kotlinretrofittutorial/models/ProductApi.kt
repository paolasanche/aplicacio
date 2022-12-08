package simplifiedcoding.net.kotlinretrofittutorial.models

import com.google.gson.annotations.SerializedName

data class ProductApi( val id: Int, val name: String, val tamano: String,val descripcion: String,val price: String )
data class ProductList (
        @SerializedName("products" )
        val products: List<ProductApi>
)
data class ProductEmbedded (
        @SerializedName("_embedded" )
        val list: ProductList
)

