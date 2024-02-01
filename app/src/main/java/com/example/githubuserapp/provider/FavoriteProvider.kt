package com.example.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserapp.model.db.DatabaseContract.AUTHORITY
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.model.db.UserHelper

class FavoriteProvider : ContentProvider() {
    companion object{
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var favoriteHelper: UserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            sUriMatcher.addURI(AUTHORITY, UserColumns.TABLE_NAME, FAV)
            sUriMatcher.addURI(
                AUTHORITY,
                "UserColumns.TABLE_NAME/#",
                FAV_ID
            )
        }
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted:Int = when (FAV_ID){
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added:Long = when(FAV){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        favoriteHelper = UserHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)){
            FAV -> favoriteHelper.queryAll()
            FAV_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated:Int = when(FAV_ID){
            sUriMatcher.match(uri) -> favoriteHelper.update(
                uri.lastPathSegment.toString(), values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}