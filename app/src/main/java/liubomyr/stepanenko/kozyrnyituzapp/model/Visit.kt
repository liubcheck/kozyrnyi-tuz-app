package liubomyr.stepanenko.kozyrnyituzapp.model

import java.time.LocalDateTime

data class Visit (
    val id: Long,
    val barberId: Long,
    val userId: Long,
    val datetime: LocalDateTime,
    val durationMin: Int
)
