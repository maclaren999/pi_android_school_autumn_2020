package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import kotlinx.coroutines.*
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Request
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.requestStrKey
import ua.maclaren99.pi_android_school_autumn_2020.ui.WebViewActivity
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.urlKey
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

fun asyncFlickrSearchJob(
    requestStr: String,
) = GlobalScope.launch(Dispatchers.Main) {
    //Requesting list of photos url
    val answerList = async(Dispatchers.IO) {
        requestStr.let {
            FlickrApiEndPoint.doSearchRequest(it, FlickrApiEndPoint())
        }
    }.await()
    displayAnswer(answerList)

    //Saving
    launch(Dispatchers.IO) {
        appDatabase.pictureDAO().insertRequest(Request(requestStr, currentUser.login))
    }
}

private fun displayAnswer(
    answerList: List<String>?
) {
    answerList?.let {
        MainActivity.mAdapter.removeAll()
        MainActivity.mAdapter.addItems(*it.toTypedArray())
    }

}

fun displayWebViewActivity(linksView: View, url: String) {
    val context = linksView.context
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(urlKey, url)
        .putExtra(requestStrKey, MainActivity.requestStr)
    context.startActivity(intent)
}

fun displayWebViewActivity(context: Context, picture: Picture) {
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(urlKey, picture.url)
        .putExtra(requestStrKey, picture.request)
    context.startActivity(intent)
}

