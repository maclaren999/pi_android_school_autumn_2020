package ua.maclaren99.pi_android_school_autumn_2020.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.data.database.User
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.WebViewActivity


val TAG = "CAT"
val LOGGED_USER_KEY = "LOGGED_USER"
val UNLOGGED_USER = ""
lateinit var currentUser: User
lateinit var appDatabase: AppDatabase

fun displayWebViewActivity(linksView: View, url: String) {
    val context = linksView.context
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(FlickrApiEndPoint.KEY_URL, url)
        .putExtra(FlickrApiEndPoint.KEY_REQUEST_TEXT, MainActivity.requestStr)
        .putExtra(FlickrApiEndPoint.KEY_LOCAL_RESOURCE_MODE, false)
    context.startActivity(intent)
}

fun displayWebViewActivity(context: Context, picture: Picture) {
    val intent = Intent(context, WebViewActivity::class.java)
        .putExtra(FlickrApiEndPoint.KEY_URL, picture.uri)
        .putExtra(FlickrApiEndPoint.KEY_REQUEST_TEXT, picture.request)
        .putExtra(FlickrApiEndPoint.KEY_LOCAL_RESOURCE_MODE, true)
    context.startActivity(intent)
}

fun Activity.hideKeyboard() {

    val view: View? = this.currentFocus

    if (view != null) {

        val manager: InputMethodManager? = getSystemService(
            AppCompatActivity.INPUT_METHOD_SERVICE
        ) as InputMethodManager?
        manager?.hideSoftInputFromWindow(
            view.windowToken, 0
        )
    }
}

inline fun Activity.runWithPermission(permissionName: String, requestCode: Int, ifGranted: () -> Unit): Boolean {
    if (ActivityCompat.checkSelfPermission(
            this,
            permissionName
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this, arrayOf(permissionName),
            requestCode
        )
        return false
    } else {
        Log.d(TAG, "$permissionName: permissions granted")
        ifGranted()
        return true
    }
}