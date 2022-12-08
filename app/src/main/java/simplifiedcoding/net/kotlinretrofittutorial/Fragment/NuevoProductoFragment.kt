package simplifiedcoding.net.kotlinretrofittutorial.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_nuevo_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.activities.AppDatabase
import simplifiedcoding.net.kotlinretrofittutorial.controller.ImagenControlller
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto


class NuevoProductoFragment : Fragment() {
    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_producto, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var idProducto: Int? = null

        //if (getActivity()?.getIntent()?.hasExtra("producto") == true) {
            //val producto = getActivity()?.getIntent()?.extras?.getSerializable("producto") as Producto
        if(arguments!=null){
            nombre_et.setText(arguments?.getString("nombre",null))
            precio_et.setText(arguments?.getDouble("precio",0.0).toString())
            descripcion_et.setText(arguments?.getString("descripcion",null))
            idProducto = arguments?.getInt("id",-1)
            val imageUri = context?.let { idProducto?.let { it1 -> ImagenControlller.getImageUri(it, it1.toLong()) } }
            imageSelect_iv.setImageURI(imageUri)
        }

        val database = context?.let { AppDatabase.getDatabase(it) }

        save_btn.setOnClickListener {
            val nombre = nombre_et.text.toString()
            val precio = precio_et.text.toString().toDouble()
            val descripcion = descripcion_et.text.toString()

            val producto = Producto(nombre, precio, descripcion, R.drawable.ic_launcher_background)

            if (idProducto != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    producto.idProducto = idProducto
                    //Log.d("TAG", "${producto.idProducto}")
                    if (database != null) {
                        database.productos().update(producto)
                    }

                    imageUri?.let{
                        val intent = Intent()
                        intent.data = it
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.let { it1 ->
                            ImagenControlller.saveImage(
                                it1,
                                idProducto.toLong(),
                                it
                            )
                        }
                    }
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.relativelayout, ProductsFragment())?.commit()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = database?.productos()?.insertAll(producto)?.get(0)
                    imageUri?.let{
                        activity?.let { it1 ->
                            if (id != null) {
                                ImagenControlller.saveImage(it1, id, it)
                            }
                        }
                    }
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.relativelayout, ProductsFragment())?.commit()
                }
            }

        }
        imageSelect_iv.setOnClickListener {
            //Log.d("TAG", "holo")
            this?.let { it1 -> ImagenControlller.selectPhotoFromGallery(it1, SELECT_ACTIVITY) }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        //Log.d("TAG", "holoooo2")
        when{
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data

                imageSelect_iv.setImageURI(imageUri)
            }
        }

        }
    }
