package com.ifs21005.lostandfound

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DitandaiFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ditandai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aksesDatabase = ViewModelProvider(this@DitandaiFragment)[DataViewModel::class.java]
        val apiService = RetrofitInstance().getRetrofit().create(Api::class.java)
        val authToken = "Bearer " + context?.getSharedPreferences(
            "my_prefs_file",
            Context.MODE_PRIVATE
        )?.getString("auth_token","")
        val myRecyclerView=view.findViewById<RecyclerView>(R.id.savedRecyclerView)
        myRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        aksesDatabase.getAllSaved.observe(viewLifecycleOwner){
            data ->
            val savedLostFoundAdapter = SavedAdapter(
                data,
                context,
                apiService,
                authToken,
                aksesDatabase
            )

            myRecyclerView.adapter = savedLostFoundAdapter


        }
    }

}