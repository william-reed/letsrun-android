package dev.williamreed.letsrun.util

import android.content.Context
import android.text.format.DateUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun dateTimeToTimeAgo(localDateTime: LocalDateTime, context: Context): String {
    val timeMs = localDateTime.toInstant(ZoneId.systemDefault().rules.getOffset(Instant.now())).toEpochMilli()
    return DateUtils.getRelativeDateTimeString(
        context,
        timeMs,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.WEEK_IN_MILLIS,
        0
    ).toString()
}
