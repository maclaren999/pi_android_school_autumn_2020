package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.content.Intent
import android.text.method.ScrollingMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import kotlinx.coroutines.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import ua.maclaren99.pi_android_school_autumn_2020.WebViewActivity
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.urlKey
import java.lang.ref.WeakReference

fun asyncFlickrSearchJob(
    requestStr: String,
    meTextView: WeakReference<TextView>
) = GlobalScope.launch(Dispatchers.Main) {
        //Requesting list of photos url
        val answerList = async(Dispatchers.IO) {
            requestStr.let {
                FlickrApiEndPoint.doSearchRequest(it, FlickrApiEndPoint())
            }
        }.await()

    displayAnswer(meTextView, answerList)


}

private fun displayAnswer(
    meTextView: WeakReference<TextView>,
    answerList: List<String>?
) {
    val textView = meTextView.get()
    textView?.text = answerList?.joinToString(separator = "\n\n")
    textView?.movementMethod = ScrollingMovementMethod()

    BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, textView)
        .setOnLinkClickListener { linksView, url ->
            displayWebViewActivity(linksView, url)

            return@setOnLinkClickListener true
        }
}

fun displayWebViewActivity(linksView: TextView, url: String) {
    val context = linksView.context
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(urlKey, url)
    context.startActivity(intent)
}

