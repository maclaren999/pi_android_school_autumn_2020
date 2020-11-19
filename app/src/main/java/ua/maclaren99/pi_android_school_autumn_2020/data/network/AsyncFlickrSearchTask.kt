package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.content.Intent
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import ua.maclaren99.pi_android_school_autumn_2020.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.WebViewActivity
import java.lang.ref.WeakReference


class AsyncFlickrSearchTask(textView: TextView, val activity: MainActivity) :
    AsyncTask<String, Unit, List<String>?>() {

    private var meTextView: WeakReference<TextView> = WeakReference(textView)

    override fun doInBackground(vararg params: String?): List<String>? {
        val result = params[0]?.let {
            FlickrApiEndPoint.doSearchRequest(it, FlickrApiEndPoint())
        }
        return result
    }

    override fun onPostExecute(result: List<String>?) {
        val textView = meTextView.get()
        val mainContext = textView?.context?.applicationContext
        textView?.text = result?.joinToString(separator = "\n\n")
        textView?.movementMethod = ScrollingMovementMethod()

        BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, textView)
            .setOnLinkClickListener { linksView, url ->
                displayWebViewActivity(linksView, url)

                return@setOnLinkClickListener true
            }

    }

    fun displayWebViewLayout(linksView: TextView, url: String) {
        val webView = activity.web_view
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(true)
        webView.loadUrl(url)
        activity.web_view_layout.visibility = View.VISIBLE
    }

    fun displayWebViewActivity(linksView: TextView, url: String) {
        val intent = Intent(activity.applicationContext, WebViewActivity::class.java)
            .putExtra(MainActivity.urlKey, url)
        activity.startActivity(intent)
    }


}

/*
class SimpleAsyncTask internal constructor(tv: TextView) :
    AsyncTask<Void?, Void?, String>() {
    // The TextView where we will show results
    private val mTextView: WeakReference<TextView>

    override fun doInBackground(vararg voids: Void?): String {

        // Generate a random number between 0 and 10.
        val r = Random()
        val n: Int = r.nextInt(11)

        // Make the task take long enough that we have
        // time to rotate the phone while it is running.
        val s = n * 200

        // Sleep for the random amount of time.
        try {
            Thread.sleep(s.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Return a String result.
        return "Awake at last after sleeping for $s milliseconds!"
    }

    override fun onPostExecute(result: String) {
        mTextView.get()!!.text = result
    }

    // Constructor that provides a reference to the TextView from the MainActivity
    init {
        mTextView = WeakReference(tv)
    }
}
*/

