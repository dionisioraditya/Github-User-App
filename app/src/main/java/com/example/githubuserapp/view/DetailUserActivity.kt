package com.example.githubuserapp.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapp.model.DataUser
import com.example.githubuserapp.R
import com.example.githubuserapp.model.DataFavorit
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_COMPANY
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_FOLLOWERS
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_FOLLOWING
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOCATION
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_NAME
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_REPOSITORY
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.model.db.UserHelper
import com.example.githubuserapp.viewModel.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {
    private lateinit var userHelper: UserHelper
    private var statusFavorite = false
    private var favorites:DataFavorit? = null
    companion object {
        const val EXTRA_PERSON = "extra_person"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        private val TAG = DetailUserActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        favorites = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (favorites != null){
            statusFavorite = true
            setStatusFav(statusFavorite)
            setDataSql()
        } else{
            statusFavorite = false
            setStatusFav(statusFavorite)
            setDetailUser()
        }

    }
    private fun setDetailUser(){
        // initialization Component
        val tvUsername: TextView = findViewById(R.id.tv_username_detail)
        val ivImage: ImageView = findViewById(R.id.iv_photo_detail_user)

        // init variable intent parcelable
        val person = intent.getParcelableExtra(EXTRA_PERSON) as DataUser?
        Log.d(TAG, "check person detail: $person")
        val txtUsername = person?.username
        val txtName = person?.name
        val txtAvatar = person?.avatar
        val txtFollowers = person?.followers
        val txtFollowing = person?.following
        val txtLocation = person?.location
        val txtCompany = person?.company
        val txtRepository = person?.repository
        // Output Textview & ImageView
        Glide.with(this)
            .load(txtAvatar)
            .apply(RequestOptions().override(150,150))
            .into(ivImage)
        tvUsername.text = txtUsername
        setDesc(txtName,txtLocation,txtCompany,txtRepository,txtFollowers,txtFollowing)

        val mUsername = person?.username
        setViewPagerAdapter(mUsername)
        val title = "$txtUsername"
        setActionBarTitle(title)

        //
        //
        // event listener fab favorite button
        //
        //
        fab_favorite.setOnClickListener {
            if (statusFavorite){
                userHelper.deleteById(txtUsername.toString())
                val lRemoved = resources.getString(R.string.remove_user)
                val lRemoved2 = resources.getString(R.string.from_favorite)
                Toast.makeText(this, "$lRemoved $txtUsername $lRemoved2", Toast.LENGTH_SHORT).show()
                statusFavorite = false
                setStatusFav(statusFavorite)
            }
            else{
                statusFavorite = true
                insertDatabase(txtUsername, txtName, txtAvatar, txtCompany,txtLocation,txtFollowers, txtFollowing, txtRepository)
                val lAdd = resources.getString(R.string.add_user)
                val lAdd2 = resources.getString(R.string.to_favorite)
                Toast.makeText(this, "$lAdd $txtUsername $lAdd2", Toast.LENGTH_SHORT).show()
                setStatusFav(statusFavorite)
            }

        }
    }
    private fun setDataSql(){
        val favUser = intent.getParcelableExtra<DataFavorit>(EXTRA_FAVORITE) as DataFavorit
        //val favUser = intent.getParcelableExtra(EXTRA_FAVORITE) as DataFavorit
        val title = "${favUser.username}"
        setActionBarTitle(title)
        Glide.with(this)
            .load(favUser.avatar)
            .apply(RequestOptions().override(150, 150))
            .into(iv_photo_detail_user)
        tv_username_detail.text = favUser.username
        val txtName = favUser.name
        val txtUsername = favUser.username
        val txtAvatar = favUser.avatar
        val txtFollowers = favUser.followers
        val txtFollowing = favUser.following
        val txtLocation = favUser.location
        val txtCompany = favUser.company
        val txtRepository = favUser.repository
        setDesc(txtName,txtLocation,txtCompany,txtRepository, txtFollowers, txtFollowing)
        val mUsername = favUser.username
        setViewPagerAdapter(mUsername)
        fab_favorite.setOnClickListener {
            if (statusFavorite){
                userHelper.deleteById(txtUsername.toString())
                val lRemoved = resources.getString(R.string.remove_user)
                val lRemoved2 = resources.getString(R.string.from_favorite)
                Toast.makeText(this, "$lRemoved $txtUsername $lRemoved2", Toast.LENGTH_SHORT).show()
                statusFavorite = false
                setStatusFav(statusFavorite)
            }
            else{
                statusFavorite = true
                insertDatabase(txtUsername, txtName, txtAvatar, txtCompany,txtLocation,txtFollowers, txtFollowing, txtRepository)
                val lAdd = resources.getString(R.string.add_user)
                val lAdd2 = resources.getString(R.string.to_favorite)
                Toast.makeText(this, "$lAdd $txtUsername $lAdd2", Toast.LENGTH_SHORT).show()
                setStatusFav(statusFavorite)
            }
        }
    }

    private fun setStatusFav(statusFavorit: Boolean){
        if (statusFavorit){
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_red_24)
        }else fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_white_24)
    }
    private fun insertDatabase(username: String?, name:String?, avatar: String?, company: String?, location: String?, followers: Int?, following: Int?, repository: Int?){
        val values = ContentValues()
        values.put(COLUMN_NAME_USERNAME, username)
        values.put(COLUMN_NAME_NAME, name)
        values.put(COLUMN_NAME_AVATAR_URL, avatar)
        values.put(COLUMN_NAME_COMPANY, company)
        values.put(COLUMN_NAME_LOCATION, location)
        values.put(COLUMN_NAME_FOLLOWERS, followers)
        values.put(COLUMN_NAME_FOLLOWING, following)
        values.put(COLUMN_NAME_REPOSITORY, repository)
        contentResolver.insert(CONTENT_URI,values)
    }
    private fun setActionBarTitle(str: String){
        if (supportActionBar!= null){
            (supportActionBar as ActionBar).title = str
        }
    }
    private fun setViewPagerAdapter(username: String?){
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
    private fun setDesc(name: String?, location: String?, company: String?, repository: Int?, followers: Int?,following: Int?){
        val tvdescDetailUser:TextView = findViewById(R.id.desc_user_detail)
        val lFollowers = resources.getString(R.string.followers)
        val lFollowing = resources.getString(R.string.following)
        val lCompany = resources.getString(R.string.company)
        val lRepository = resources.getString(R.string.repository)
        val lLocation = resources.getString(R.string.location)
        val lName = resources.getString(R.string.name)
        val textDesc = "$lName: $name \n$lLocation: $location \n$lCompany: $company \n$lRepository: $repository \n$lFollowers: $followers \n$lFollowing: $following"
        tvdescDetailUser.text = textDesc
    }
}