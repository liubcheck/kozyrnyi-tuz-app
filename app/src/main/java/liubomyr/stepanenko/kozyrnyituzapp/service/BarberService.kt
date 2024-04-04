package liubomyr.stepanenko.kozyrnyituzapp.service

import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BarberService {
    @GET("/barber")
    suspend fun getAllBarbers(): Response<List<Barber>>

    @GET("/barber/barbershop/{id}")
    suspend fun getBarberByShopId(@Path("id") id: String): Response<List<Barber>>
}