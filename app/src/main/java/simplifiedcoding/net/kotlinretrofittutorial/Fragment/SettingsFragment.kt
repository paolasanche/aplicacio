@file:Suppress("UNREACHABLE_CODE")
package simplifiedcoding.net.kotlinretrofittutorial.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import retrofit2.http.*
import simplifiedcoding.net.kotlinretrofittutorial.models.Movie
import simplifiedcoding.net.kotlinretrofittutorial.adapter.MovieAdapter
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.activities.CircleTransform
import simplifiedcoding.net.kotlinretrofittutorial.api.Constants.Co.storageUrl
import simplifiedcoding.net.kotlinretrofittutorial.api.MovieApiClient
import simplifiedcoding.net.kotlinretrofittutorial.models.MovieList

class SettingsFragment : Fragment() {
    lateinit var adapter: MovieAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }


            fun up() {
                val navigationView =
                    requireActivity().findViewById<View>(R.id.nav_view) as NavigationView
                val headerView = navigationView.getHeaderView(0)
                headerView.name_header.text = "holaa"
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getInt("id", -1)
        val name = sharedPreferences?.getString("nombre", null)
        val empresa = sharedPreferences?.getString("empresa", null)
        val detalles = sharedPreferences?.getString("detalles", null)
        val tipou = sharedPreferences?.getString("tipou", null)
        val email = sharedPreferences?.getString("email", null)
        val img =
            storageUrl + (sharedPreferences?.getString("img", null) ?: 1)
        et_nombre.setText(name)
        ""
        ""
        ""
        ""
        //et_empresa.setText(empresa)
        //et_detalles.setText(detalles)
        //et_tipou.setText(tipou)
        et_email.setText(email)
        //Picasso.get().load(img).transform(CircleTransform()).into(update_profile)
        val gc = activity?.baseContext
        adapter = gc?.let { MovieAdapter(/*this.baseContext*/it, /*readAuthState().accessToken*/"abcd1234",null,this) }!!
        bt_register.setOnClickListener {
            id?.let { it1 ->
                Movie(
                    it1,
                    et_nombre.getText().toString(),
                    "",
                    "",

                )
            }?.let { it2 -> adapter.updateMovie(it2) }
        }
    }
}