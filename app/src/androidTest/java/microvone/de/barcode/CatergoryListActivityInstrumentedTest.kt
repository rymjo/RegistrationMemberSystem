package microvone.de.barcode

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.Button
import android.widget.ListView
import microvone.de.registrationmembersystem.R
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by jörn on 10.01.2018.
 */


@RunWith(AndroidJUnit4::class)
class CatergoryListActivityInstrumentedTest : ActivityTestRule<CatergoryListActivity>(CatergoryListActivity::class.java) {

    @Rule
    var mActivityRule = ActivityTestRule(CatergoryListActivity::class.java)


    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("microvone.de.barcode.debug", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun ensureListViewIsPresent() {
        val activity = mActivityRule.activity
        val viewById = activity.findViewById<ListView>(R.id.listview)
        assertThat<View>(viewById, notNullValue())
        assertThat<View>(viewById, instanceOf<Any>(ListView::class.java))
        val listView = viewById as ListView
        val adapter = listView.adapter
        assertThat(adapter, instanceOf<Any>(BarcodeListAdapter::class.java))
        assertThat(adapter.count, lessThanOrEqualTo<Int>(0))
    }

    /*
	 * Tests the correct layout of the activity. Are all layout elements
	 * available? Are all label texts as expected?
	 */
    @Test
    fun testScanButton() {

        // label should be available and have correct text

        // button should be available
        val activity = mActivityRule.activity
        // make resource available for the tests
        val mResources = getInstrumentation().targetContext.resources
        val button = activity.findViewById(R.id.btn_scan) as Button
        assertNotNull(button)
        assertEquals(button.text.toString(), mResources.getString(R.string.txt_scan))
    }
}