package ua.maclaren99.pi_android_school_autumn_2020.data.model


import com.google.gson.annotations.SerializedName

data class Photo(
    val farm: Int,
    val id: String,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String
)