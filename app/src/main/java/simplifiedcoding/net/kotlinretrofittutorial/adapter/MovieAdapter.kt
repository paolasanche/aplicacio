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
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_photos.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.PhotosFragment
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.SettingsFragment
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.api.MovieApiClient
import simplifiedcoding.net.kotlinretrofittutorial.models.Movie

class MovieAdapter (val context: Context, val token: String?,val activity1: PhotosFragment?,val activity2: SettingsFragment?) :
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()  {

    val client by lazy { MovieApiClient.create() }
    var movies: ArrayList<Movie> = ArrayList()

    init { refreshMovies(context) }

    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return MovieViewHolder(view)

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.name.text = "Nombre: ${movies[position].name}"
        holder.view.price.text = "Precio: ${movies[position].price}"
        holder.view.quantity.text = "Cantidad: ${movies[position].quantity}"
        holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, movies[position]) }
        holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, movies[position]) }
    }

    fun showUpdateDialog(holder: MovieViewHolder, movie: Movie) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        val layoutInflater = LayoutInflater.from(holder.view.context)
        val view:View = layoutInflater.inflate(R.layout.dialog,null)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp

        view.dname.setText(movie.name)
        view.dprice.setText(movie.price)
        view.dquantity.setText(movie.quantity)
        dialogBuilder.setView(view)

        dialogBuilder.setTitle("Actualizar Usuario")
        dialogBuilder.setPositiveButton("Actualizar", { dialog, whichButton ->
            updateMovie(Movie(movie.id,view.dname.text.toString(),view.dprice.text.toString(),view.dquantity.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun getItemCount() = movies.size
    @SuppressLint("NotifyDataSetChanged")
    fun refreshMovies(context: Context) {

        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        //Log.e("TOKEN", "$token")
        client.getMovies("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            movies.clear()
                            movies.addAll(result.movies)
                            notifyDataSetChanged()
                        },
                        { error ->
                            Toast.makeText(context, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                            Log.e("ERRORS", error.message)
                        }
                )
    }
    /*fun up(movie: Movie) {
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", movie.nombre)
        editor.putString("correo", movie.correo)
        editor.apply()
        if(activity1!=null) {
            val navigationView =
                activity1?.requireActivity()?.findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            headerView.name_header.text = movie.nombre
            headerView.email_header.text = movie.correo
        }else if(activity2!=null){
            val navigationView =
                activity2?.requireActivity()?.findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            headerView.name_header.text = movie.nombre
            headerView.email_header.text = movie.correo
        }
    }*/
    fun updateMovie(movie: Movie) {
        val sp =
            context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val id = sp.getInt("id", -1)
        /*if(id==movie.idu) {
            up(movie)
        }*/
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        Log.d("ERROR","${movie}")
        client.updateMovie("Bearer $token",movie.id, movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshMovies(context)
                    Toast.makeText(context, "Datos actualizados", Toast.LENGTH_LONG).show()
                           }, { throwable ->
                    Toast.makeText(context, "Actualizar error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun addMovie(movie: Movie) {
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        client.addMovie("Bearer $token",movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshMovies(context) }, { throwable ->
                    Toast.makeText(context, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun deleteMovie(movie: Movie) {
        val sharedPreferences = context.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("fcm_token", null)
        client.deleteMovie("Bearer $token",movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshMovies(context) }, { throwable ->
                    Toast.makeText(context, "Eliminar error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )

    }



    fun showDeleteDialog(holder: MovieViewHolder, movie: Movie) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Eliminar")
        dialogBuilder.setMessage("Seguro de eliminar esta compra?")
        dialogBuilder.setPositiveButton("Eliminar", { dialog, whichButton ->
            deleteMovie(movie)
        })
        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}
