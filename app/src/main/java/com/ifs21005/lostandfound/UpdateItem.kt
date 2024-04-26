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
import com.ifs21005.lostandfound.models.UpdateData
import com.ifs21005.lostandfound.databinding.ActivityUpdateItemBinding
import com.ifs21005.lostandfound.models.UpdateLostFoundResponse
import com.ifs21005.lostandfound.models.UploadCoverResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateItem : AppCompatActivity() {

    lateinit var binding : ActivityUpdateItemBinding
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
                    Toast.makeText(this@UpdateItem, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@UpdateItem, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

        fotoProfil = binding.tempatFotoUpdate

        binding.buttonSelectUpdate.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(500, 500)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    output.launch(intent)
                }
        }

        val id = intent.getIntExtra("id", 0)


        binding.button3.setOnClickListener {
            val retrofit = RetrofitInstance().getRetrofit()
            val api = retrofit.create(Api::class.java)

            val sharedDataPref = this@UpdateItem .getSharedPreferences(
                "my_prefs_file",
                Context.MODE_PRIVATE
            )

            val myValue = sharedDataPref?.getString("auth_token", "")

            val authToken = "Bearer $myValue"



            val coverPart = MultipartBody.Part.createFormData("cover", "foto.jpg", file.asRequestBody("image/*".toMediaTypeOrNull()))

            val call2 = api.uploadCoverApi(authToken, coverPart, id.toString())

            call2.enqueue(object : Callback<UploadCoverResponse> {
                override fun onResponse(
                    p0: Call<UploadCoverResponse>,
                    p1: Response<UploadCoverResponse>
                ) {
                    if (p1.isSuccessful) {
                        Toast.makeText(this@UpdateItem, "Berhasil menambahkan!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateItem, MainActivity::class.java))
                        finish()
                    }

                    else {
                        Toast.makeText(this@UpdateItem, "Gagal menambahkan gambar!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(p0: Call<UploadCoverResponse>, p1: Throwable) {
                    Toast.makeText(this@UpdateItem, "Error", Toast.LENGTH_SHORT).show()
                }

            })

        }


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