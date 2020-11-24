package ua.maclaren99.pi_android_school_autumn_2020.data.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ua.maclaren99.pi_android_school_autumn_2020.data.model.FlickrRequestData

interface FlickrApiEndPoint {

    @GET("services/rest/")
    fun getPhotosSearch(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") api_key: String = API_KEY,
        @Query("text") text: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: String = "1"
    ): Call<FlickrRequestData>


    companion object {

        const val urlKey = "URL_KEY"

        private var mInstant: FlickrApiEndPoint? = null

        operator fun invoke(): FlickrApiEndPoint? {

            return if (mInstant != null){
                mInstant
            } else {
                val BASE_URL = "https://www.flickr.com/"

                mInstant = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory
                        .create())
                    .build()
                    .create(FlickrApiEndPoint::class.java)
                mInstant
            }
        }


        fun doSearchRequest(text: String, instant: FlickrApiEndPoint?): List<String>? {
            if (instant == null) throw UninitializedPropertyAccessException(message =
            "FlickrApiEndPoint instant must be initialized by calling {@code FlickrApiEndPoint.invoke() }")

            val size = "c"
            val callFlickr = instant.getPhotosSearch(text = text)
            val response = callFlickr.execute().body()
            val urlList = response?.photos?.photo?.map {photo ->
                 "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_${size}.jpg"
            } ?: listOf<String>("No such photos :(")

            return urlList
        }
    }
}