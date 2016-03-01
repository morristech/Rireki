package tr.xip.rireki.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import tr.xip.rireki.ext.shiftDown
import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ext.toTimestamp
import tr.xip.rireki.ui.fragment.DayListFragment
import java.util.*

class DayListFragmentPagerAdapter(val pager: ViewPager, val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    var previousPosition = 0

    val dataset: MutableList<DayListFragment> = arrayListOf(
            DayListFragment(Calendar.getInstance().toSimpleDate().toTimestamp()),
            DayListFragment(Calendar.getInstance().toSimpleDate().shiftDown(1).toTimestamp())
    )

    init {
        pager.addOnPageChangeListener(object : OnPageChangeListenerAdapter() {
            override fun onPageSelected(position: Int) = onPageChanged(position)
        })
    }

    override fun getCount(): Int = dataset.size

    override fun getItem(position: Int): Fragment? = dataset[position]

    override fun getItemPosition(item: Any): Int = dataset.indexOf(item)

    private fun onPageChanged(position: Int) {
        if (position == count - 1) {
            dataset.add(DayListFragment(Calendar.getInstance().toSimpleDate().shiftDown(position + 1).toTimestamp()))
            notifyDataSetChanged();
        }

        previousPosition = position
    }
}