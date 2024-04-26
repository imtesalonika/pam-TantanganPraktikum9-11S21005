package com.ifs21005.lostandfound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ifs21005.lostandfound.databinding.ActivityProfileBinding
import com.ifs21005.lostandfound.models.GetCurrentUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this@ProfileActivity, R.layout.activity_profile)

        getCurrentUser()

        binding.button.setOnClickListener {
            startActivity(Intent(this@ProfileActivity,EditPicture::class.java))
        }
    }

    private fun getCurrentUser () {
        val retrofit = RetrofitInstance().getRetrofit()
        val api = retrofit.create(Api::class.java)

        val sharedDataPref = this@ProfileActivity .getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )

        val myValue = sharedDataPref?.getString("auth_token", "")

        val token = "Bearer $myValue"

        val call = api.getCurrentUserApi(token)

        call.enqueue(object : Callback<GetCurrentUserResponse> {
            override fun onResponse(
                p0: Call<GetCurrentUserResponse>,
                p1: Response<GetCurrentUserResponse>
            ) {


                if (p1.body()?.data!!.user.photo !== null) {
                    Glide.with(this@ProfileActivity).load("https://public-api.delcom.org/${p1.body()?.data!!.user.photo}").into(binding.gambarProfile)
                    Log.d("tagsaya", "https://public-api.delcom.org ${p1.body()?.data!!.user.photo}")
                }
                binding.loadingAnimation.visibility = View.INVISIBLE
                binding.profilePengguna.visibility = View.VISIBLE
                binding.tempatNamaPengguna.text = p1.body()?.data!!.user.name
                binding.tempatEmailPengguna.text = p1.body()?.data!!.user.email
            }

            override fun onFailure(p0: Call<GetCurrentUserResponse>, p1: Throwable) {
                Toast.makeText(this@ProfileActivity, "Error", Toast.LENGTH_SHORT).show()
                TODO("Not yet implemented")
            }

        })
    }
}