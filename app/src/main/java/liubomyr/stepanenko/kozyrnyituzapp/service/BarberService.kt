package liubomyr.stepanenko.kozyrnyituzapp.service

import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import retrofit2.Response
import retrofit2.http.GET

interface BarberService {
    @GET("/barber")
    suspend fun getAllBarbers(): Response<List<Barber>>
}