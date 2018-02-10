package microvone.de.registrationmembersystem

import android.content.Context

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import microvone.de.commons.DatePickerFragment


class RegistrationSearchFragment : Fragment() {

    private val TAG = RegistrationSearchFragment::class.java.simpleName
    private var imageButtonStartDate: ImageButton? = null
    private var imageButtonStopDate: ImageButton? = null
    private var editText_startDate: EditText? = null
    private var editText_stopDate: EditText? = null
    private var btn_search: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_registration_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText_startDate = this.activity.findViewById<EditText>(R.id.editText_startdate)
        editText_stopDate = this.activity.findViewById<EditText>(R.id.editText_stopdate)

        imageButtonStartDate = this.activity.findViewById<ImageButton>(R.id.imageButton_startdate)
        imageButtonStartDate!!.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.editText = editText_startDate
            datePickerFragment.show(this.activity.fragmentManager, "datePickerStart")
        }
        imageButtonStopDate = this.activity.findViewById<ImageButton>(R.id.imageButton_stopdate)
        imageButtonStopDate!!.setOnClickListener(View.OnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.editText = editText_stopDate
            datePickerFragment.show(this.activity.fragmentManager, "datePickerStop")
        })

        btn_search = this.activity.findViewById<Button>(R.id.btn_search)
        btn_search!!.setOnClickListener { v ->
            //            val intent = Intent(v.context, RegistrationListActivity::class.java)
//            intent.putExtra(DatePickerKeys.START_DATE, editText_startDate!!.text.toString())
//            intent.putExtra(DatePickerKeys.STOP_DATE, editText_stopDate!!.getText().toString())
//            startActivity(intent)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }


}