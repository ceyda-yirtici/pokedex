package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecyclerItem(val name: String, val tag: String, val content: String) : Parcelable