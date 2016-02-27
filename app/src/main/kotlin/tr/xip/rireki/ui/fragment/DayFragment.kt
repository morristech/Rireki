package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.fragment_day.*
import tr.xip.rireki.ext.setToolbar

import tr.xip.rireki.ui.adapter.DayListFragmentPagerAdapter
import tr.xip.rireki.ui.adapter.OnPageChangeListenerAdapter

class DayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (host as AppCompatActivity).setToolbar(toolbar)

        val adapter = DayListFragmentPagerAdapter(pager, fragmentManager)
        pager.adapter = adapter
    }
}