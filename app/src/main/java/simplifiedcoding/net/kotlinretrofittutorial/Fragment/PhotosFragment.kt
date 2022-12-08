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
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.fragment_photos.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.adapter.MovieAdapter
import simplifiedcoding.net.kotlinretrofittutorial.models.Movie


class PhotosFragment : Fragment() {
    lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        fab.setOnClickListener { showNewDialog(context) }
        val gc = activity?.baseContext
       adapter = gc?.let { MovieAdapter(it,"abcd1234",this,null) }!!
        rv_item_list.layoutManager = LinearLayoutManager(activity)
        rv_item_list.adapter = adapter
    }

    fun showNewDialog(holder: Context?) {
        val dialogBuilder = holder?.let { AlertDialog.Builder(it) }
        //val view2:View = layoutInflater.inflate(R.layout.activity_profile,null).nav_view.getHeaderView(0)
        //view2.name_header.text = "hola"
        //Log.e("VIEW", "$view3")
        //Log.e("VIEW", "$headerView")
        val layoutInflater = LayoutInflater.from(holder)
        val view:View = layoutInflater.inflate(R.layout.dialog,null)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp
        if (dialogBuilder != null) {
            dialogBuilder.setView(view)
        }
        if (dialogBuilder != null) {
            dialogBuilder.setTitle("Nueva Compra")
        }
        if (dialogBuilder != null) {
            dialogBuilder.setPositiveButton("Guardar", { dialog, whichButton ->
                adapter.addMovie(Movie(0,view.dname.text.toString(),view.dprice.text.toString(),view.dquantity.text.toString()))
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
            context?.let { adapter.refreshMovies(it) }
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
