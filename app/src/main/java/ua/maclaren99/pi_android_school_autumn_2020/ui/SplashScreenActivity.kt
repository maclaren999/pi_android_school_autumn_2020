package ua.maclaren99.pi_android_school_autumn_2020.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import ua.maclaren99.pi_android_school_autumn_2020.data.database.User
import ua.maclaren99.pi_android_school_autumn_2020.util.LOGGED_USER_KEY
import ua.maclaren99.pi_android_school_autumn_2020.util.UNLOGGED_USER
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

        //TODO("Add sign out")
        val loggedUser: String = sharedPreferences.getString(LOGGED_USER_KEY, UNLOGGED_USER) ?: ""
        if (loggedUser != UNLOGGED_USER) {
            currentUser = User(loggedUser)
            startActivity(
                Intent(baseContext, MainActivity::class.java)
            )
            finish()
        } else {
            startActivity(
                Intent(baseContext, LoginActivity::class.java)
            )
            finish()
        }
    }
}