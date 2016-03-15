package tr.xip.rireki.event

import tr.xip.rireki.model.Record

/**
 * An event that is published when a new Record is saved to Realm.
 */
class RecordAddedEvent(val record: Record)