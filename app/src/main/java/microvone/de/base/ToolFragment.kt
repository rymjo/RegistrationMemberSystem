package microvone.de.base


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import microvone.de.admin.ClubAdminBaseFragment
import microvone.de.registrationmembersystem.R
import microvone.de.registrationmembersystem.RegistrationListFragment
import microvone.de.registrationmembersystem.RegistrationSearchFragment


/**
 * A simple [Fragment] subclass.
 */
class ToolFragment : Fragment() {

    private val TAG = ToolFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_tool, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exportButton = this.activity.findViewById(R.id.btn_export) as Button
        exportButton.setOnClickListener { v -> goToExportView(v) }

        val clubAdminButton = this.activity.findViewById(R.id.btn_club_admin) as Button
        clubAdminButton.setOnClickListener { v -> goToCubAdmintView(v) }
    }

    fun goToExportView(view: View?) {
        var fragment: Fragment? = null
        val fragmentManager2 = this.activity.supportFragmentManager
        val fragmentTransaction = fragmentManager2.beginTransaction()
        fragment = RegistrationSearchFragment()
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }

    fun goToCubAdmintView(view: View?) {
        var fragment: Fragment? = null
        val fragmentManager2 = this.activity.supportFragmentManager
        val fragmentTransaction = fragmentManager2.beginTransaction()
        fragment = ClubAdminBaseFragment()
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

}// Required empty public constructor
