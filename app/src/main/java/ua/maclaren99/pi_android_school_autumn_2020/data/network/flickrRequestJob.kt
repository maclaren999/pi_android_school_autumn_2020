package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.content.Intent
import android.view.View
import kotlinx.coroutines.*
import ua.maclaren99.pi_android_school_autumn_2020.ui.WebViewActivity
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.urlKey
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

fun asyncFlickrSearchJob(
    requestStr: String,
//    meTextView: WeakReference<RecyclerView>
) = GlobalScope.launch(Dispatchers.Main) {
    //Requesting list of photos url
    val answerList = async(Dispatchers.IO) {
        requestStr.let {
            FlickrApiEndPoint.doSearchRequest(it, FlickrApiEndPoint())
        }
    }.await()

    displayAnswer(/*meTextView,*/ answerList)
}

private fun displayAnswer(
//    meRecyclerView: WeakReference<RecyclerView>,
    answerList: List<String>?
) {
    answerList?.let {
//        val recyclerView = meRecyclerView.get()
        MainActivity.mAdapter.addItems(*it.toTypedArray())
    }

}

fun displayWebViewActivity(linksView: View, url: String) {
    val context = linksView.context
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(urlKey, url)
    context.startActivity(intent)
}

