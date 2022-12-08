package simplifiedcoding.net.kotlinretrofittutorial.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_products.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.PhotosFragment
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.ProductsApiFragment
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.ProductsFragment
import simplifiedcoding.net.kotlinretrofittutorial.Fragment.SettingsFragment
import simplifiedcoding.net.kotlinretrofittutorial.adapter.MovieAdapter
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.adapter.ProductosAdapter
import simplifiedcoding.net.kotlinretrofittutorial.api.Constants.Co.storageUrl
import simplifiedcoding.net.kotlinretrofittutorial.models.Producto
import simplifiedcoding.net.kotlinretrofittutorial.storage.SharedPrefManager

class CircleTransform : Transformation {
    override fun transform(source: Bitmap?): Bitmap? {
        if (source == null) {
            return source
        }

        var bitmap: Bitmap

        // since we cant transform hardware bitmaps create a software copy first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && source.config == Bitmap.Config.HARDWARE) {
            val softwareCopy = source.copy(Bitmap.Config.ARGB_8888, true)
            if (softwareCopy == null) {
                return source
            } else {
                bitmap = softwareCopy
                source.recycle()
            }
        } else {
            bitmap = source
        }

        var size = bitmap.width
        // if bitmap is non-square first create square one
        if (size != bitmap.height) {
            var sizeX = size
            var sizeY = bitmap.height
            size = Math.min(sizeY, sizeX)
            sizeX = (sizeX - size) / 2
            sizeY = (sizeY - size) / 2

            val squareSource = Bitmap.createBitmap(bitmap, sizeX, sizeY, size, size)
            bitmap.recycle()
            bitmap = squareSource
        }

        val circleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circleBitmap)
        val paint = Paint()
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        paint.shader = shader
        paint.isAntiAlias = true
        val centerAndRadius = size / 2f
        canvas.drawCircle(centerAndRadius, centerAndRadius, centerAndRadius, paint)

        bitmap.recycle()
        return circleBitmap
    }


    override fun key(): String {
        return "circleTransformation()"
    }
}
    class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
        lateinit var adapter: MovieAdapter

        @SuppressLint("MissingInflatedId", "SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_profile)
            //updateInfo()
            setSupportActionBar(toolbar)


            //val intent = Intent(getActivity(), NuevoProductoActivity::class.java)
            //startActivity(intent)

            val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()
            nav_view.setNavigationItemSelectedListener(this)

            displayScreen(-1)


        }

        fun updateInfo() {
            val view: View = nav_view.getHeaderView(0)
            val sharedPreferences =
                this.getSharedPreferences("my_shared_preff", Context.MODE_PRIVATE)
            val name = sharedPreferences.getString("name", null)
            val email = sharedPreferences.getString("email", null)
            val img =
                storageUrl + sharedPreferences.getString("img", null)
            view.name_header.text = name
            view.email_header.text = email
            Picasso.get().load(img).transform(CircleTransform()).into(view.img_profile)
        }
        private fun displayScreen(id: Int) {
            when (id) {
                R.id.nav_home -> {
                    //supportFragmentManager.beginTransaction().replace(R.id.relativelayout, HomeFragment()).commit()
                    finish()
                    startActivity(getIntent())
                }

                R.id.nav_usuarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.relativelayout, PhotosFragment()).commit()
                }

                R.id.nav_productos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.relativelayout, ProductsApiFragment()).commit()
                    //val intent = Intent(this, ProductsActivity::class.java)
                    //startActivity(intent)
                }

                /*R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.relativelayout, SettingsFragment()).commit()
                }*/
                R.id.nav_logout -> {
                    SharedPrefManager.getInstance(applicationContext).clear()
                    onStart()
                }
            }
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // Handle navigation view item clicks here.

            displayScreen(item.itemId)

            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }

        @SuppressLint("SetTextI18n")
        override fun onStart() {
            super.onStart()

            if (!SharedPrefManager.getInstance(this).isLoggedIn) {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }



