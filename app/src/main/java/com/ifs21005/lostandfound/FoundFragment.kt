package com.ifs21005.lostandfound

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifs21005.lostandfound.models.GetAllLostAndFoundsResponse
import com.ifs21005.lostandfound.models.GetCurrentUserResponse
import com.ifs21005.lostandfound.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FoundFragment : Fragment() {
    private lateinit var retrofit : Retrofit
    private lateinit var userYangLogin : User
    private lateinit var api : Api
    private lateinit var recyclerView: RecyclerView
    private lateinit var token : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.my_recikle)
        retrofit = RetrofitInstance().getRetrofit()
        api = retrofit.create(Api::class.java)

        val sharedPref = context?.getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )
        val tokenAsli = sharedPref?.getString("auth_token", "")

        token = "Bearer $tokenAsli"

        getCurrentUser()
        loadData()
    }


    fun loadData () {
//        view?.findViewById<LinearProgressIndicator>(R.id.progress_horizontal)?.visibility = View.VISIBLE
        val call = api.getLostFoundApi(token, null, null, "found")

        call.enqueue(object : Callback<GetAllLostAndFoundsResponse> {
            override fun onResponse(
                p0: Call<GetAllLostAndFoundsResponse>,
                p1: Response<GetAllLostAndFoundsResponse>
            ) {
                try {
                    val aksesDatabase = ViewModelProvider(this@FoundFragment)[DataViewModel::class.java]
                    val myAdapter = AdapterLostFound(p1.body()?.data!!.lostFounds, context, api, token, userYangLogin.name, aksesDatabase )

                    recyclerView.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerView.adapter = myAdapter

//                    view?.findViewById<LinearProgressIndicator>(R.id.progress_horizontal)?.hide()
                } catch (e : UninitializedPropertyAccessException) {
                    Toast.makeText(context, "Reload page", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(p0: Call<GetAllLostAndFoundsResponse>, p1: Throwable) {
                Toast.makeText(context, "Gagal memuat data. Refresh!", Toast.LENGTH_LONG).show()
                Log.e("my_error", p1.toString())
            }
        })
    }

    private fun getCurrentUser () {
        val call = api.getCurrentUserApi(token)

        call.enqueue(object : Callback<GetCurrentUserResponse> {
            override fun onResponse(
                p0: Call<GetCurrentUserResponse>,
                p1: Response<GetCurrentUserResponse>
            ) {
                userYangLogin =  p1.body()?.data!!.user
            }

            override fun onFailure(p0: Call<GetCurrentUserResponse>, p1: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
            }

        })
    }
}