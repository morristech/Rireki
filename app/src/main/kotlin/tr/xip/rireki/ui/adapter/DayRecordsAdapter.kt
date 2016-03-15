package tr.xip.rireki.ui.adapter

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import io.realm.Realm
import tr.xip.rireki.model.Record

import kotlinx.android.synthetic.main.item_record.view.*
import tr.xip.rireki.R
import tr.xip.rireki.event.Bus
import tr.xip.rireki.event.RecordRemovalUndoneEvent

class DayRecordsAdapter(val dataset: MutableList<Record>, val coordinatorLayout: CoordinatorLayout) : RecyclerView.Adapter<DayRecordsAdapter.ViewHolder>() {

    init {
        Bus.register(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_record, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DayRecordsAdapter.ViewHolder, position: Int) {
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
                    var shouldRemove = true
                    dataset.remove(item)
                    notifyItemRemoved(position)
                    val snackbar = Snackbar.make(coordinatorLayout, "Removed ${item.title}", 3000)
                    snackbar.setAction("Undo", {
                        shouldRemove = false
                        dataset.add(position, item)
                        notifyItemInserted(position)
                    })
                    snackbar.setCallback(object : Snackbar.Callback() {
                        override fun onDismissed(snackbar: Snackbar?, event: Int) {
                            if (shouldRemove) {
                                val realm = Realm.getDefaultInstance()
                                realm.beginTransaction()
                                item.removeFromRealm()
                                realm.commitTransaction()
                            }
                        }
                    })
                    snackbar.show()
                }
            }
            true
        }
        holder.view.more.setOnClickListener {
            popup.show()
        }
    }

    override fun getItemCount(): Int = dataset.size

    @Subscribe
    fun onRemoveUndone(event: RecordRemovalUndoneEvent) {
        Log.wtf("ASD", "Brought ${event.record.title} back!")

        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface RecordRemoveSnackBarTunnel {
        fun show(item: Record, callback: RemoveSnackbarActionCallback?)
    }

    abstract class RemoveSnackbarActionCallback {
        abstract fun onUndo()
        abstract fun onRemove()
    }
}
