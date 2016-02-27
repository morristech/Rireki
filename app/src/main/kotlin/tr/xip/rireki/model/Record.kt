package tr.xip.rireki.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * An activity record
 */
open class Record(
        open var title: RecordTitle? = null,
        open var quantity: Int? = null,
        open var unit: RecordUnit? = null,
        open var date: Long? = null
) : RealmObject()
