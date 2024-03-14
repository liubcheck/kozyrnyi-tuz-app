package liubomyr.stepanenko.kozyrnyituzapp.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




data class Visit(
    val id: Long,
    val barberId: Long,
    val userId: Long,
    val datetime: LocalDateTime,
    val durationMin: Int,
) {
    fun getFormattedDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return datetime.format(formatter)
    }
}
