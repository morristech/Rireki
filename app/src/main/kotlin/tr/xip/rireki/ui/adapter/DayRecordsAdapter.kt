package tr.xip.rireki.ui.adapter

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import io.realm.RealmResults
import tr.xip.rireki.model.Record

import kotlinx.android.synthetic.main.item_record.view.*
import tr.xip.rireki.R
import tr.xip.rireki.broadcast.RecordInRealmUpdateBroadcast

class DayRecordsAdapter(val dataset: RealmResults<Record>) : RecyclerView.Adapter<DayRecordsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_record, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.view.context
        val item = dataset[position]

        holder.view.quantity.text = item.quantity.toString()
        holder.view.unit.text = item.unit?.unit ?: "?"
        holder.view.title.text = item.title?.title ?: "?"

        val popup = PopupMenu(context, holder.view.more)
        popup.inflate(R.menu.item_record_more)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    item.removeFromRealm()
                    RecordInRealmUpdateBroadcast().send(context)
                }
            }
            true
        }
        holder.view.more.setOnClickListener {
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
