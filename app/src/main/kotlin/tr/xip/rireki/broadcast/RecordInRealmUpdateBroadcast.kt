package tr.xip.rireki.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager

class RecordInRealmUpdateBroadcast(val callback: RecordInRealmUpdateBroadcast.Callback? = null) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (callback == null) throw IllegalArgumentException("Callback can't be null when registering a receiver!")
        callback.onRecordInRealmUpdate()
    }

    fun send(context: Context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(action))
    }

    fun registerReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(action))
    }

    fun unregisterReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
    }

    interface Callback {
        fun onRecordInRealmUpdate()
    }

    companion object {
        val action = "RecordInRealmUpdateBroadcast"
    }
}