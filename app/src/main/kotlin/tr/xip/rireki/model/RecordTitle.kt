package tr.xip.rireki.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RecordTitle(
        @PrimaryKey
        open var title: String? = null
) : RealmObject()