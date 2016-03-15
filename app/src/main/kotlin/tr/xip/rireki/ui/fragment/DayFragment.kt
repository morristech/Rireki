package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.fragment_day.*
import tr.xip.rireki.event.Bus
import tr.xip.rireki.event.RecordRemovalUndoneEvent
import tr.xip.rireki.ext.setToolbar
import tr.xip.rireki.ext.shiftDown
import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ext.toTimestamp
import tr.xip.rireki.model.Record
import tr.xip.rireki.realm.saveRecordToRealm

import tr.xip.rireki.ui.adapter.DayListFragmentPagerAdapter
import tr.xip.rireki.ui.adapter.DayRecordsAdapter
import tr.xip.rireki.ui.adapter.OnPageChangeListenerAdapter
import tr.xip.rireki.ui.dialog.NewRecordDialog
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment(), DayRecordsAdapter.RecordRemoveSnackBarTunnel {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (host as AppCompatActivity).setToolbar(toolbar)

        addNewRecord.setOnClickListener {
            NewRecordDialog().create(activity as AppCompatActivity).show()
        }

        val adapter = DayListFragmentPagerAdapter(pager, fragmentManager)
        pager.adapter = adapter

        pager.addOnPageChangeListener(object : OnPageChangeListenerAdapter() {
            override fun onPageSelected(position: Int) = notifyPageSelected(position)
        })

        nextDay.setOnClickListener {
            if (pager.currentItem != 0) pager.currentItem--
        }
        nextDay.setOnLongClickListener {
            val dataset = adapter.dataset
            pager.currentItem = 0
            for (i in dataset.indices) {
                if (i > 1) dataset.removeAt(dataset.size - 1)
            }
            adapter.notifyDataSetChanged()
            true
        }

        previousDay.setOnClickListener {
            pager.currentItem++
        }

        notifyPageSelected(0)
    }

    private fun notifyPageSelected(position: Int) {
        nextDay.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE

        val date = (pager.adapter as DayListFragmentPagerAdapter).dataset[position].date

        when (date) {
            Calendar.getInstance().toSimpleDate() -> {
                dateTextView.text = getString(R.string.today)
            }
            Calendar.getInstance().toSimpleDate().shiftDown(1) -> {
                dateTextView.text = getString(R.string.yesterday)
            }
            else -> {
                val format = SimpleDateFormat("d MMMM y")
                dateTextView.text = format.format(date.toTimestamp())
            }
        }

        /* AppBarLayout and FAB show/hide */
        appBarLayout.setExpanded(true, true)
        addNewRecord.show()
    }

    override fun show(item: Record, callback: DayRecordsAdapter.RemoveSnackbarActionCallback?) {
        var shouldRemove = true
        val snackbar = Snackbar.make(coordinatorLayout, "Removed ${item.title}", 3000)
        snackbar.setAction("Undo", {
            shouldRemove = false
            callback?.onUndo()
        })
        snackbar.setCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar?, event: Int) {
                if (shouldRemove) {
                    callback?.onRemove()
                }
            }
        })
        snackbar.show()
    }
}