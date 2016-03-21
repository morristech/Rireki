package tr.xip.rireki.event

import tr.xip.rireki.model.Record

/**
 * An event that is published when a Record in Realm is changed.
 */
class RecordUpdatedEvent(val record: Record)