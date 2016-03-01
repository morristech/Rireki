package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.fragment_day_list.*
import tr.xip.rireki.broadcast.RecordInRealmUpdateBroadcast
import tr.xip.rireki.ext.setDisplayedChildSafe
import tr.xip.rireki.ext.toCalendar
import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ext.toTimestamp
import tr.xip.rireki.model.Record
import tr.xip.rireki.ui.adapter.DayRecordsAdapter
import tr.xip.rireki.ui.widget.DividerItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class DayListFragment() : Fragment(), RecordInRealmUpdateBroadcast.Callback {
    var date: Calendar = Calendar.getInstance().toSimpleDate()

    private val updateBroadcastReceiver = RecordInRealmUpdateBroadcast(this)

    constructor(date: Long) : this() {
        this.date = date.toCalendar()
    }

    override fun onResume() {
        super.onResume()
        updateBroadcastReceiver.registerReceiver(context)
    }

    override fun onPause() {
        super.onPause()
        updateBroadcastReceiver.unregisterReceiver(context)
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
        val dataset = Realm.getDefaultInstance().where(Record::class.java).equalTo("date", date.toTimestamp()).findAll()
        recycler.adapter = AlphaInAnimationAdapter(DayRecordsAdapter(dataset))

        flipper.setDisplayedChildSafe(if (dataset.size == 0) FLIPPER_POSITION_NO_RECORDS else FLIPPER_POSITION_RECYCLER)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("date", date)
    }

    override fun onRecordInRealmUpdate() {
        loadRecords()
    }

    companion object {
        val FLIPPER_POSITION_RECYCLER = 0
        val FLIPPER_POSITION_NO_RECORDS = 1
    }
}
