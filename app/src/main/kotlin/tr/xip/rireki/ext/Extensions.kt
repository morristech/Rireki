package tr.xip.rireki.ext

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import android.widget.ViewFlipper
import tr.xip.rireki.R
import java.util.*

/**
 * Sets the displayed child to a given position if the position isn't the currently displayed one
 */
fun ViewFlipper.setDisplayedChildSafe(child: Int) {
    if (displayedChild != child) displayedChild = child
}

fun AppCompatActivity.setToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

/**
 * Checks whether the [TextView] is truly empty
 */
fun TextView.isEmpty(): Boolean = text.trim().length <= 0