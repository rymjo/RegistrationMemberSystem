package microvone.de.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment
import microvone.de.barcode.*
import microvone.de.registrationmembersystem.R
import microvone.de.utils.Names
import java.util.*
import microvone.de.barcode.ContinuousCaptureActivity
import microvone.de.utils.Constants


/**
 * Created by jörn on 18.01.2018.
 */
class ScanMainFragment : StatedFragment() {

    private val TAG = ScanMainFragment::class.java.simpleName
    private var idCategoryTxt: TextView? = null
    private var nameCategoryTxt: TextView? = null
    private var listview: ListView? = null
    private var barcodeItems: ArrayList<BarcodeItem>? = null
    private var adapter: BarcodeListAdapter? = null
    private var idCategory: Long = 0
    private var nameCategory: String? = null

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater!!.inflate(R.layout.fragment_scan_main, container, false)
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
        activity.title = "Scannen"

        var bundle = arguments
        if (bundle != null) {
            //int i = bundle.getInt(key, defaulValue);
            Log.d(TAG, Names.CATEGORY_ID.toString())
            idCategory = bundle.getLong(Names.CATEGORY_ID.toString())

            Log.d(TAG, Names.CATEGORY_NAME.toString())
            nameCategory = bundle.getString(Names.CATEGORY_NAME.toString())
        }

        idCategoryTxt = this.activity.findViewById(R.id.category_id) as TextView
        nameCategoryTxt = this.activity.findViewById(R.id.category_name) as TextView
        listview = this.activity.findViewById(R.id.listview) as ListView

        idCategoryTxt?.setText("" + idCategory)
        nameCategoryTxt?.setText(nameCategory)


        barcodeItems = ArrayList()
        adapter = BarcodeListAdapter(this.activity, barcodeItems)
        // Here, you set the data in your ListView

        listview?.setAdapter(adapter)
        /**
         * get on item click listener
         */
        listview?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, v, position, id ->
            Log.d("List View Clicked", "**********")
            Toast.makeText(this@ScanMainFragment.context, "Deleted", Toast.LENGTH_LONG).show()
            barcodeItems?.removeAt(position)
            adapter?.notifyDataSetChanged()
        })

        val scanButton = this.activity.findViewById(R.id.btn_scan) as Button
        scanButton.setOnClickListener { v -> scanBarCodes(v) }

        val saveButton = this.activity.findViewById(R.id.btn_save) as Button
        saveButton.setOnClickListener { v -> saveBarCodes(v) }
    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")

    }

    /**
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    /**
     * Save the barcodes
     * @param view
     */
    fun saveBarCodes(view: View) {
        Log.i(TAG, "Save Items!")
        Log.i(TAG, "barcodeItems!" + barcodeItems)
        if (!barcodeItems!!.isEmpty()) {


            val task = BarcodeAddTask(this.context, idCategory)
            task.setAdapter(adapter)
            task.execute(barcodeItems)

        }
    }

    /**
     * Barcode scannen
     * @param view
     */
    fun scanBarCodes(view: View) {
        Log.i(TAG, "Scan button works!")

        val intent = Intent(this.activity, ContinuousCaptureActivity::class.java)
        intent.putExtra(Names.CATEGORY_ID.name, idCategory)
        startActivityForResult(intent, Constants.SCANNING_RESULT.id)

    }

    /**
     * Aufbau der Liste nach dem Scannen
     * @param requestCode
     * @param resultCode
     * @param data
     */
    // TODO: Handle onActivityResult in Fragments!!!
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // Add your code here
        Log.i(TAG, "onActivityResult 2: " + resultCode)

        if(resultCode == Constants.SCANNING_RESULT.id) {
            Log.i(TAG, "Scanned")
            Toast.makeText(this.context, "Daten wurden gescannt", Toast.LENGTH_LONG).show()

            if (data != null) {
                 val scannedCodes = data.extras.getStringArrayList(Names.SCANNED_BARCODES.name)
                Log.i(TAG, "scannedCodes:: " + scannedCodes)
                if(scannedCodes.isNotEmpty()) {
                    for(scanContent in scannedCodes) {
                        Log.i(TAG, "scanContent:: " + scanContent)
                        val barcodeItem = BarcodeItem.Builder(scanContent, Date()).build()
                        if (!barcodeItems!!.contains(barcodeItem)) {
                            barcodeItems?.add(barcodeItem)
                        }
                    }
                    adapter?.notifyDataSetChanged()
                }

            }

        }

    }
}