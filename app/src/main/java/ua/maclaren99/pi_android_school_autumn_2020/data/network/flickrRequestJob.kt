package ua.maclaren99.pi_android_school_autumn_2020.data.network

import kotlinx.coroutines.*
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Request
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.KEY_REQUEST_TEXT
import ua.maclaren99.pi_android_school_autumn_2020.ui.WebViewActivity
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.KEY_URL
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
        appDatabase.requestDAO().insertRequest(Request(requestStr, currentUser.login))
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



