package microvone.de.registrationmembersystem

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultBus
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import microvone.de.admin.ClubAdminBaseFragment
import microvone.de.base.CategoryListFragment
import microvone.de.commons.ExportFileTask
import microvone.de.utils.FileUtils

import microvone.de.utils.Names


// TODO:  Replace Fragements by click menu -
// https://developer.android.com/training/implementing-navigation/nav-drawer.html
// http://abhiandroid.com/materialdesign/navigation-drawer
/**
 * Main activity with menu list and start fragment
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG = MainActivity::class.java.simpleName

    /**
     * Create method
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val bundle = intent.extras
        val hasScannedCodes: String = bundle?.getString(Names.SCANNED_BARCODES.name) ?: "NO"
        Log.i(TAG, "SCANNED_BARCODES: " + hasScannedCodes)

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_scanner)

    }

    /**
     * Back pressed method
     */
    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed")
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    /**
     * Create the option menu
     * @param menu
     *
     * @return TRUE
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * Method to navigate through option menu by selecting an option item
     * @param item the MenuItem
     *
     * @return TRUE
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Methode to handle the action by selecting an navigation item, e.g. start view
     * @param item the MenuItem
     *
     * @return TRUE
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.itemId)
        //make this method blank
        return true
    }

    /**
     * @param itemId the id to display the view/fragment
     */
    private fun displaySelectedScreen(itemId: Int) {

        //creating fragment object
        var fragment: Fragment? = null

        //initializing the fragment object which is selected
        when (itemId) {
            R.id.nav_scanner -> {
                fragment = CategoryListFragment()

            }
            R.id.nav_search -> {
                fragment = RegistrationSearchFragment()
            }
            R.id.nav_list -> {
                fragment = RegistrationListFragment()

            }
            R.id.nav_manage -> {
                fragment = ClubAdminBaseFragment()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        //replacing the fragment
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment)
            ft.commit()
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)


        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        Log.i(TAG, "permissionCheck(): " + permissionCheck)
        Log.i(TAG, "permissionCheck2 : " + permissionCheck2)
        // Saving allowed
        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            requestPermission(drawer)
        }

    }

    /**
     * Method to handle the results if an activity is started by result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "resultCode Items!" + resultCode)
        Log.i(TAG, "requestCode Items!" + requestCode)
        Log.i(TAG, "data Items!" + data)

        super.onActivityResult(requestCode, resultCode, data)
        ActivityResultBus.getInstance().postQueue(ActivityResultEvent(requestCode, resultCode, data))

    }


    //private val PERMISSION_REQUEST_EXTERNAL_STORAGE = 0
    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestPermission(view:View){
        // Permission has not been granted and must be requested.
        var check1 = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var check2 = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
        Log.i(TAG, "check1: " + check1)
        Log.i(TAG, "check2: " + check2)
        if (!check1 || !check2) {
            Snackbar.make(view,"Permissions are not available. Need camera and external storage permissions..",
                    Snackbar.LENGTH_SHORT).setAction("OK", View.OnClickListener {
                openAppSystemSettings()

            }).show()
        }
    }

    /**
     * Append Context class to open system settings for the app
     */
    fun Context.openAppSystemSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }

}
