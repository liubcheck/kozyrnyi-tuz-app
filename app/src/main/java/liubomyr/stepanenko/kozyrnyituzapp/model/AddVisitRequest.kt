package liubomyr.stepanenko.kozyrnyituzapp.model

import java.time.LocalDateTime

data class AddVisitRequest(
    val barberId: Long,
    val userId: Long,
    val datetime: LocalDateTime,
    val durationMin: Int
)