package com.ifs21005.lostandfound

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ifs21005.lostandfound.databinding.ActivityEditPictureBinding
import com.ifs21005.lostandfound.databinding.ActivityProfileBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

class EditPicture : AppCompatActivity() {
    lateinit var binding : ActivityEditPictureBinding
    private lateinit var fotoProfil : ImageView
    private lateinit var link : Uri
    private lateinit var file : File

    private val output =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    link = fileUri

                    file = fileUri.toFile()
                    fotoProfil.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this@EditPicture, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@EditPicture, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_picture)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this@EditPicture, R.layout.activity_edit_picture)

        fotoProfil = binding.tempatEditPictureProfile

        binding.selectPicture.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(500, 500)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    output.launch(intent)
                }
        }

        binding.saveSelectPicture.setOnClickListener {
            val retrofit = RetrofitInstance().getRetrofit()
            val apiService = retrofit.create(Api::class.java)

            val sharedDataPref = this@EditPicture .getSharedPreferences(
                "my_prefs_file",
                Context.MODE_PRIVATE
            )

            val myValue = sharedDataPref?.getString("auth_token", "")

            val authToken = "Bearer $myValue"

            val gambar = MultipartBody.Part.createFormData(
                "photo",
                "profile.png",
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            val call = apiService.simpanFotoProfil(authToken, gambar)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(p0: Call<Void>, p1: Response<Void>) {
                    if (p1.isSuccessful) {
                        Toast.makeText(
                            this@EditPicture,
                            "Foto berhasil disimpan!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@EditPicture, ProfileActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditPicture,
                            "Foto gagal disimpan!",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("tagsaya", p1.body().toString())
                    }
                }

                override fun onFailure(p0: Call<Void>, p1: Throwable) {
                    Toast.makeText(
                        this@EditPicture,
                        "Foto gagal disimpan!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}