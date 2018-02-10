package microvone.de.registrationmembersystem


import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ListFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import microvone.de.commons.DatePickerKeys
import microvone.de.commons.ExportCSVItem
import microvone.de.commons.ExportFileTask
import microvone.de.database.DbHelper
import microvone.de.database.RegistrationColumns
import microvone.de.database.SqliteLoader
import microvone.de.utils.FileUtils.isExternalStorageWritable


/**
 * List to show all registartion items.
 */
 class RegistrationListFragment: ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val TAG = RegistrationListFragment::class.java.simpleName
    private var loadInProgressDialog: ProgressDialog? = null
    private var db: SQLiteDatabase? = null


    private val LOADER_ID = 2
    private var mCategoryAdapter: SimpleCursorAdapter? = null
    private var mLoaderCallbacks: LoaderManager.LoaderCallbacks<Cursor>? = null

    /**
     * Spalten des Cursors der Kategorien, die
     * in der Liste angezeigt werden.
     */
    private val ANZEIGE_REGISTRATION = arrayOf<String>(RegistrationColumns.ID, RegistrationColumns.CODE,
            RegistrationColumns.REG_DATE, RegistrationColumns.STATUS, RegistrationColumns.ID_CATEGORY)

    /** IDs im SimpleListView Layout.  */
    private val SIMPLE_LIST_VIEW_IDS = intArrayOf(R.id.txt_registration_item_id, R.id.txt_registration_item_code,
            R.id.txt_registration_item_date, R.id.txt_registration_item_status, R.id.txt_registration_item_idcategory)
    private var startDate: String? = null
    private var stopDate: String? = null
    private var exportButton: Button? = null

    /**
     * @param loader
     */
    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Log.d(TAG, "onLoaderReset " + loader?.getId())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID -> mCategoryAdapter?.swapCursor(null)
        }
    }

    /**
     * @param loader
     * @param data
     */
    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        Log.d(TAG, "onLoadFinished " + loader?.getId())
        Log.d(TAG, "onLoadFinished: mCategoryAdapter count..." + mCategoryAdapter?.getCount())
        loadInProgressDialog?.cancel()
        when (loader?.getId()) {
            LOADER_ID ->
                // Daten sind geladen. Der Cursor wird an den Adapter gebunden.
                mCategoryAdapter?.swapCursor(data)
        }
    }

    /**
     * @param id
     * @param args
     *
     * @return Loader<Cursor>
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d(TAG, "onCreateLoader")

        var selection: String? = null
        if (stopDate != null && startDate != null) {

            selection = RegistrationColumns.REG_DATE + " BETWEEN '" + startDate + " 00:00:00' AND " + "'" + stopDate + " 23:59:59'"
            //RegistrationColumns.REG_DATE + " <= '" + stopDate + "'";
        }
        Log.i(TAG, "selection: " + selection)

        loadInProgressDialog = ProgressDialog.show(this@RegistrationListFragment.activity, getString(R.string.txt_progressdialogmessage), "", true)
        return SqliteLoader(this.context, db(), RegistrationColumns.TABLE, RegistrationColumns.COLUMNS,
                selection, null, null, null, RegistrationColumns.DEFAULT_ORDER_BY)
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?, savedInstanceState:Bundle?):View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_registration_list, container, false)
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onCreate(): entered...")

        var bundle = arguments
        if (bundle != null) {
            startDate = bundle.getString(DatePickerKeys.START_DATE)
            stopDate = bundle.getString(DatePickerKeys.STOP_DATE)

        }
        // Assume thisActivity is the current activity
        val permissionCheck = ContextCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        Log.i(TAG, "permissionCheck(): " + permissionCheck)

        // Saving allowed
        if (!isExternalStorageWritable() || permissionCheck != PackageManager.PERMISSION_GRANTED) {
            exportButton?.setEnabled(false)
            Toast.makeText(this.activity.applicationContext, "Saving files not allowed", Toast.LENGTH_LONG).show()
            if(view != null) {
                requestPermission(view)
            }
        } else {
            Toast.makeText(this.activity.applicationContext, "Saving files allowed", Toast.LENGTH_LONG).show()
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        44444)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
        exportButton = this.activity.findViewById(R.id.btn_export) as Button
        exportButton?.setOnClickListener { v -> exportToFile(v) }
        Log.i(TAG, "onCreate(): startDate..." + startDate)
        Log.i(TAG, "onCreate(): stopDate..." + stopDate)
        // Hier bereitet man den CursorAdapter vor. Er wird aber noch nicht
        // an einen Cursor gebunden. Dies geschieht erst, nachdem der Cursor
        // nach einer Datenanfrage zurückgegeben wurde (onLoadFinished).
        // Der letzte Parameter ist ebenfalls 0. Das vermeidet, dass der
        // Adapter einen eigenen ContentObserver für den Cursor registriert.
        // Diese Aufgabe übernimmt der CursorLoader.
        mCategoryAdapter = SimpleCursorAdapter(this.context, R.layout.registration_list_item, null, ANZEIGE_REGISTRATION, SIMPLE_LIST_VIEW_IDS, 0)
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

    /**
     * TODO: Check for external storage
     * @param view
     */
    fun exportToFile(view: View) {

        Log.i(TAG, "exportToFile")
        val cursor = mCategoryAdapter?.getCursor()
        val values = ArrayList<ExportCSVItem>()
        //ArrayList<ExportCSVItem> values = new ArrayList();
        try {
            /**
             * final String[] ANZEIGE_REGISTRATION = new String[] {
             * RegistrationColumns.ID, RegistrationColumns.CODE,
             * RegistrationColumns.REG_DATE, RegistrationColumns.STATUS,
             * RegistrationColumns.ID_CATEGORY
             */
            var valuesPerLine: ArrayList<String> = ArrayList()
            valuesPerLine.add(RegistrationColumns.CODE)
            valuesPerLine.add(RegistrationColumns.REG_DATE)
            valuesPerLine.add(RegistrationColumns.STATUS)
            valuesPerLine.add(RegistrationColumns.ID_CATEGORY)
            var item = ExportCSVItem(valuesPerLine)
            values.add(item)
            Log.i(TAG, "cursor. count)" + cursor!!.count)
            if (cursor != null && cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    //
                    do {
                        Log.i(TAG, "cursor.getString(1)" + cursor.getString(1))
                        Log.i(TAG, "cursor.getString(2)" + cursor.getString(2))
                        Log.i(TAG, "cursor.getString(3)" + cursor.getString(3))
                        Log.i(TAG, "cursor.getString(4)" + cursor.getString(4))
                        valuesPerLine = ArrayList()
                        valuesPerLine.add(cursor.getString(1))
                        valuesPerLine.add(cursor.getString(2))
                        valuesPerLine.add(cursor.getString(3))
                        valuesPerLine.add(cursor.getString(4))
                        item = ExportCSVItem(valuesPerLine)
                        values.add(item)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }


        } finally {
            cursor?.close()
        }

        val task = ExportFileTask(view.context, values)
        task.execute()

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

    private val PERMISSION_REQUEST_EXTERNAL_STORAGE = 0
    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestPermission(view: View) {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.

            Snackbar.make(view, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", View.OnClickListener {


                // Request the permission
                //ActivityCompat.requestPermissions(this.activity, ANZEIGE_REGISTRATION).show()
            }).show()
        } else {
            Snackbar.make(view,
                    "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
           // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                  //  PERMISSION_REQUEST_CAMERA);
        }
}

}
