package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ua.maclaren99.pi_android_school_autumn_2020.data.TAG
import ua.maclaren99.pi_android_school_autumn_2020.data.model.FlickrRequestData

interface FlickrApiEndPoint {
    // Request method and URL specified in the annotation
    @GET("services/rest/")
    fun getPhotosSearch(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") api_key: String = "d076ff22ea8872ee9b49c0035dd95d4f",
        @Query("text") text: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: String = "1"
    ): Call<FlickrRequestData>


    /*companion object {

        lateinit var retrofit: FlickrApiEndPoint

        operator fun invoke(): FlickrApiEndPoint {

            val BASE_URL = "https://www.flickr.com/"

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FlickrApiEndPoint::class.java)
        }


        fun executeRes(text: String): String{
            //Test
            val callFlickr = retrofit.getPhotosSearch(text = text)
            val responce = callFlickr.execute().body()

            Log.d(TAG, responce.toString())
            return responce.toString()

        }
    }*/
}
