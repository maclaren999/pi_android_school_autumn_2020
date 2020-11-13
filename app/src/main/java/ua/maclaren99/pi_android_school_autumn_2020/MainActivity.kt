package ua.maclaren99.pi_android_school_autumn_2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import kotlinx.android.synthetic.main.activity_main.*

internal const val selectedItemKey = "selected"
internal const val textPlainMimeType = "text/plain"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent
        receiveIntent()
        initListeners()
    }

    companion object {
        private const val chooseRequestCode = 17
        private var selectedText: String = ""
    }

    private fun receiveIntent() {
        text1.text = intent.getStringExtra(Intent.EXTRA_TEXT)
        text1.visibility = View.VISIBLE
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
        if (!selectedText.isBlank()) {
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(textPlainMimeType)
                    .setChooserTitle(getString(R.string.share_text_dialog))
                    .setText(selectedText)
                    .startChooser()
        } else {
            Toast.makeText(this, getString(R.string.make_your_choice_toast), Toast.LENGTH_SHORT).show()
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