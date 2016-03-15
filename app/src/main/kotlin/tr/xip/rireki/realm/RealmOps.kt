package tr.xip.rireki.realm

import io.realm.Realm
import tr.xip.rireki.event.Bus
import tr.xip.rireki.event.RecordAddedEvent
import tr.xip.rireki.event.RecordRemovedEvent
import tr.xip.rireki.model.Record

/**
 * A file that contains extension methods for read/write operations of data to Realm.
 */

/**
 * Saves a Record object to realm and returns a ()[Record] that is in the realm.
 */
fun saveRecordToRealm(record: Record, realm: Realm = Realm.getDefaultInstance()): Record {
    realm.beginTransaction()
    val result = realm.createObject(Record::class.java)
    result.title = realm.copyToRealmOrUpdate(record.title)
    result.unit = realm.copyToRealmOrUpdate(record.unit)
    result.quantity = record.quantity
    result.date = record.date
    realm.commitTransaction()
    Bus.post(RecordAddedEvent(result))
    return result
}

/**
 * Removes a ()[Record] from Realm.
 */
fun removeRecordFromRealm(record: Record) {
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    record.removeFromRealm()
    realm.commitTransaction()
    Bus.post(RecordRemovedEvent())
}