package com.ifs21005.lostandfound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.ifs21005.lostandfound.databinding.ActivityLoginBinding
import com.ifs21005.lostandfound.models.LoginRequest
import com.ifs21005.lostandfound.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() 
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val retrofit = RetrofitInstance().getRetrofit()

        binding.loadingAnimation.hide()

        binding.tombolLogin.setOnClickListener {
            binding.loadingAnimation.show()
            val apip = retrofit.create(Api::class.java)

            val request = LoginRequest(binding.inputEmailLogin.text.toString(), binding.inputPasswordLogin.text.toString())

            apip.loginApi(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(p0: Call<LoginResponse>, p1: Response<LoginResponse>) {
                    if (p1.isSuccessful) {
                        val shareddataref = applicationContext.getSharedPreferences(
                            "my_prefs_file",
                            Context.MODE_PRIVATE
                        )

                        val editor = shareddataref.edit()
                        editor.putString("auth_token", p1.body()?.data?.token)
                        editor.apply()

                        binding.loadingAnimation.hide()
                        finish()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    else {
                        Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                        binding.loadingAnimation.hide()
                    }
                }

                override fun onFailure(p0: Call<LoginResponse>, p1: Throwable) {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                    binding.loadingAnimation.hide()
                }
            })
        }

        binding.tombolHalamanRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, DaftarActivity::class.java))
        }
    }
}