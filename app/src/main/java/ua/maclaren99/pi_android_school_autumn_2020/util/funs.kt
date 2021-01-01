package ua.maclaren99.pi_android_school_autumn_2020.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.User
import java.net.SocketPermission


val TAG = "CAT"
val LOGGED_USER_KEY = "LOGGED_USER"
val UNLOGGED_USER = ""
lateinit var currentUser: User
lateinit var appDatabase: AppDatabase



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



//fun generateImgUrl()