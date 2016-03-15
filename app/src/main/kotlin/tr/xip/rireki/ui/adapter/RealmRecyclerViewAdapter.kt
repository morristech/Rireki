package tr.xip.rireki.ui.adapter

import android.support.v7.widget.RecyclerView

import io.realm.RealmObject
import io.realm.RealmResults

abstract class RealmRecyclerViewAdapter<T : RealmObject> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var realmObjectAdapter: RealmObjectAdapter<T>? = null

    fun setRealmAdapter(realmAdapter: RealmObjectAdapter<T> ) {
        realmObjectAdapter = realmAdapter
    }

    fun getItem(position: Int): T? = realmObjectAdapter?.getItem(position)

    fun getRealmAdapter(): RealmObjectAdapter<T>? = realmObjectAdapter

    fun getDataset(): RealmResults<T>? = realmObjectAdapter?.dataset
}