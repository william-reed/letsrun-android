package dev.williamreed.letsrun.data

import io.objectbox.converter.PropertyConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

/**
 * Convert a [LetsRunDate] to epoch seconds for [ObjectBox] storage
 */
class LetsRunDateConverter : PropertyConverter<LetsRunDate, Long> {

    override fun convertToEntityProperty(dbValue: Long) =
        LetsRunDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(dbValue), ZoneId.of("UTC")))

    override fun convertToDatabaseValue(entity: LetsRunDate?): Long? {
        if (entity == null) return null
        return entity.datestamp.atZone(ZoneId.of("UTC")).toEpochSecond()
    }

}
