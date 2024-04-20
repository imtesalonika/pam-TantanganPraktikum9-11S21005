package com.ifs21005.lostandfound

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SavedAdapter (private val dataset : List<LostFoundEntity>, val context: Context?, val apiService : Api, val authToken : String, val aksesDatabase : DataViewModel): RecyclerView.Adapter<SavedAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val judul : TextView
        val description : TextView
        val username : TextView
        val tombolHapus : Button

        init {
            judul = view.findViewById(R.id.title_thing)
            description = view.findViewById(R.id.description_of_things)
            username = view.findViewById(R.id.uploader)
            tombolHapus = view.findViewById(R.id.tombol_hapus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_item, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataset[(dataset.size - 1) - position]

        holder.judul.text = data.title
        holder.description.text = data.description
        holder.username.text = data.username
        holder.tombolHapus.setOnClickListener {
            aksesDatabase.delete(data.id)
            Toast.makeText(context,"Berhasil Dihapus!", Toast.LENGTH_SHORT).show()
        }
    }
}