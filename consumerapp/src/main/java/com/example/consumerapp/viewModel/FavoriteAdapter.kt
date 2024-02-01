package com.example.consumerapp.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.model.DataFavorit

class FavoriteAdapter(private val activity:Activity) :RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    var listFav = ArrayList<DataFavorit>()
    set (listFav){
        if (listFav.size > 0){
            this.listFav.clear()
        }
        this.listFav.addAll(listFav)
        notifyDataSetChanged()
    }
    fun addItem (fav: DataFavorit){
        this.listFav.add(fav)
        notifyItemInserted(this.listFav.size - 1)
    }
    fun updateItem (position: Int, fav: DataFavorit){
        this.listFav[position] = fav
        notifyItemChanged(position, fav)
    }
    fun removeItem (position: Int){
        this.listFav.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFav.size)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFav.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFav[position])
    }
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvtUsername: TextView = itemView.findViewById(R.id.txt_username)
        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvFollowers: TextView = itemView.findViewById(R.id.tv_followers)
        private val tvFollowing: TextView = itemView.findViewById(R.id.tv_following)
        private val lFollowers = itemView.context.resources.getString(R.string.followers)
        private val lFollowing = itemView.context.resources.getString(R.string.following)
        @SuppressLint("SetTextI18n")
        fun bind(fav: DataFavorit){
            tvtUsername.text = fav.username
            tvName.text = fav.name
            tvFollowers.text = "$lFollowers: ${fav.followers}"
            tvFollowing.text = "$lFollowing: ${fav.following}"
            Glide.with(itemView.context)
                .load(fav.avatar)
                .apply(RequestOptions().override(110, 110))
                .into(imgPhoto)

        }
    }
}