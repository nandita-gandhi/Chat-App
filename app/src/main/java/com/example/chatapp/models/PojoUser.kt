package com.example.chatapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PojoUser(
    val uid: String = "",
    val userName: String = ""
) : Parcelable