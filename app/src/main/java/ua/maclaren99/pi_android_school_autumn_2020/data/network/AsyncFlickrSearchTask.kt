package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.content.Intent
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import ua.maclaren99.pi_android_school_autumn_2020.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.WebViewActivity
import java.lang.ref.WeakReference


class AsyncFlickrSearchTask(textView: TextView) :
    AsyncTask<String, Unit, List<String>?>() {

    companion object {
        const val urlKey = "URL_KEY"
    }

    private var meTextView: WeakReference<TextView> = WeakReference(textView)

    override fun doInBackground(vararg params: String?): List<String>? {
        val result = params[0]?.let {
            FlickrApiEndPoint.doSearchRequest(it, FlickrApiEndPoint())
        }
        return result

    }

    override fun onPostExecute(result: List<String>?) {
        val textView = meTextView.get()
        textView?.text = result?.joinToString(separator = "\n\n")
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

}

