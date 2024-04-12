package com.ifs21005.lostandfound

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.ifs21005.lostandfound.models.AddLostFoundRequest
import com.ifs21005.lostandfound.models.AddLostFoundResponse
import com.ifs21005.lostandfound.models.UploadCoverResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

class AddFragment : Fragment() {

    private lateinit var fotoProfil : ImageView
    private lateinit var link : Uri
    private lateinit var retrofit : Retrofit
    private lateinit var api : Api
    private lateinit var token : String
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
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tambahFoto = view.findViewById<Button>(R.id.tombol_tambah_foto)
        val tambahLostFound = view.findViewById<Button>(R.id.button_add_lostfound)
        val judul = view.findViewById<TextInputEditText>(R.id.input_title_lostandfound)
        val deskripsi = view.findViewById<TextInputEditText>(R.id.input_description_lostandfound)
        val status = view.findViewById<AutoCompleteTextView>(R.id.selection)
        val textjudul = judul.text
        val textdeskripsi = deskripsi.text
        val sharedPref = context?.getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )
        val myValue = sharedPref?.getString("auth_token", "")
        token = "Bearer $myValue"
        retrofit = RetrofitInstance().getRetrofit()
        api = retrofit.create(Api::class.java)
        fotoProfil = view.findViewById(R.id.tempat_foto_lostandfound)

        tambahFoto.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    output.launch(intent)
                }
        }

        tambahLostFound.setOnClickListener {
            addLostFound(textjudul.toString(), textdeskripsi.toString(), status)
        }
    }

    private fun addLostFound (title : String, desc : String, status : AutoCompleteTextView) {
        val data = AddLostFoundRequest(title, desc, status.text.toString().lowercase())
        var id : Int

        val call = api.createLostFoundApi(token, data)
        call.enqueue(object : Callback<AddLostFoundResponse>{
            override fun onResponse(
                p0: Call<AddLostFoundResponse>,
                p1: Response<AddLostFoundResponse>
            ) {
                if (p1.isSuccessful) {
                    id = p1.body()?.data!!.lost_found_id

                    val coverPart = MultipartBody.Part.createFormData("cover", title, file.asRequestBody("image/*".toMediaTypeOrNull()))

                    val call2 = api.uploadCoverApi(token, coverPart, id.toString())

                    call2.enqueue(object : Callback<UploadCoverResponse> {
                        override fun onResponse(
                            p0: Call<UploadCoverResponse>,
                            p1: Response<UploadCoverResponse>
                        ) {
                            if (p1.isSuccessful) {
                                Toast.makeText(context, "Berhasil menambahkan!", Toast.LENGTH_SHORT).show()
                            }

                            else {
                                Toast.makeText(context, "Gagal menambahkan gambar!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(p0: Call<UploadCoverResponse>, p1: Throwable) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        }

                    })

                }
            }

            override fun onFailure(p0: Call<AddLostFoundResponse>, p1: Throwable) {
                Toast.makeText(context, "Gagal menambahkan!", Toast.LENGTH_SHORT).show()
            }

        })
    }

}