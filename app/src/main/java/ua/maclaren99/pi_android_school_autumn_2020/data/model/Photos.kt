package ua.maclaren99.pi_android_school_autumn_2020.data.model


import ua.maclaren99.pi_android_school_autumn_2020.data.model.Photo

data class Photos(
    val page: Int,
    val pages: String,
    val perpage: Int,
    val photo: List<Photo>,
    val total: String
)