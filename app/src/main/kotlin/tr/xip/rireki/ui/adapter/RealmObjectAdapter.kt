package tr.xip.rireki.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import io.realm.RealmBaseAdapter
import io.realm.RealmObject
import io.realm.RealmResults

class RealmObjectAdapter<T : RealmObject>(context: Context, val dataset: RealmResults<T>, automaticUpdate: Boolean) : RealmBaseAdapter<T>(context, dataset, automaticUpdate) {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? = null
}