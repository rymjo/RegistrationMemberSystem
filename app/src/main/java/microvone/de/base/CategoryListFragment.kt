package microvone.de.base

import android.app.FragmentManager
import android.app.ProgressDialog

import android.support.v4.app.LoaderManager

import android.content.Intent

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.support.v4.content.Loader

import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter

import microvone.de.barcode.ScanActivity
import microvone.de.database.CategoryColumns
import microvone.de.database.DbHelper
import microvone.de.database.SqliteLoader
import microvone.de.registrationmembersystem.R
import microvone.de.utils.Names


/**
 * Created by jörn on 18.01.2018.
 */
class CategoryListFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val TAG = CategoryListFragment::class.java!!.getSimpleName()
    private var loadInProgressDialog: ProgressDialog? = null
    private var db: SQLiteDatabase? = null

    private val LOADER_ID = 1
    private var mCategoryAdapter: SimpleCursorAdapter? = null
    private var mLoaderCallbacks: LoaderManager.LoaderCallbacks<Cursor>? = null

    /**
     * Spalten des Cursors der Kategorien, die
     * in der Liste angezeigt werden.
     */
    private val ANZEIGE_CATEGORY = CategoryColumns.COLUMNS

    /** IDs im SimpleListView Layout.  */
    private val SIMPLE_LIST_VIEW_IDS = intArrayOf(android.R.id.text1, android.R.id.text2)

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {


        //returning our layout file

        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater!!.inflate(R.layout.fragment_category_list, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
        activity.title = "Scannen"

        Log.i(TAG, "onCreate(): entered...")

        // Hier bereitet man den CursorAdapter vor. Er wird aber noch nicht
        // an einen Cursor gebunden. Dies geschieht erst, nachdem der Cursor
        // nach einer Datenanfrage zurückgegeben wurde (onLoadFinished).
        // Der letzte Parameter ist ebenfalls 0. Das vermeidet, dass der
        // Adapter einen eigenen ContentObserver für den Cursor registriert.
        // Diese Aufgabe übernimmt der CursorLoader.
        mCategoryAdapter = SimpleCursorAdapter(this.context, android.R.layout.simple_list_item_2, null, ANZEIGE_CATEGORY, SIMPLE_LIST_VIEW_IDS, 0)
        listAdapter = mCategoryAdapter

        // Referenz auf das Objekt, das sich um die Callbacks nach einer
        // Datenanfrage kümmert. Ist i.A. die Activity oder das aufrufende
        // Fragment.
        mLoaderCallbacks = this


        // Registriert einen Loader mit ID LOADER_ID_KONTAKTLISTE beim LoaderManager.
        // Ab hier übernimmt der Manager die Kontrolle über den Lebenszyklus des Loaders.
        val lm = loaderManager
        lm.initLoader(LOADER_ID, null, mLoaderCallbacks)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Log.i(TAG, "onLoaderReset " + loader?.getId())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID -> mCategoryAdapter?.swapCursor(null)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        Log.i(TAG, "onCreateLoader")
        loadInProgressDialog = ProgressDialog.show(this@CategoryListFragment.context, getString(R.string.txt_progressdialogmessage), "", true)
        var loader = SqliteLoader(this.context, db(), CategoryColumns.TABLE, CategoryColumns.COLUMNS, null, null, null, null, CategoryColumns.DEFAULT_ORDER_BY)

        return loader
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        Log.i(TAG, "onLoadFinished " + loader?.getId())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID ->
                // Daten sind geladen. Der Cursor wird an den Adapter gebunden.
                mCategoryAdapter?.swapCursor(data)
        }

    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val cursor = listAdapter.getItem(position) as Cursor

        Log.i(TAG, " Category sprung zum Scannen: " + id)
        Log.i(TAG, " Category sprung zum Scannen: " + cursor.getString(1))

       /* val intent = Intent(this.context, ScanActivity::class.java)
        intent.putExtra(Names.CATEGORY_ID.toString(), id)
        intent.putExtra(Names.CATEGORY_NAME.toString(), cursor.getString(1))
        startActivity(intent)*/

        var bundle = Bundle()
        bundle.putLong(Names.CATEGORY_ID.toString(), id)
        bundle.putString(Names.CATEGORY_NAME.toString(), cursor.getString(1))

        var fragment: Fragment? = null
        val fragmentManager2 = this.activity.supportFragmentManager
        val fragmentTransaction = fragmentManager2.beginTransaction()
        fragment = ScanMainFragment()
        fragment.setArguments(bundle)

        //fragmentTransaction.addToBackStack("xyz")
        //fragmentTransaction.hide(this@MeinProfilFragment)
        fragmentTransaction.replace(R.id.content_frame, fragment)
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        //ft.replace(R.id.content_frame, fragment)
        //ft.commit()
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