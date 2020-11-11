package ua.maclaren99.pi_android_school_autumn_2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import kotlinx.android.synthetic.main.activity_main.*

val selectedItemKey = "selected"
private val chooseRequestCode = 17
private var selectedText: String = ""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiveIntent()
        initListeners()
    }

    private fun receiveIntent() {
        text1.text = intent.getStringExtra(Intent.EXTRA_TEXT)
        text1.visibility = View.VISIBLE

//        val dataStr = intent.dataString
//        val data = intent.data
//        val action = intent.action
//        val type = intent.type
//        val extras = intent.extras
//        val clipData = intent.clipData

//        Log.d("TAG", extras.toString())
//        Log.d("TAG", "$data \n$action \n$type")
//        Toast.makeText(this, "$data \n$action \n$type", Toast.LENGTH_LONG).show()
    }

    private fun initListeners() {
        choose_button.setOnClickListener {
            val intent = Intent(this, ChooseActivity::class.java)
            startActivityForResult(intent, chooseRequestCode)
        }

        share_button.setOnClickListener {
            startShareIntentBuilder()
        }
    }

    private fun startShareIntentBuilder() {
        if (!selectedText.isNullOrBlank()) {
            val typeMime = "text/plain"
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(typeMime)
                    .setChooserTitle(getString(R.string.share_text_dialog))
                    .setText(selectedText)
                    .startChooser()
        } else {
            Toast.makeText(this, "Firstly make your choice!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            chooseRequestCode -> {
                selectedText = data?.getStringExtra(selectedItemKey) ?: ""
                text3.text = selectedText
            }
        }
    }
}