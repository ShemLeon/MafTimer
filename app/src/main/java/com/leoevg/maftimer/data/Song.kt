package com.leoevg.maftimer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    val data: String,
    val albumId: Long
): Parcelable
