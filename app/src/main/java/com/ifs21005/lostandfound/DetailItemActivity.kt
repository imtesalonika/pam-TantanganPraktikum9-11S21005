package com.ifs21005.lostandfound

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ifs21005.lostandfound.databinding.ActivityDetailItemBinding
import com.ifs21005.lostandfound.models.LostFoundDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class DetailItemActivity : AppCompatActivity() {

    lateinit var binding : ActivityDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this@DetailItemActivity, R.layout.activity_detail_item)

        val id = intent.getIntExtra("id", 0)

        val retrofit = RetrofitInstance().getRetrofit()
        val apiService = retrofit.create(Api::class.java)

        val sharedPref = this@DetailItemActivity.getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )

        val tokenDariShared = sharedPref?.getString("auth_token", "")

        val token = "Bearer $tokenDariShared"

        val call = apiService.getDetailLostFoundApi(token, id)

        call.enqueue(object : Callback<LostFoundDetailResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                p0: Call<LostFoundDetailResponse>,
                p1: Response<LostFoundDetailResponse>
            ) {
                if (p1.isSuccessful) {

                    if (p1.body()?.data?.lost_found?.cover !== null) {
                        Glide.with(this@DetailItemActivity).load(p1.body()?.data?.lost_found?.cover).into(binding.gambarBarangLostandfound)
                    }

                    binding.title.text = p1.body()!!.data.lost_found.title
                    binding.deskripsi.text = p1.body()!!.data.lost_found.description
                    binding.statusLostorfound.text = p1.body()!!.data.lost_found.status

                    if (p1.body()!!.data.lost_found.is_completed == 1) {
                        binding.descComplete.text = "Yes"
                    } else {
                        binding.descComplete.text = "No"
                    }

                    binding.updateTerbaru.text = konversikanTanggal(p1.body()!!.data.lost_found.updated_at)
                    binding.namaUploader.text = p1.body()!!.data.lost_found.author.name

                    val sementara = "https://public-api.delcom.org/"

                    if (p1.body()?.data?.lost_found?.author?.photo !== null) {
                        Glide.with(this@DetailItemActivity).load(sementara + p1.body()!!.data.lost_found.author.photo).into(binding.gambarUploader)
                    }

                    binding.loadingAnimation.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(p0: Call<LostFoundDetailResponse>, p1: Throwable) {
                Toast.makeText(this@DetailItemActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // this method for coversion date
    fun konversikanTanggal(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

}