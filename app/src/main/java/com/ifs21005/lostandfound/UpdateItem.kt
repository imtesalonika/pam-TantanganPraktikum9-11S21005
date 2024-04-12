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
import com.ifs21005.lostandfound.models.UpdateData
import com.ifs21005.lostandfound.databinding.ActivityUpdateItemBinding
import com.ifs21005.lostandfound.models.UpdateLostFoundResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateItem : AppCompatActivity() {

    lateinit var binding : ActivityUpdateItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this@UpdateItem, R.layout.activity_update_item)

        val id = intent.getIntExtra("id", 0)
        val judul = intent.getStringExtra("title")
        val deskripsi = intent.getStringExtra("desc")

        binding.inputTitleUpdate.setText(judul)
        binding.inputDescUpdate.setText(deskripsi)

        val retrofit = RetrofitInstance().getRetrofit()
        val apiService = retrofit.create(Api::class.java)

        val sharedPref = this@UpdateItem.getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )

        val myValue = sharedPref?.getString("auth_token", "")

        val authToken = "Bearer $myValue"


        binding.buttonUpdateLostandfound.setOnClickListener {
            var isCompleted = 0

            if (binding.inputCompletedUpdate.text.toString() == "Yes") {
                isCompleted = 1
            }

            val body = UpdateData(
                binding.inputTitleUpdate.text.toString(),
                binding.inputDescUpdate.text.toString(),
                binding.inputStatusUpdate.text.toString().lowercase(),
                isCompleted
            )
            val call = apiService.updateLostFoundApi(authToken, id.toString(), body)

            call.enqueue(object : Callback<UpdateLostFoundResponse> {
                override fun onResponse(
                    p0: Call<UpdateLostFoundResponse>,
                    p1: Response<UpdateLostFoundResponse>
                ) {
                    if (p1.isSuccessful) {
                        Toast.makeText(this@UpdateItem, "Sukses update!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateItem, MainActivity::class.java))
                        finish()
                    }

                    else {
                        Toast.makeText(this@UpdateItem, "Gagal update!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(p0: Call<UpdateLostFoundResponse>, p1: Throwable) {
                    Toast.makeText(this@UpdateItem, "Gagal update!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}