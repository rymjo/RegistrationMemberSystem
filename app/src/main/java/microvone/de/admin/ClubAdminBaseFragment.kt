package microvone.de.admin


import android.app.ProgressDialog
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import microvone.de.base.CategoryListFragment
import microvone.de.database.DbHelper
import microvone.de.registrationmembersystem.R


/**
 * A simple [Fragment] subclass.
 */
class ClubAdminBaseFragment : Fragment() {

    private val TAG = ClubAdminBaseFragment::class.java!!.getSimpleName()
    private var loadInProgressDialog: ProgressDialog? = null
    private var db: SQLiteDatabase? = null

    private val LOADER_ID = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_club_admin_base, container, false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        loaderManager.destroyLoader(LOADER_ID)
        if (db != null) {
            db?.close()
        }
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStop() {
        if (db != null) {
            db?.close()
        }
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    private fun db(): SQLiteDatabase? {
        val dbm = DbHelper.getInstance(this.context)
        try {
            db = dbm.getReadableDatabase()
        } catch (e: SQLiteException) {
            Log.e(TAG, "SQL: " + e)
        }

        return db
    }

}
