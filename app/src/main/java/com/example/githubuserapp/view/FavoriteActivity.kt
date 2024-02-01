package com.example.githubuserapp.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.model.DataFavorit
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.model.mappingHelper.MappingHelper
import com.example.githubuserapp.viewModel.FavoriteAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var handlerThread: HandlerThread
    companion object{
        private const val EXTRA_STATE = "extra_state"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val lTitle = resources.getString(R.string.favorite)
        supportActionBar?.title = lTitle
        rv_list_favorite.layoutManager = LinearLayoutManager(this)
        rv_list_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_list_favorite.adapter = adapter

        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadDataAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<DataFavorit>(EXTRA_STATE)
            if (list != null){
                adapter.listFav = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFav)
    }
    private fun loadDataAsync(){
        GlobalScope.launch(Dispatchers.Main){
            progressBar_favorite.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArray(cursor)
            }
            progressBar_favorite.visibility = View.INVISIBLE
            val favorites = deferredFav.await()
            if (favorites.size > 0){
                adapter.listFav = favorites
            } else{
                adapter.listFav = ArrayList()
                showSnackbarMessage("Data are Empty now")
            }
        }
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_list_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadDataAsync()
    }
}