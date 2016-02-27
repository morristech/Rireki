package tr.xip.rireki.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.widget.Toast
import tr.xip.rireki.ext.shiftDown
import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ui.fragment.DayListFragment
import java.util.*

class DayListFragmentPagerAdapter(val pager: ViewPager, val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    var date = Calendar.getInstance().toSimpleDate()

    val dataset: MutableList<DayListFragment> = arrayListOf(
            DayListFragment(Calendar.getInstance().toSimpleDate().shiftDown(1)),
            DayListFragment(Calendar.getInstance().toSimpleDate())
    )

    var previousPosition = -1;

    init {
        pager.addOnPageChangeListener(object : OnPageChangeListenerAdapter() {
            override fun onPageSelected(position: Int) {
                onPageChanged(position)
            }
        })

        pager.currentItem = count - 1
    }

    override fun getCount(): Int {
        return dataset.size
    }

    override fun getItem(position: Int): Fragment? {
        return dataset[position]
    }

    override fun getItemPosition(`object`: Any): Int = dataset.indexOf(`object`)

    public fun onPageChanged(position: Int) {
        if (position == 0 && previousPosition > position) {
            val previousDate = dataset[position].date
            dataset.add(0, DayListFragment(previousDate.shiftDown(1)))
            notifyDataSetChanged()
            pager.currentItem = 1
            Toast.makeText(pager.context, "Added new page at 0 from $position", Toast.LENGTH_SHORT)
        } else {
            previousPosition = position;
        }

        12.toDp();
    }

    fun Int.toDp(): Int {
        return this * 1 * 1 * 1 // Blah blah. Actual calculation here...
    }
}