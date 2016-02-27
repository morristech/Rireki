package tr.xip.rireki.ext

import tr.xip.rireki.model.RecordTitle
import tr.xip.rireki.model.RecordUnit

/**
 * Converts and returns a string array of [RecordTitle] list or [RecordUnit] list
 */
fun <T : Any> List<T>.toStringArray(): Array<String?> {
    val array: Array<String?> = arrayOfNulls(this.size)
    for ((i, item) in this.withIndex()) {
        when (item) {
            is RecordTitle -> array[i] = item.title
            is RecordUnit -> array[i] = item.unit
            else -> throw UnsupportedOperationException("List<${item.javaClass.simpleName}> does not support this operation.")
        }
    }
    return array
}
