package simplifiedcoding.net.kotlinretrofittutorial.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.activities.AppDatabase
import simplifiedcoding.net.kotlinretrofittutorial.controller.ImagenControlller
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto
import android.view.Menu
import android.view.MenuInflater
import kotlinx.android.synthetic.main.fragment_nuevo_producto.*


class ProductoFragment : Fragment() {
    private lateinit var database: AppDatabase
    private lateinit var producto: Producto
    private lateinit var productoLiveData: LiveData<Producto>
    private val EDIT_ACTIVITY = 49
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_producto, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        database = context?.let { AppDatabase.getDatabase(it) }!!
        val idProducto = arguments?.getInt("id",-1)/*activity?.intent?.getIntExtra("id", 0)*/
        //Log.d("ID", "$idProducto")
        val imageUri = idProducto?.let { ImagenControlller.getImageUri(requireContext(), it.toLong()) }
        imagen.setImageURI(imageUri)
        productoLiveData = idProducto?.let { database.productos().get(it) }!!

        productoLiveData.observe(viewLifecycleOwner, Observer {
            producto = it

            nombre_producto.text = producto.nombre
            precio_producto.text = "$${producto.precio}"
            detalles_producto.text = producto.descripcion
            //imagen.setImageResource(producto.imagen)
        })
        edit_item.setOnClickListener {
            //val intent = Intent(activity, NuevoProductoFragment::class.java)
            //intent.putExtra("producto", producto)
            //startActivityForResult(intent, EDIT_ACTIVITY)
            val frag_03 = NuevoProductoFragment()
            val bundle = Bundle()
            bundle.putInt("id",producto.idProducto)
            bundle.putString("nombre",producto.nombre)
            bundle.putDouble("precio",producto.precio)
            bundle.putString("descripcion",producto.descripcion)
            bundle.putInt("img",producto.imagen)

            frag_03.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.relativelayout, frag_03)?.commit()
        }
        delete_item.setOnClickListener {
            productoLiveData.removeObservers(this)

            CoroutineScope(Dispatchers.IO).launch {
                database.productos().delete(producto)
                ImagenControlller.deleteImage(
                    requireContext(),
                    producto.idProducto.toLong()
                )
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.relativelayout, ProductsFragment())?.commit()
            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.producto_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                val intent = Intent(activity, NuevoProductoFragment::class.java)
                intent.putExtra("producto", producto)
                startActivityForResult(intent, EDIT_ACTIVITY)
            }

            R.id.delete_item -> {
                productoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.productos().delete(producto)
                    context?.let {
                        ImagenControlller.deleteImage(
                            it,
                            producto.idProducto.toLong()
                        )
                    }
                    activity?.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            resultCode == EDIT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imagen.setImageURI(data!!.data)
            }
        }

        }
    }
