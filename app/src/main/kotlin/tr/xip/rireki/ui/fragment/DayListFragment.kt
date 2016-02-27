package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.fragment_day_list.*
import tr.xip.rireki.broadcast.RecordInRealmUpdateBroadcast
import tr.xip.rireki.ext.setDisplayedChildSafe
import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ext.toTimestamp
import tr.xip.rireki.model.Record
import tr.xip.rireki.ui.adapter.DayRecordsAdapter
import tr.xip.rireki.ui.widget.DividerItemDecoration
import java.util.*

class DayListFragment() : Fragment(), RecordInRealmUpdateBroadcast.Callback {
    private val flipperRecyclerPosition = 0
    private val flipperNoRecordsPosition = 1

    var date: Calendar = Calendar.getInstance().toSimpleDate()

    private val updateBroadcastReceiver = RecordInRealmUpdateBroadcast(this)

    constructor(date: Calendar) : this() {
        this.date = date
    }

    override fun onResume() {
        super.onResume()
        updateBroadcastReceiver.registerReceiver(context)
    }

    override fun onPause() {
        super.onPause()
        updateBroadcastReceiver.unregisterReceiver(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_day_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val manager = LinearLayoutManager(context)
        recycler.layoutManager = manager
        recycler.addItemDecoration(DividerItemDecoration(context))

        loadRecords()
    }

    private fun loadRecords() {
        val dataset = Realm.getDefaultInstance().where(Record::class.java).equalTo("date", date.toTimestamp()).findAll()
        recycler.adapter = AlphaInAnimationAdapter(DayRecordsAdapter(dataset))

        flipper.setDisplayedChildSafe(if (dataset.size == 0) flipperNoRecordsPosition else flipperRecyclerPosition)
    }

    override fun onRecordInRealmUpdate() {
        loadRecords()
    }
}
