package tr.xip.rireki.ui.adapter

import android.support.v4.view.ViewPager

abstract class OnPageChangeListenerAdapter() : ViewPager.OnPageChangeListener {

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}