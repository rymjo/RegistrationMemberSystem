package microvone.de.admin


import android.app.ProgressDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter

import microvone.de.database.ClubColumns
import microvone.de.database.DbHelper
import microvone.de.database.SqliteLoader
import microvone.de.registrationmembersystem.R


/**
 * A simple [Fragment] subclass.
 */
class ClubAdminBaseFragment :ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val TAG = ClubAdminBaseFragment::class.java!!.getSimpleName()
    private var loadInProgressDialog: ProgressDialog? = null
    private var db: SQLiteDatabase? = null

    private val LOADER_ID = 1
    private var mClubAdapter: SimpleCursorAdapter? = null
    private var mLoaderCallbacks: LoaderManager.LoaderCallbacks<Cursor>? = null

    /**
     * Spalten des Cursors der Kategorien, die
     * in der Liste angezeigt werden.
     */
    private val ANZEIGE_CLUB = ClubColumns.COLUMNS

    /** IDs im SimpleListView Layout.  */
    private val SIMPLE_LIST_VIEW_IDS = intArrayOf(android.R.id.text1, android.R.id.text2)

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        val view = inflater!!.inflate(R.layout.fragment_club_admin_base,container, false)
        return view
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
        activity.title = "Mein Verein"

        Log.i(TAG, "onCreate(): entered...")

        // Hier bereitet man den CursorAdapter vor. Er wird aber noch nicht
        // an einen Cursor gebunden. Dies geschieht erst, nachdem der Cursor
        // nach einer Datenanfrage zurückgegeben wurde (onLoadFinished).
        // Der letzte Parameter ist ebenfalls 0. Das vermeidet, dass der
        // Adapter einen eigenen ContentObserver für den Cursor registriert.
        // Diese Aufgabe übernimmt der CursorLoader.
        mClubAdapter = SimpleCursorAdapter(this.context, android.R.layout.simple_list_item_2, null, ANZEIGE_CLUB, SIMPLE_LIST_VIEW_IDS, 0)
        listAdapter = mClubAdapter

        // Referenz auf das Objekt, das sich um die Callbacks nach einer
        // Datenanfrage kümmert. Ist i.A. die Activity oder das aufrufende
        // Fragment.
        mLoaderCallbacks = this


        // Registriert einen Loader mit ID LOADER_ID_KONTAKTLISTE beim LoaderManager.
        // Ab hier übernimmt der Manager die Kontrolle über den Lebenszyklus des Loaders.
        val lm = loaderManager
        lm.initLoader(LOADER_ID, null, mLoaderCallbacks)

        var floating_add_club_action_button = this.activity.findViewById<FloatingActionButton>(R.id.floating_add_club_action_button)
        floating_add_club_action_button?.setOnClickListener { view ->
            var fragment: Fragment? = null
            val fragmentManager2 = this.activity.supportFragmentManager
            val fragmentTransaction = fragmentManager2.beginTransaction()
            fragment = AddClubFragment()

            fragmentTransaction.replace(R.id.content_frame, fragment)
            fragmentTransaction.commit()
        }


    }

    /**
     * @param loader
     */
    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Log.i(TAG, "onLoaderReset " + loader?.getId())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID -> mClubAdapter?.swapCursor(null)
        }
    }

    /**
     * @param id
     * @param args
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        Log.i(TAG, "onCreateLoader")
        loadInProgressDialog = ProgressDialog.show(this@ClubAdminBaseFragment.context, getString(R.string.txt_progressdialogmessage), "", true)
        var loader = SqliteLoader(this.context, db(), ClubColumns.TABLE, ClubColumns.COLUMNS, null, null, null, null, ClubColumns.DEFAULT_ORDER_BY)

        return loader
    }

    /**
     * @param loader
     * @param data
     */
    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        Log.i(TAG, "onLoadFinished " + loader?.getId())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID ->
                // Daten sind geladen. Der Cursor wird an den Adapter gebunden.
                mClubAdapter?.swapCursor(data)
        }

    }

    /**
     * Click item of the list
     * @param l the listView
     * @param v the main view
     * @param position
     * @param id
     */
    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val cursor = listAdapter.getItem(position) as Cursor

        Log.i(TAG, " Club id: " + id)
        Log.i(TAG, " Club cursor: " + cursor.getString(1))

      /*  var bundle = Bundle()
        bundle.putLong(Names.CATEGORY_ID.toString(), id)
        bundle.putString(Names.CATEGORY_NAME.toString(), cursor.getString(1))

        var fragment: Fragment? = null
        val fragmentManager2 = this.activity.supportFragmentManager
        val fragmentTransaction = fragmentManager2.beginTransaction()
        fragment = ScanMainFragment()
        fragment.setArguments(bundle)

        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()
        */
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
