package ua.maclaren99.pi_android_school_autumn_2020.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ua.maclaren99.pi_android_school_autumn_2020.data.database.User
import ua.maclaren99.pi_android_school_autumn_2020.ui.PhotoUrlListAdapter

val TAG = "CAT"
val LOGGED_USER_KEY = "LOGGED_USER"
val UNLOGGED_USER = ""
lateinit var currentUser: User

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

//fun generateImgUrl()