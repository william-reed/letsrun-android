package dev.williamreed.letsrun.data

import kotlinx.serialization.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * A reply to a particular thread
 */
@Serializable
data class ThreadReply(
    val id: Int,
    val thread: Int,
    val author: String,
    val parent: Int,
    val subject: String,
    @SerialName("body_text")
    val bodyText: String,
    val datestamp: LetsRunDate
)

/**
 * Wrapper around a [LocalDateTime] to make it serializable
 */
@Serializable
data class LetsRunDate(
    val datestamp: LocalDateTime
) {
    @Serializer(forClass = LetsRunDate::class)
    companion object : KSerializer<LetsRunDate> {
        // 2019-07-04 20:25:35
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd k:mm:ss")

        override fun serialize(encoder: Encoder, obj: LetsRunDate) {
            encoder.encodeString(obj.datestamp.format(formatter))
        }

        override fun deserialize(decoder: Decoder): LetsRunDate {
            return LetsRunDate(LocalDateTime.parse(decoder.decodeString(), formatter))
        }
    }
}
