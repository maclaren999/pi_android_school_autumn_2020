package ua.maclaren99.pi_android_school_autumn_2020

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_choose.*


private lateinit var listView: ListView
private lateinit var list: List<String>

class ChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        initList()
    }

    private fun initList() {
        listView = choose_list_view
        list = List(5) { n -> n.toString() }
        val mAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = mAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            returnSelectedString(list[position])
        }
    }

    private fun returnSelectedString(selected: String) {
        val data = Intent().putExtra(selectedItemKey, selected)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}