package tr.xip.rireki.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RecordUnit(
        @PrimaryKey
        open var unit: String? = null
) : RealmObject()
