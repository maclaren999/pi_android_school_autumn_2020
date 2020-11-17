package ua.maclaren99.pi_android_school_autumn_2020.data.network

import android.os.AsyncTask
import android.widget.TextView
import ua.maclaren99.pi_android_school_autumn_2020.data.execFlickrCall
import java.lang.ref.WeakReference

/*
class SimpleAsyncTask internal constructor(tv: TextView) :
    AsyncTask<Void?, Void?, String>() {
    // The TextView where we will show results
    private val mTextView: WeakReference<TextView>

    *//**
     * Runs on the background thread.
     *
     * @param voids No parameters in this use case.
     * @return Returns the string including the amount of time that
     * the background thread slept.
     *//*
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

    *//**
     * Does something with the result on the UI thread; in this case
     * updates the TextView.
     *//*
    override fun onPostExecute(result: String) {
        mTextView.get()!!.text = result
    }

    // Constructor that provides a reference to the TextView from the MainActivity
    init {
        mTextView = WeakReference(tv)
    }
}
*/

class AsyncFlickrSearchTask(textView: TextView): AsyncTask<String, Unit, String>() {

    private var meTextView: WeakReference<TextView> = WeakReference(textView)

    /**
     * Runs on the background thread.
     *
     * @param voids No parameters in this use case.
     * @return Returns the string including the amount of time that
     * the background thread slept.
     */
    override fun doInBackground(vararg params: String?): String {
        val result = params[0]?.let { execFlickrCall(it) }
        return result.toString()
    }


    /**
     * Does something with the result on the UI thread; in this case
     * updates the TextView.
     */
    override fun onPostExecute(result: String?) {
        meTextView.get()?.text = result
    }

}