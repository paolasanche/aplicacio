package simplifiedcoding.net.kotlinretrofittutorial.activities

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import simplifiedcoding.net.kotlinretrofittutorial.R
import simplifiedcoding.net.kotlinretrofittutorial.api.RetrofitClient
import simplifiedcoding.net.kotlinretrofittutorial.models.LoginResponse
import simplifiedcoding.net.kotlinretrofittutorial.storage.SharedPrefManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        buttonLogin.setOnClickListener {

            val correo = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(correo.isEmpty()){
                editTextEmail.error = "Email requerido"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password requerido"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(correo, password)
                    .enqueue(object: Callback<LoginResponse>{
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            //if(!response.body()?.error!!){
                            Log.e("RES2", response.body().toString())
                            if (response.isSuccessful) {

                                SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)

                                val intent = Intent(applicationContext, ProfileActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)


                            }else{
                                Log.e("RES", response.toString())
                                Toast.makeText(applicationContext, /*response.body()?.message*/"Credenciales inválidas", Toast.LENGTH_LONG).show()
                            }

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
