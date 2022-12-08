package simplifiedcoding.net.kotlinretrofittutorial.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.dialogp.view.*
import kotlinx.android.synthetic.main.item_producto.view.*
import kotlinx.android.synthetic.main.list_itemp.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.ProductsApiFragment
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.SettingsFragment
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.api.MovieApiClient
import simplifiedcoding.net.kotlinretrofittutorial.models.ProductApi

class ProductsApiAdapter (val context: Context, val token: String?,val activity1: ProductsApiFragment?,val activity2: SettingsFragment?) :
        RecyclerView.Adapter<ProductsApiAdapter.ProductViewHolder>()  {

    val client by lazy { MovieApiClient.create() }
    var products: ArrayList<ProductApi> = ArrayList()

    init { refreshProducts(context) }

    class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_itemp, parent, false)

        return ProductViewHolder(view)

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.view.namep.text = "Producto: ${products[position].name}"
        holder.view.tamano.text = "Tamaño: ${products[position].tamano}"
        holder.view.descripcion.text = "Descripción: ${products[position].descripcion}"
        holder.view.pricep.text = "Precio: ${products[position].price}"
        //holder.view.gallery.text = "Email: ${products[position].fotop}"
        holder.view.btnDeletep.setOnClickListener { showDeleteDialog(holder, products[position]) }
        holder.view.btnEditp.setOnClickListener { showUpdateDialog(holder, products[position]) }
    }

    fun showUpdateDialog(holder: ProductViewHolder, product: ProductApi) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        val layoutInflater = LayoutInflater.from(holder.view.context)
        val view:View = layoutInflater.inflate(R.layout.dialogp,null)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp

        view.dnamep.setText(product.name)
        view.dtamano.setText(product.tamano)
        view.ddescripcion.setText(product.descripcion)
        view.dpricep.setText(product.price)
        //view.dgallery.setText(product.gallery)
        dialogBuilder.setView(view)

        dialogBuilder.setTitle("Actualizar Producto")
        dialogBuilder.setPositiveButton("Actualizar", { dialog, whichButton ->
            updateProduct(ProductApi(product.id,view.dnamep.text.toString(),view.dtamano.text.toString(),view.ddescripcion.text.toString(),view.dpricep.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun getItemCount() = products.size
    @SuppressLint("NotifyDataSetChanged")
    fun refreshProducts(context: Context) {

        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        //Log.e("TOKEN", "$token")
        client.getProducts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            products.clear()
                            products.addAll(result.products)
                            notifyDataSetChanged()
                        },
                        { error ->
                            Toast.makeText(context, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                            Log.e("ERRORS", error.message)
                        }
                )
    }

    fun updateProduct(product: ProductApi) {
        val sp =
            context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val id = sp.getInt("id", -1)
        /*if(id==movie.idu) {
            up(movie)
        }*/
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        Log.d("ERROR","${product}")
        client.updateProduct("Bearer $token",product.id, product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshProducts(context)
                    Toast.makeText(context, "Datos actualizados", Toast.LENGTH_LONG).show()
                           }, { throwable ->
                    Toast.makeText(context, "Actualizar error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun addProduct(product: ProductApi) {
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        Log.d("TAG","{$product}")
        client.addProduct("Bearer $token",product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshProducts(context) }, { throwable ->
                    Toast.makeText(context, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun deleteProduct(product: ProductApi) {
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        client.deleteProduct("Bearer $token",product.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshProducts(context) }, { throwable ->
                    Toast.makeText(context, "Eliminar error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )

    }



    fun showDeleteDialog(holder: ProductViewHolder, product: ProductApi) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Eliminar")
        dialogBuilder.setMessage("Seguro de eliminar este producto?")
        dialogBuilder.setPositiveButton("Eliminar", { dialog, whichButton ->
            deleteProduct(product)
        })
        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}
