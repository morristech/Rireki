package tr.xip.rireki.event

import tr.xip.rireki.model.Record

/**
 * An event that is published when a deleted record is restored.
 */
class RecordRemovalUndoneEvent(val record: Record)