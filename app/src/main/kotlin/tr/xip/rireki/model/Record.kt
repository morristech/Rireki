package tr.xip.rireki.model

import io.realm.RealmObject

/**
 * An activity record
 */
open class Record(
        open var title: RecordTitle? = null,
        open var quantity: Int? = null,
        open var unit: RecordUnit? = null,
        open var date: Long? = null
) : RealmObject()
