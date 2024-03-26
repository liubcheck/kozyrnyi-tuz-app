package liubomyr.stepanenko.kozyrnyituzapp.model

import com.google.gson.annotations.SerializedName

data class Barbershop (
    val id: Long,
    val name: String,
    val address: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double,
    val imageUrl: String,
)
