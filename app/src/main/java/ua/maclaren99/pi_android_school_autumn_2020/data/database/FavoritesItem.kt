package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
/*

sealed class FavoritesItem {

    class PictureListItem(picture: Picture) {

        val ownerLogin: String = picture.ownerLogin,
        val url: String = picture.url,
        val uri: String = picture.uri,
        val request: String = picture.request,
        val localID: Int? = picture.localID
    }

    class HeaderListItem(val request: String)

    fun generateAdapterList(origList: List<Picture>): List<FavoritesItem> {
        val list = List(origList.size){PictureListItem(origList[it])}
        val result = mutableListOf<FavoritesItem>()
        list.groupBy { it.request }.forEach {
            result.add(HeaderListItem(it.key) as FavoritesItem)
            result.addAll(it.value as List<FavoritesItem>)
        }
    }
}
*/
