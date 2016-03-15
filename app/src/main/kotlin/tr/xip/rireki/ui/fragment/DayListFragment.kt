package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.otto.Subscribe
import io.realm.Realm
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_day.*
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.fragment_day_list.*
import tr.xip.rireki.event.Bus
import tr.xip.rireki.event.RecordAddedEvent
import tr.xip.rireki.event.RecordRemovedEvent
import tr.xip.rireki.ext.*
import tr.xip.rireki.model.Record
import tr.xip.rireki.ui.adapter.DayRecordsAdapter
import tr.xip.rireki.ui.widget.DividerItemDecoration
import java.util.*

class DayListFragment() : Fragment() {
    var adapter: DayRecordsAdapter? = null

    var date: Calendar = Calendar.getInstance().toSimpleDate()

    constructor(date: Long) : this() {
        this.date = date.toCalendar()
    }

    override fun onResume() {
        super.onResume()
        Bus.register(this)
    }

    override fun onPause() {
        super.onPause()
        Bus.unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_day_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            date = savedInstanceState.getSerializable("date") as Calendar
        }

        val manager = LinearLayoutManager(context)
        recycler.layoutManager = manager
        recycler.addItemDecoration(DividerItemDecoration(context))

        loadRecords()
    }

    private fun loadRecords() {
        val dataset = mutableListOf<Record>()
        dataset.addAll(Realm.getDefaultInstance().where(Record::class.java).equalTo("date", date.toTimestamp()).findAll())
        adapter = DayRecordsAdapter(dataset, activity.coordinatorLayout)
        recycler.adapter = AlphaInAnimationAdapter(adapter)
        notifyViewFlipper()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("date", date)
    }

    private fun notifyViewFlipper() {
        flipper.setDisplayedChildSafe(if (adapter?.itemCount != 0) FLIPPER_POSITION_RECYCLER else FLIPPER_POSITION_NO_RECORDS )
    }

    @Subscribe
    fun onRecordAddedToRealm(event: RecordAddedEvent) {
        if (adapter != null) {
            adapter!!.dataset.add(event.record)
            recycler.adapter.notifyItemInserted(adapter!!.itemCount - 1)
            notifyViewFlipper()
        }
    }

    @Subscribe
    fun onRecordRemovedFromRealm(event: RecordRemovedEvent) {
        notifyViewFlipper()
    }

    companion object {
        private val FLIPPER_POSITION_RECYCLER = 0
        private val FLIPPER_POSITION_NO_RECORDS = 1
    }
}
