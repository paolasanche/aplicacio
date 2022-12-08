package simplifiedcoding.net.kotlinretrofittutorial.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import simplifiedcoding.net.kotlinretrofittutorial.controller.ImagenControlller
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto
import kotlinx.android.synthetic.main.item_producto.view.*
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.ProductsFragment
import simplifiedcoding.net.kotlinretrofittutorial.R

class ProductosAdapter(private val mContext: ProductsFragment, private val listaProductos: List<Producto>) :
    ArrayAdapter<Producto>(mContext.context, 0, listaProductos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext.context).inflate(R.layout.item_producto, parent, false)

        val producto = listaProductos[position]

        layout.nombre.text = producto.nombre
        layout.precio.text = "$${producto.precio}"
        //layout.imageView.setImageResource(producto.imagen)
        val imageUri = mContext.context?.let { ImagenControlller.getImageUri(it, producto.idProducto.toLong()) }
        layout.imageView.setImageURI(imageUri)
        return layout
    }

}