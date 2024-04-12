package com.ifs21005.lostandfound

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.ifs21005.lostandfound.databinding.ActivityDaftarBinding
import com.ifs21005.lostandfound.models.RegisterRequest
import com.ifs21005.lostandfound.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarActivity : AppCompatActivity() {

    lateinit var binding : ActivityDaftarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this@DaftarActivity, R.layout.activity_daftar)

        binding.klikLogin.setOnClickListener {
        startActivity(Intent(this@DaftarActivity, LoginActivity::class.java))
        }

        binding.loadingAnimation.hide()

        val retrofit = RetrofitInstance().getRetrofit()

        binding.buttonRegisterPerson.setOnClickListener {
            binding.loadingAnimation.show()

            val daftar = RegisterRequest(
                binding.inputanNamaRegistrasi.text.toString(),
                binding.inputanEmailRegistrasi.text.toString(),
                binding.inputanPasswordRegistrasi.text.toString()
            )

            val api = retrofit.create(Api::class.java)
            api.registerApi(daftar).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                p0: Call<RegisterResponse>,
                p1: Response<RegisterResponse>
            ) {
                if (p1.isSuccessful) {
                    Toast.makeText(this@DaftarActivity, p1.body()?.message, Toast.LENGTH_LONG).show()
                    binding.loadingAnimation.hide()
                    finish()
                    val intent = Intent(this@DaftarActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                else {
                    Toast.makeText(this@DaftarActivity, "Login Gagal", Toast.LENGTH_LONG).show()
                    binding.loadingAnimation.hide()
                }
            }

                    override fun onFailure(p0: Call<RegisterResponse>, p1: Throwable) {
                        Toast.makeText(this@DaftarActivity, "Login Gagal", Toast.LENGTH_LONG).show()
                        binding.loadingAnimation.hide()
                    }
            });
        }
    }
}