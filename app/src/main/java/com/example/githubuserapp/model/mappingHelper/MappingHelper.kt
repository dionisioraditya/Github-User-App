package com.example.githubuserapp.model.mappingHelper

import android.database.Cursor
import com.example.githubuserapp.model.DataFavorit
import com.example.githubuserapp.model.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArray(favoriteCursor: Cursor?):ArrayList<DataFavorit>{
        val favList = ArrayList<DataFavorit>()
        favoriteCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_LOCATION))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_FOLLOWING))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_REPOSITORY))
                favList.add(
                    DataFavorit(
                        username,
                        name,
                        avatar,
                        followers,
                        following,
                        company,
                        location,
                        repository
                    )
                )
            }
        }
        return favList
    }
}