package microvone.de.barcode

import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import android.os.Bundle
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeCallback
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import microvone.de.registrationmembersystem.MainActivity
import microvone.de.registrationmembersystem.R
import microvone.de.utils.Names
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues.TAG
import android.support.v7.app.AppCompatActivity
import microvone.de.base.ScanMainFragment


/**
 * Created by jörn on 21.01.2018.
 */
/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
class ContinuousCaptureActivity : AppCompatActivity() {
    val TAG = ContinuousCaptureActivity::class.java.simpleName
    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private var lastText: String? = null
    var list = ArrayList<String>()

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            lastText = result.text
            barcodeView!!.setStatusText(result.text)
            beepManager!!.playBeepSoundAndVibrate()

            //Added preview of scanned barcode
            val imageView = findViewById(R.id.barcodePreview) as ImageView
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))

            // add to list
            if(!list.contains(result.text)) {
                list.add(result.text)
            }

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.continuous_scan)

        barcodeView = findViewById(R.id.barcode_scanner) as DecoratedBarcodeView
        barcodeView!!.decodeContinuous(callback)

        beepManager = BeepManager(this)

        val bundle = intent.extras
        val idCategory: Long = bundle.getLong(Names.CATEGORY_ID.name)

        if(bundle != null) {
            val scannedCodes = bundle?.getStringArrayList(Names.SCANNED_BARCODES.name)
            if(scannedCodes != null && scannedCodes.isNotEmpty())
                list = scannedCodes
        }
        Log.i(TAG, "idCategory: " + idCategory)
  }

    override fun onBackPressed() {

       Log.i(TAG, "LISTE: "  + list)

       if (fragmentManager.getBackStackEntryCount() == 0) {
            Log.i(TAG, "if LISTE: " + list)
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtras( bundle )
            intent.putStringArrayListExtra(Names.SCANNED_BARCODES.name, list)
            setResult(3333, intent)
            this.finish()
        } else {
            Log.i(TAG, "else LISTE: " + list)
            fragmentManager.popBackStack()
        }

        super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()

        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView!!.pause()
    }

    fun pause(view: View) {
        barcodeView!!.pause()
    }

    fun resume(view: View) {
        barcodeView!!.resume()
    }

    fun triggerScan(view: View) {
        barcodeView!!.decodeSingle(callback)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}