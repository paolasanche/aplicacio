package simplifiedcoding.net.kotlinretrofittutorial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.api.RetrofitClient
import simplifiedcoding.net.kotlinretrofittutorial.models.DefaultResponse
import simplifiedcoding.net.kotlinretrofittutorial.storage.SharedPrefManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewLogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }


        buttonSignUp.setOnClickListener {

            val nombre = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val rol = editTextRol.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(nombre.isEmpty()){
                editTextName.error = "Nombre requerido"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                editTextEmail.error = "Email requerido"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }
            if(rol.isEmpty()){
                editTextRol.error = "Rol requerido"
                editTextRol.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                editTextPassword.error = "Password requerido"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.createUser(nombre,email,rol,password)
                    .enqueue(object: Callback<DefaultResponse>{
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(applicationContext, /*response.body()?.message*/"Registro correcto", Toast.LENGTH_LONG).show()
                       Log.d("fdf","${response}")
                        }

                    })

        }
    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}
