package tr.xip.rireki.ext

import java.util.Calendar

/**
 * Strips Calendar of its HOUR, MINUTE, SECOND, and MILLISECOND values, thus returning a Calendar
 * which only has information about the date and not the time.
 */
fun Calendar.toSimpleDate(): Calendar {
    set(Calendar.HOUR, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}

/**
 * Returns UNIX timestamp from this Calendar instance
 */
fun Calendar.toTimestamp(): Long = time.time

/**
 * Shifts the current calendar instance up by [days] and returns it
 */
fun Calendar.shiftUp(days: Int): Calendar {
    add(Calendar.DAY_OF_YEAR, days)
    return this
}

/**
 * Shifts the current calendar instance down by [days] and returns it
 */
fun Calendar.shiftDown(days: Int): Calendar {
    add(Calendar.DAY_OF_YEAR, -days)
    return this
}

