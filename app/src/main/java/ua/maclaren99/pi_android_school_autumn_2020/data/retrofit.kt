package ua.maclaren99.pi_android_school_autumn_2020.data

import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ua.maclaren99.pi_android_school_autumn_2020.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.data.model.FlickrRequestData
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint

//https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=d076ff22ea8872ee9b49c0035dd95d4f&text=dog&format=json&nojsoncallback=1
const val TAG = "TEST"
lateinit var retrofit: FlickrApiEndPoint

fun initRetrofit(){
    val BASE_URL = "https://www.flickr.com/"
    retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FlickrApiEndPoint::class.java)

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()
}

fun execFlickrCall(text: String): String{
    //Test
    val callFlickr = retrofit.getPhotosSearch(text = text)
    val responce = callFlickr.execute().body()

    Log.d(TAG, responce.toString())
    return responce.toString()

}
