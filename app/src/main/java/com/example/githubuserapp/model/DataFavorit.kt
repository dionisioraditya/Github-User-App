package com.example.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataFavorit(
    var username: String?,
    var name: String?,
    var avatar: String?,
    var followers: Int?,
    var following:Int?,
    var company: String?,
    var location: String?,
    var repository: Int?
):Parcelable