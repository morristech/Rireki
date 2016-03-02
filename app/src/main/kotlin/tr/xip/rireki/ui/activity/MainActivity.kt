package tr.xip.rireki.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import tr.xip.rireki.R

import tr.xip.rireki.ui.dialog.NewRecordDialog
import tr.xip.rireki.ui.fragment.DayFragment
import tr.xip.rireki.ui.fragment.WeekFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var toolbarTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.setStatusBarBackground(R.color.apptheme_primary_dark)
        drawer.setNavigationItemSelectedListener(this)

        selectItem(R.id.navigation_day)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(drawer)
            }
            R.id.action_settings -> {
                /* empty */
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.title = toolbarTitle
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        if (item == null) return false
        return selectItem(item.itemId)
    }

    private fun selectItem(itemId: Int): Boolean {
        var fragment: Fragment

        when (itemId) {
            R.id.navigation_day -> {
                toolbarTitle = getString(R.string.navigation_item_day)
                fragment = DayFragment()
            }
            R.id.navigation_week -> {
                toolbarTitle = getString(R.string.navigation_item_week)
                fragment = WeekFragment()
            }
            else -> return false
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

        drawerLayout.closeDrawer(drawer)

        return true
    }
}
