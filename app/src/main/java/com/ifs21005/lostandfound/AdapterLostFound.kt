package com.ifs21005.lostandfound

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ifs21005.lostandfound.models.DeleteResponse
import com.ifs21005.lostandfound.models.LostFoundXX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterLostFound (private val dataset : ArrayList<LostFoundXX>, val context: Context?, val apiService : Api, val authToken : String, val currentUserName : String) : RecyclerView.Adapter<AdapterLostFound.ViewHolder>()  {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gambar : ImageView
        val judul : TextView
        val isi : TextView
        val delete : Button
        val user : TextView
        val update : Button
        val info : Button

        init {
            delete = view.findViewById(R.id.button_delete)
            user = view.findViewById(R.id.uploader)
            update = view.findViewById(R.id.button_update)
            info = view.findViewById(R.id.tombol_detail)
            gambar = view.findViewById(R.id.gambar_barang_lostandfound)
            judul = view.findViewById(R.id.title_thing)
            isi = view.findViewById(R.id.description_of_things)



        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lost_found_item, viewGroup, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataset[(dataset.size - 1) - position].cover

        if (data != null) {
            Glide.with(viewHolder.itemView.context).load(dataset[(dataset.size - 1) - position].cover).into(viewHolder.gambar)
        }

        viewHolder.judul.text = dataset[(dataset.size - 1) - position].title
        viewHolder.isi.text = dataset[(dataset.size - 1) - position].description
        viewHolder.user.text = dataset[(dataset.size - 1) - position].author.name



        viewHolder.info.setOnClickListener{
            context?.startActivity(Intent(context, DetailItemActivity::class.java).putExtra("id", dataset[(dataset.size - 1) - position].id))
        }

        if (currentUserName == dataset[(dataset.size - 1) - position].author.name) {
            viewHolder.delete.setOnClickListener {
                val call = apiService.deleteLostFoundApi(authToken, "${dataset[(dataset.size - 1) - position].id}")


                call.enqueue(object : Callback<DeleteResponse>{
                    override fun onResponse(p0: Call<DeleteResponse>, p1: Response<DeleteResponse>) {
                        if (p1.isSuccessful) {
                            Toast.makeText(context, "Berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        }

                        else {
                            Toast.makeText(context, "Maaf. Gagal Dihapus.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(p0: Call<DeleteResponse>, p1: Throwable) {
                        Toast.makeText(context, "Gagal menghapus LostFound", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            viewHolder.update.setOnClickListener {
                context?.startActivity(Intent(context, UpdateItem::class.java)
                    .putExtra("id", dataset[(dataset.size - 1) - position].id)
                    .putExtra("title", dataset[(dataset.size - 1) - position].title)
                    .putExtra("desc", dataset[(dataset.size - 1) - position].description)
                )
            }
        } else {
            viewHolder.delete.visibility = View.GONE
            viewHolder.update.visibility = View.GONE
        }

    }

    override fun getItemCount() = dataset.size
}