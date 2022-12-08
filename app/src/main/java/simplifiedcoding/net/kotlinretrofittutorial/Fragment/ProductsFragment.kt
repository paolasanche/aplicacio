package simplifiedcoding.net.kotlinretrofittutorial.Fragment

//import simplifiedcoding.net.kotlinretrofittutorial.activities.NuevoProductoActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_products.*
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.activities.AppDatabase
import simplifiedcoding.net.kotlinretrofittutorial.adapter.ProductosAdapter
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto


class ProductsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var listaProductos = emptyList<Producto>()

        val database = context?.let { AppDatabase.getDatabase(it) }

        if (database != null) {
            database.productos().getAll().observe(viewLifecycleOwner, Observer {
                listaProductos = it
                val adapter = ProductosAdapter(this, listaProductos)

                lista.adapter = adapter
            })
        }

        lista.setOnItemClickListener { parent, view, position, id ->
            //val intent = Intent(activity, ProductoFragment::class.java)
            //intent.putExtra("id", listaProductos[position].idProducto)
            val frag_02 = ProductoFragment()
            val bundle = Bundle()
            bundle.putInt("id",listaProductos[position].idProducto)
            frag_02.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.relativelayout, frag_02)?.commit()
        //startActivity(intent)
        }

        floatingActionButton.setOnClickListener {
            //val intent = Intent(activity, NuevoProductoFragment::class.java)
            //startActivity(intent)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.relativelayout, NuevoProductoFragment())?.commit()
        }

        }
    }
