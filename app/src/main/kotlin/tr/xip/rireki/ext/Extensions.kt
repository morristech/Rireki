package tr.xip.rireki.ext

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.TextView
import android.widget.ViewFlipper
import io.realm.RealmObject
import io.realm.RealmResults
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

/**
 * Returns a Calendar object from a timestamp
 */
fun Long.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar
}

fun <E : RealmObject> RealmResults<E>.realIndexOf(item: E): Int {
    for ((index, value) in this.withIndex()) {
        if (value.equals(item)) return index
    }
    return -1
}