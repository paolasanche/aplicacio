/*package simplifiedcoding.net.kotlinretrofittutorial.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import simplifiedcoding.net.kotlinretrofittutorial.controller.ImagenControlller
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto
import kotlinx.android.synthetic.main.activity_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import simplifiedcoding.net.kotlinretrofittutorial.R

class ProductoActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var producto: Producto
    private lateinit var productoLiveData: LiveData<Producto>
    private val EDIT_ACTIVITY = 49

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        database = AppDatabase.getDatabase(this)

        val idProducto = intent.getIntExtra("id", 0)
        val imageUri = ImagenControlller.getImageUri(this, idProducto.toLong())
        imagen.setImageURI(imageUri)
        productoLiveData = database.productos().get(idProducto)

        productoLiveData.observe(this, Observer {
            producto = it

            nombre_producto.text = producto.nombre
            precio_producto.text = "$${producto.precio}"
            detalles_producto.text = producto.descripcion
            //imagen.setImageResource(producto.imagen)
        })
        edit_item.setOnClickListener {
            val intent = Intent(this, NuevoProductoActivity::class.java)
            intent.putExtra("producto", producto)
            startActivityForResult(intent, EDIT_ACTIVITY)
        }
        delete_item.setOnClickListener {
            productoLiveData.removeObservers(this)

            CoroutineScope(Dispatchers.IO).launch {
                database.productos().delete(producto)
                ImagenControlller.deleteImage(
                    this@ProductoActivity,
                    producto.idProducto.toLong()
                )
                this@ProductoActivity.finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.producto_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                val intent = Intent(this, NuevoProductoActivity::class.java)
                intent.putExtra("producto", producto)
                startActivityForResult(intent, EDIT_ACTIVITY)
            }

            R.id.delete_item -> {
                productoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.productos().delete(producto)
                    ImagenControlller.deleteImage(
                        this@ProductoActivity,
                        producto.idProducto.toLong()
                    )
                    this@ProductoActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            resultCode == EDIT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imagen.setImageURI(data!!.data)
            }
        }
    }
}*/
