package microvone.de.registrationmembersystem

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultBus
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import microvone.de.base.CategoryListFragment
import microvone.de.utils.Names


// TODO:  Replace Fragements by click menu -
// https://developer.android.com/training/implementing-navigation/nav-drawer.html
// http://abhiandroid.com/materialdesign/navigation-drawer
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG = MainActivity::class.java.simpleName

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

    override fun onBackPressed() {
        Log.i("MainTag", "onBackPressed")
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.itemId)
        //make this method blank
        return true
    }

    private fun displaySelectedScreen(itemId: Int) {

        //creating fragment object
        var fragment: Fragment? = null

        //initializing the fragment object which is selected
        when (itemId) {
            R.id.nav_scanner -> {
                fragment = CategoryListFragment()

            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

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
    }

    /**
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         Log.i(TAG, "resultCode Items!" + resultCode)
        Log.i(TAG, "requestCode Items!" + requestCode)
        Log.i(TAG, "data Items!" + data)

        super.onActivityResult(requestCode, resultCode, data)
        ActivityResultBus.getInstance().postQueue(ActivityResultEvent(requestCode, resultCode, data))

    }

}
