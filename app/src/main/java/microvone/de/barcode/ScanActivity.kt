package microvone.de.barcode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import microvone.de.registrationmembersystem.R
import microvone.de.utils.Names
import java.util.*

class ScanActivity : AppCompatActivity() {

    private val TAG = ScanActivity::class.java.simpleName
    private var idCategoryTxt: TextView? = null
    private var nameCategoryTxt:TextView? = null
    private var listview: ListView? = null
    private var barcodeItems: ArrayList<BarcodeItem>? = null
    private var adapter: BarcodeListAdapter? = null
    private var idCategory: Long = 0
    private var nameCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val extras = intent.extras
        if (extras != null && extras.containsKey(Names.CATEGORY_ID.toString())) {
            Log.d(TAG, Names.CATEGORY_ID.toString())
            idCategory = extras.getLong(Names.CATEGORY_ID.toString())
        }

        if (extras != null && extras.containsKey(Names.CATEGORY_NAME.toString())) {
            Log.d(TAG, Names.CATEGORY_NAME.toString())
            nameCategory = extras.getString(Names.CATEGORY_NAME.toString())
        }

        idCategoryTxt = findViewById(R.id.category_id) as TextView
        nameCategoryTxt = findViewById(R.id.category_name) as TextView
        listview = findViewById(R.id.listview) as ListView

        idCategoryTxt?.setText("" + idCategory)
        nameCategoryTxt?.setText(nameCategory)

        barcodeItems = ArrayList()
        adapter = BarcodeListAdapter(this, barcodeItems)
        // Here, you set the data in your ListView

        listview?.setAdapter(adapter)
        /**
         * get on item click listener
         */
        listview?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, v, position, id ->
            Log.i("List View Clicked", "**********")
            Toast.makeText(this@ScanActivity, "Deleted", Toast.LENGTH_LONG).show()
            barcodeItems?.removeAt(position)
            adapter?.notifyDataSetChanged()
        })
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    fun saveCodes(view: View) {
        Log.d(TAG, "Save Items!")

        if (!barcodeItems!!.isEmpty()) {
            // TODO: save

            /*
            SqlInsertTask(SQLiteDatabase db, String table,
			String nullColumnHack, ContentValues values

			final ContentValues daten = new ContentValues();
			 daten.put(GeoKontaktTbl.STICHWORT_POS, stichwort);
             */

            val task = BarcodeAddTask(this, idCategory)
            task.setAdapter(adapter)
            task.execute(barcodeItems)
            /* AsyncTask.Status status = task.getStatus();
            Log.d(TAG, "Save Items!" + status);
            if(status == AsyncTask.Status.FINISHED){
                barcodeItems.clear();
                adapter.notifyDataSetChanged();
            }
            */

        }
    }

    /**
     * Barcode scannen
     * @param view
     */
    fun scanBarcode(view: View) {
        Log.d(TAG, "Scan button works!")

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
        integrator.setPrompt("Scan a barcode")
        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.setOrientationLocked(false)
        integrator.captureActivity = SmallCaptureActivity::class.java
        integrator.initiateScan()
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d(TAG, "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "Scanned")
                //we have a result
                val scanContent = result.contents
                val scanFormat = result.formatName

                // display it on screen
                val scanValues = "FORMAT: $scanFormat\nCONTENT: $scanContent"
                Toast.makeText(this, scanValues, Toast.LENGTH_LONG).show()

                val barcodeItem = BarcodeItem.Builder(scanContent, Date()).build()
                Log.d(TAG, "barcodeItem:" + barcodeItem.code)
                if (barcodeItems!!.contains(barcodeItem)) {
                    Toast.makeText(this, "ID already exists!!!", Toast.LENGTH_LONG).show()
                } else {
                    barcodeItems?.add(barcodeItem)
                    adapter?.notifyDataSetChanged()
                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
