package microvone.de.registrationmembersystem

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import microvone.de.commons.DatePickerFragment
import microvone.de.commons.DatePickerKeys
import microvone.de.registrationmembersystem.R.*

class RegistrationSearchActivity : AppCompatActivity() {

    private val TAG = RegistrationSearchActivity::class.java.simpleName

    private var imageButtonStartDate: ImageButton? = null
    private var imageButtonStopDate:ImageButton? = null
    private var editText_startDate: EditText? = null
    private var editText_stopDate:EditText? = null
    private var btn_search: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "RegistrationSearchActivity")
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_registration_search)

        editText_startDate = findViewById<EditText>(id.editText_startdate)
        editText_stopDate = findViewById<EditText>(id.editText_stopdate)

        imageButtonStartDate = findViewById<ImageButton>(id.imageButton_startdate)
        imageButtonStartDate!!.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.editText = editText_startDate
            datePickerFragment.show(fragmentManager, "datePickerStart")
        }
        imageButtonStopDate = findViewById<ImageButton>(id.imageButton_stopdate)
        imageButtonStopDate!!.setOnClickListener(View.OnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.editText = editText_stopDate
            datePickerFragment.show(fragmentManager, "datePickerStop")
        })

        btn_search = findViewById<Button>(id.btn_search)
        btn_search!!.setOnClickListener { v ->
//            val intent = Intent(v.context, RegistrationListActivity::class.java)
//            intent.putExtra(DatePickerKeys.START_DATE, editText_startDate!!.text.toString())
//            intent.putExtra(DatePickerKeys.STOP_DATE, editText_stopDate!!.getText().toString())
//            startActivity(intent)
        }
    }
}
