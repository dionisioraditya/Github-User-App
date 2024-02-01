package com.example.githubuserapp.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapp.CustomOnItemClickListener
import com.example.githubuserapp.R
import com.example.githubuserapp.model.DataFavorit
import com.example.githubuserapp.view.DetailUserActivity

class FavoriteAdapter(private val activity:Activity) :RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    var listFav = ArrayList<DataFavorit>()
    set (listFav){
        if (listFav.size > 0){
            this.listFav.clear()
        }
        this.listFav.addAll(listFav)
        notifyDataSetChanged()
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
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
            itemView.setOnClickListener (
                CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, DetailUserActivity:: class.java)
                        intent.putExtra(DetailUserActivity.EXTRA_FAVORITE, fav)
                        intent.putExtra(DetailUserActivity.EXTRA_POSITION, position)
                        activity.startActivity(intent)
                    }
                })
            )
        }
    }
}