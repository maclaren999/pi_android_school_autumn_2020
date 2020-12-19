package ua.maclaren99.pi_android_school_autumn_2020.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.User
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.LOGGED_USER_KEY
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            val loginStr = login_input.text.toString()
            if (loginStr.isNotBlank()) GlobalScope.launch(Dispatchers.Default) { login(loginStr) }
        }
    }

    private suspend fun login(loginStr: String) {
        //Saving current user to ShearedPref, variable and db
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        with(sharedPref.edit()) {
            putString(LOGGED_USER_KEY, loginStr)
            apply()
        }
        currentUser = User(loginStr)
        AppDatabase.getDatabase(this).pictureDAO()
            .insertUser(currentUser)

        withContext(Dispatchers.Main) {

            startActivity(
                Intent(baseContext, MainActivity::class.java)
            )
            finish()
        }
    }


}