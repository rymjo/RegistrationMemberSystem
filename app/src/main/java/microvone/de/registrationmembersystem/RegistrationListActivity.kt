//package microvone.de.registrationmembersystem
//
//import android.app.ListActivity
//import android.app.LoaderManager
//import android.app.ProgressDialog
//import android.content.Loader
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteException
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.SimpleCursorAdapter
//import android.widget.Toast
//import microvone.de.commons.DatePickerKeys
//import microvone.de.commons.ExportCSVItem
//import microvone.de.commons.ExportFileTask
//import microvone.de.database.DbHelper
//import microvone.de.database.RegistrationColumns
//import microvone.de.database.SqliteLoader
//import microvone.de.registrationmembersystem.R.*
//import microvone.de.utils.FileUtils.isExternalStorageWritable
//import java.util.*
//
//class RegistrationListActivity : ListActivity(), LoaderManager.LoaderCallbacks<Cursor> {
//
//    private val TAG = RegistrationListActivity::class.java.simpleName
//    private var loadInProgressDialog: ProgressDialog? = null
//    private var db: SQLiteDatabase? = null
//
//
//    private val LOADER_ID = 2
//    private var mCategoryAdapter: SimpleCursorAdapter? = null
//    private var mLoaderCallbacks: LoaderManager.LoaderCallbacks<Cursor>? = null
//
//    /**
//     * Spalten des Cursors der Kategorien, die
//     * in der Liste angezeigt werden.
//     */
//    private val ANZEIGE_REGISTRATION = arrayOf<String>(RegistrationColumns.ID, RegistrationColumns.CODE,
//            RegistrationColumns.REG_DATE, RegistrationColumns.STATUS, RegistrationColumns.ID_CATEGORY)
//
//    /** IDs im SimpleListView Layout.  */
//    private val SIMPLE_LIST_VIEW_IDS = intArrayOf(id.txt_registration_item_id, id.txt_registration_item_code,
//            id.txt_registration_item_date, id.txt_registration_item_status, id.txt_registration_item_idcategory)
//    private var startDate: String? = null
//    private var stopDate: String? = null
//    private var saveButton: Button? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "onCreate(): entered...")
//        setContentView(R.layout.activity_registration_list)
//
//        val extras = intent.extras
//        if (extras != null) {
//            startDate = extras.getString(DatePickerKeys.START_DATE)
//            stopDate = extras.getString(DatePickerKeys.STOP_DATE)
//        }
//        // Saving allowed
//        if (!isExternalStorageWritable()) {
//            saveButton?.setEnabled(false)
//            Toast.makeText(applicationContext, "Saving files not allowed", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(applicationContext, "Saving files allowed", Toast.LENGTH_LONG).show()
//        }
//        saveButton = findViewById(R.id.btn_export) as Button
//
//
//        Log.d(TAG, "onCreate(): startDate..." + startDate)
//        Log.d(TAG, "onCreate(): stopDate..." + stopDate)
//        // Hier bereitet man den CursorAdapter vor. Er wird aber noch nicht
//        // an einen Cursor gebunden. Dies geschieht erst, nachdem der Cursor
//        // nach einer Datenanfrage zurückgegeben wurde (onLoadFinished).
//        // Der letzte Parameter ist ebenfalls 0. Das vermeidet, dass der
//        // Adapter einen eigenen ContentObserver für den Cursor registriert.
//        // Diese Aufgabe übernimmt der CursorLoader.
//        mCategoryAdapter = SimpleCursorAdapter(this, R.layout.registration_list_item, null, ANZEIGE_REGISTRATION, SIMPLE_LIST_VIEW_IDS, 0)
//        listAdapter = mCategoryAdapter
//
//        // Referenz auf das Objekt, das sich um die Callbacks nach einer
//        // Datenanfrage kümmert. Ist i.A. die Activity oder das aufrufende
//        // Fragment.
//        mLoaderCallbacks = this
//
//
//        // Registriert einen Loader mit ID LOADER_ID_KONTAKTLISTE beim LoaderManager.
//        // Ab hier übernimmt der Manager die Kontrolle über den Lebenszyklus des Loaders.
//        val lm = loaderManager
//        lm.initLoader(LOADER_ID, null, mLoaderCallbacks)
//    }
//
//    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
//        Log.d(TAG, "onCreateLoader")
//
//        var selection: String? = null
//        if (stopDate != null && startDate != null) {
//
//            selection = RegistrationColumns.REG_DATE + " BETWEEN '" + startDate + " 00:00:00' AND " + "'" + stopDate + " 23:59:59'"
//            //RegistrationColumns.REG_DATE + " <= '" + stopDate + "'";
//        }
//        Log.d(TAG, "selection: " + selection!!)
//
//        loadInProgressDialog = ProgressDialog.show(this@RegistrationListActivity, getString(R.string.txt_progressdialogmessage), "", true)
//        return SqliteLoader(this, db(), RegistrationColumns.TABLE, RegistrationColumns.COLUMNS,
//                selection, null, null, null, RegistrationColumns.DEFAULT_ORDER_BY)
//    }
//
//    override fun onLoaderReset(loader: Loader<Cursor>?) {
//        Log.d(TAG, "onLoaderReset " + loader?.getId())
//        loadInProgressDialog?.cancel()
//        when (loader?.getId()) {
//            LOADER_ID -> mCategoryAdapter?.swapCursor(null)
//        }
//    }
//
//    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
//        Log.d(TAG, "onLoadFinished " + loader?.getId())
//        Log.d(TAG, "onLoadFinished: mCategoryAdapter count..." + mCategoryAdapter?.getCount())
//        loadInProgressDialog?.cancel()
//        when (loader?.getId()) {
//            LOADER_ID ->
//                // Daten sind geladen. Der Cursor wird an den Adapter gebunden.
//                mCategoryAdapter?.swapCursor(data)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onDestroy() {
//        loaderManager.destroyLoader(LOADER_ID)
//        if (db != null) {
//            db?.close()
//        }
//        super.onDestroy()
//        Log.d(TAG, "onDestroy")
//    }
//
//    override fun onStop() {
//        if (db != null) {
//            db?.close()
//        }
//        Log.d(TAG, "onStop")
//        super.onStop()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "onPause")
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "onResume")
//    }
//
//    private fun db(): SQLiteDatabase? {
//        val dbm = DbHelper.getInstance(this)
//        try {
//            db = dbm.getReadableDatabase()
//        } catch (e: SQLiteException) {
//            Log.e(TAG, "SQL: " + e)
//        }
//
//        return db
//    }
//
//    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.mainmenu, menu)
//        return true
//    }*/
//
//    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//        // action with ID action_refresh was selected
//            R.id.action_list -> {
//                val c = Calendar.getInstance()
//                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//                val currentDate = simpleDateFormat.format(c.time)
//                val intent = Intent(this, RegistrationListActivity::class.java)
//                intent.putExtra(DatePickerKeys.START_DATE, currentDate)
//                intent.putExtra(DatePickerKeys.STOP_DATE, currentDate)
//                startActivity(intent)
//            }
//            else -> {
//                val intent2 = Intent(this, RegistrationSearchActivity::class.java)
//                startActivity(intent2)
//            }
//        }
//
//        return true
//    }*/
//
//    /**
//     * TODO: Check for external storage
//     * @param view
//     */
//    fun exportToFile(view: View) {
//
//        Log.d(TAG, "exportToFile")
//        val cursor = mCategoryAdapter?.getCursor()
//        val values = ArrayList<ExportCSVItem>()
//        //ArrayList<ExportCSVItem> values = new ArrayList();
//        try {
//            /**
//             * final String[] ANZEIGE_REGISTRATION = new String[] {
//             * RegistrationColumns.ID, RegistrationColumns.CODE,
//             * RegistrationColumns.REG_DATE, RegistrationColumns.STATUS,
//             * RegistrationColumns.ID_CATEGORY
//             */
//            var valuesPerLine: ArrayList<String> = ArrayList()
//            valuesPerLine.add(RegistrationColumns.CODE)
//            valuesPerLine.add(RegistrationColumns.REG_DATE)
//            valuesPerLine.add(RegistrationColumns.STATUS)
//            valuesPerLine.add(RegistrationColumns.ID_CATEGORY)
//            var item = ExportCSVItem(valuesPerLine)
//            values.add(item)
//            Log.d(TAG, "cursor. count)" + cursor!!.count)
//            if (cursor != null && cursor.count > 0) {
//                if (cursor.moveToFirst()) {
//                    //
//                    do {
//                        Log.d(TAG, "cursor.getString(1)" + cursor.getString(1))
//                        Log.d(TAG, "cursor.getString(2)" + cursor.getString(2))
//                        Log.d(TAG, "cursor.getString(3)" + cursor.getString(3))
//                        Log.d(TAG, "cursor.getString(4)" + cursor.getString(4))
//                        valuesPerLine = ArrayList()
//                        valuesPerLine.add(cursor.getString(1))
//                        valuesPerLine.add(cursor.getString(2))
//                        valuesPerLine.add(cursor.getString(3))
//                        valuesPerLine.add(cursor.getString(4))
//                        item = ExportCSVItem(valuesPerLine)
//                        values.add(item)
//                    } while (cursor.moveToNext())
//                }
//                cursor.close()
//            }
//
//
//        } finally {
//            cursor?.close()
//        }
//
//        val task = ExportFileTask(view.context, values)
//        task.execute()
//
//    }
//
//
//}
