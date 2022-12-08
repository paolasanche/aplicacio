@file:Suppress("UNREACHABLE_CODE")

package simplifiedcoding.net.kotlinretrofittutorial.Fragment
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.dialogp.view.*
import kotlinx.android.synthetic.main.fragment_productsapi.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.adapter.ProductsApiAdapter
import simplifiedcoding.net.kotlinretrofittutorial.models.ProductApi


class ProductsApiFragment : Fragment() {
    lateinit var adapter: ProductsApiAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productsapi, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        fabp.setOnClickListener { showNewDialog(context) }
        val gc = activity?.baseContext
       adapter = gc?.let { ProductsApiAdapter(it,"abcd1234",this,null) }!!
        rv_item_listp.layoutManager = LinearLayoutManager(activity)
        rv_item_listp.adapter = adapter
    }

    fun showNewDialog(holder: Context?) {
        val dialogBuilder = holder?.let { AlertDialog.Builder(it) }
        //val view2:View = layoutInflater.inflate(R.layout.activity_profile,null).nav_view.getHeaderView(0)
        //view2.name_header.text = "hola"
        //Log.e("VIEW", "$view3")
        //Log.e("VIEW", "$headerView")
        val layoutInflater = LayoutInflater.from(holder)
        val view:View = layoutInflater.inflate(R.layout.dialogp,null)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp
        if (dialogBuilder != null) {
            dialogBuilder.setView(view)
        }
        if (dialogBuilder != null) {
            dialogBuilder.setTitle("Nuevo producto")
        }
        if (dialogBuilder != null) {
            dialogBuilder.setPositiveButton("Guardar", { dialog, whichButton ->
                adapter.addProduct(ProductApi(0,view.dnamep.text.toString(),view.dtamano.text.toString(),view.ddescripcion.text.toString(),view.dpricep.text.toString()))
            })
        }
        if (dialogBuilder != null) {
            dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
                //pass
            })
        }
        val b = dialogBuilder?.create()
        if (b != null) {
            b.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            context?.let { adapter.refreshProducts(it) }
            Toast.makeText(context, "Actualizado", Toast.LENGTH_LONG).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.buttons, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
